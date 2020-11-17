package Anarchy.Manager.FakeChests.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.base.Preconditions;

import Anarchy.AnarchyMain;
import cn.nukkit.Player;
import cn.nukkit.inventory.ContainerInventory;
import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.level.GlobalBlockPalette;
import cn.nukkit.math.BlockVector3;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.ContainerOpenPacket;
import cn.nukkit.network.protocol.UpdateBlockPacket;
import cn.nukkit.scheduler.NukkitRunnable;

public abstract class FakeChest extends ContainerInventory {
	private static final BlockVector3 ZERO = new BlockVector3(0, 0, 0);
	static final Map<Player, FakeChest> open = new ConcurrentHashMap<>();
	protected final Map<Player, List<BlockVector3>> blockPositions = new HashMap<>();
	private boolean closed = false;
	private String title;

	public FakeChest(InventoryType inventoryType, InventoryHolder inventoryHolder, String title) {
		super(inventoryHolder, inventoryType);
		this.title = title == null ? type.getDefaultTitle() : title;
	}

	@Override()
	public void onOpen(Player player) {
		checkForClosed();
		this.viewers.add(player);
		if (open.putIfAbsent(player, this) != null) {
			throw new IllegalStateException("Inventory was already open");
		}
		List<BlockVector3> blocks = onOpenBlock(player);
		blockPositions.put(player, blocks);
		onFakeOpen(player, blocks);
	}

	protected void onFakeOpen(Player player, List<BlockVector3> blocks) {
		BlockVector3 blockPosition = blocks.isEmpty() ? ZERO : blocks.get(0);
		ContainerOpenPacket containerOpen = new ContainerOpenPacket();
		containerOpen.windowId = player.getWindowId(this);
		containerOpen.type = this.getType().getNetworkType();
		containerOpen.x = blockPosition.x;
		containerOpen.y = blockPosition.y;
		containerOpen.z = blockPosition.z;
		player.dataPacket(containerOpen);
		this.sendContents(player);
	}

	protected abstract List<BlockVector3> onOpenBlock(Player player);

	@Override()
	public void onClose(Player player) {
		super.onClose(player);
		open.remove(player, this);
		List<BlockVector3> blocks = blockPositions.get(player);
		for (int i = 0, size = blocks.size(); i < size; i++) {
			final int index = i;
			new NukkitRunnable() {

				@Override()
				public void run() {
					Vector3 blockPosition = blocks.get(index).asVector3();
					UpdateBlockPacket updateBlock = new UpdateBlockPacket();
					updateBlock.blockRuntimeId = GlobalBlockPalette.getOrCreateRuntimeId(player.getLevel().getBlock(blockPosition).getFullId());
					updateBlock.flags = UpdateBlockPacket.FLAG_ALL_PRIORITY;
					updateBlock.x = blockPosition.getFloorX();
					updateBlock.y = blockPosition.getFloorY();
					updateBlock.z = blockPosition.getFloorZ();
					player.dataPacket(updateBlock);
				}
			} .runTaskLater(AnarchyMain.plugin, 10 + i);
		}
	}

	public List<BlockVector3> getPosition(Player player) {
		checkForClosed();
		return blockPositions.getOrDefault(player, null);
	}

	private void checkForClosed() {
		Preconditions.checkState(!closed, "Already closed");
	}

	public void close() {
		Preconditions.checkState(!closed, "Already closed");
		for (Player player : getViewers()) {
			player.removeWindow(this);
		}
		closed = true;
	}

	@Override()
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title == null ? type.getDefaultTitle() : title;
	}
}