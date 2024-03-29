package ru.iwareq.anarchycore.manager.FakeInventory.Inventory;

import cn.nukkit.Player;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.math.BlockVector3;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.BlockEntityDataPacket;
import cn.nukkit.scheduler.NukkitRunnable;
import ru.iwareq.anarchycore.AnarchyCore;

import java.io.IOException;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.List;

public class DefaultDoubleChest extends DefaultChest {

	public DefaultDoubleChest(String title) {
		super(InventoryType.DOUBLE_CHEST, title);
	}

	private static byte[] getDoubleNbt(BlockVector3 pos, BlockVector3 pairPos, String name) {
		CompoundTag tag = new CompoundTag().putString("id", BlockEntity.CHEST).putInt("x", pos.x).putInt("y", pos.y).putInt("z", pos.z).putInt("pairx", pairPos.x).putInt("pairz", pairPos.z).putString("CustomName", name == null ? "Двойной сундук" : name);
		try {
			return NBTIO.write(tag, ByteOrder.LITTLE_ENDIAN, true);
		} catch (IOException exception) {
			throw new RuntimeException("Unable to create NBT for DoubleChest");
		}
	}

	@Override()
	public void onOpen(Player player) {
		this.viewers.add(player);
		List<BlockVector3> blocks = onOpenBlock(player);
		blockPositions.put(player, blocks);
		new NukkitRunnable() {

			@Override()
			public void run() {
				onFakeOpen(player, blocks);
			}
		}.runTaskLater(AnarchyCore.getInstance(), 10);
	}

	@Override()
	protected List<BlockVector3> onOpenBlock(Player player) {
		BlockVector3 blockPositionA = new BlockVector3(player.getFloorX(), player.getFloorY(), player.getFloorZ());
		if (player.getFloorY() > 200) {
			blockPositionA = blockPositionA.add(0, -2, 0);
		} else {
			blockPositionA = blockPositionA.add(0, 2, 0);
		}
		BlockVector3 blockPositionB = blockPositionA.add(1, 0, 0);
		if (player.getFloorY() <= 256) {
			placeChest(player, blockPositionA);
			placeChest(player, blockPositionB);
		}
		pair(player, blockPositionA, blockPositionB);
		pair(player, blockPositionB, blockPositionA);
		return Arrays.asList(blockPositionA, blockPositionB);
	}

	private void pair(Player player, BlockVector3 pos1, BlockVector3 pos2) {
		BlockEntityDataPacket blockEntityData = new BlockEntityDataPacket();
		blockEntityData.x = pos1.x;
		blockEntityData.y = pos1.y;
		blockEntityData.z = pos1.z;
		blockEntityData.namedTag = getDoubleNbt(pos1, pos2, this.getTitle());
		player.dataPacket(blockEntityData);
	}
}