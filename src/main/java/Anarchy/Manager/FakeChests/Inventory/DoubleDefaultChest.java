package Anarchy.Manager.FakeChests.Inventory;

import java.io.IOException;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.List;

import Anarchy.AnarchyMain;
import cn.nukkit.Player;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.math.BlockVector3;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.BlockEntityDataPacket;
import cn.nukkit.scheduler.NukkitRunnable;

public class DoubleDefaultChest extends DefaultChest {
	public DoubleDefaultChest(String title) {
		super(InventoryType.DOUBLE_CHEST, title);
	}

	@Override
	public void onOpen(Player who) {
		this.viewers.add(who);
		List <BlockVector3> blocks = onOpenBlock(who);
		blockPositions.put(who, blocks);
		new NukkitRunnable() {
			@Override
			public void run() {
				onFakeOpen(who, blocks);
			}
		}.runTaskLater(AnarchyMain.plugin, 10);
	}

	@Override
	protected List < BlockVector3 > onOpenBlock(Player who) {
		BlockVector3 blockPositionA = new BlockVector3((int) who.x, ((int) who.y) + 2, (int) who.z);
		BlockVector3 blockPositionB = blockPositionA.add(1, 0, 0);
		placeChest(who, blockPositionA);
		placeChest(who, blockPositionB);
		pair(who, blockPositionA, blockPositionB);
		pair(who, blockPositionB, blockPositionA);
		return Arrays.asList(blockPositionA, blockPositionB);
	}

	private void pair(Player who, BlockVector3 pos1, BlockVector3 pos2) {
		BlockEntityDataPacket blockEntityData = new BlockEntityDataPacket();
		blockEntityData.x = pos1.x;
		blockEntityData.y = pos1.y;
		blockEntityData.z = pos1.z;
		blockEntityData.namedTag = getDoubleNbt(pos1, pos2, getTitle());
		who.dataPacket(blockEntityData);
	}

	private static byte[] getDoubleNbt(BlockVector3 pos, BlockVector3 pairPos, String name) {
		CompoundTag tag = new CompoundTag().putString("id", BlockEntity.CHEST).putInt("x", pos.x).putInt("y", pos.y).putInt("z", pos.z).putInt("pairx", pairPos.x).putInt("pairz", pairPos.z).putString("CustomName", name == null ? "Двойной сундук": name);
		try {
			return NBTIO.write(tag, ByteOrder.LITTLE_ENDIAN, true);
		} catch(IOException e) {
			throw new RuntimeException("Unable to create NBT for Double Chest");
		}
	}
}