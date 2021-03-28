package ru.jl1mbo.AnarchyCore.Manager.FakeInventory.Inventory;

import java.io.IOException;
import java.nio.ByteOrder;
import java.util.Collections;
import java.util.List;

import cn.nukkit.Player;
import cn.nukkit.block.BlockID;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.level.GlobalBlockPalette;
import cn.nukkit.math.BlockVector3;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.BlockEntityDataPacket;
import cn.nukkit.network.protocol.UpdateBlockPacket;
import ru.jl1mbo.AnarchyCore.Manager.FakeInventory.Utils.FakeChest;

public class DefaultHopper extends FakeChest {

	public DefaultHopper() {
		super(InventoryType.HOPPER, null, null);
	}

	public DefaultHopper(String title) {
		super(InventoryType.HOPPER, null, title);
	}

	public DefaultHopper(InventoryType inventoryType, String title) {
		super(inventoryType, null, title);
	}

	private static byte[] getNbt(BlockVector3 pos, String name) {
		CompoundTag tag = new CompoundTag()
		.putString("id", BlockEntity.HOPPER)
		.putInt("x", pos.x)
		.putInt("y", pos.y)
		.putInt("z", pos.z)
		.putString("CustomName", name == null ? "Воронка" : name);
		try {
			return NBTIO.write(tag, ByteOrder.LITTLE_ENDIAN, true);
		} catch (IOException e) {
			throw new RuntimeException("Unable to create NBT for Hopper");
		}
	}

	@Override()
	protected List<BlockVector3> onOpenBlock(Player player) {
		BlockVector3 blockPosition = new BlockVector3(player.getFloorX(), player.getFloorY() + 2, player.getFloorZ());
		placeHopper(player, blockPosition);
		return Collections.singletonList(blockPosition);
	}

	protected void placeHopper(Player player, BlockVector3 pos) {
		UpdateBlockPacket updateBlock = new UpdateBlockPacket();
		updateBlock.blockRuntimeId = GlobalBlockPalette.getOrCreateRuntimeId(BlockID.HOPPER_BLOCK, 0);
		updateBlock.flags = UpdateBlockPacket.FLAG_ALL_PRIORITY;
		updateBlock.x = pos.x;
		updateBlock.y = pos.y;
		updateBlock.z = pos.z;
		player.dataPacket(updateBlock);
		BlockEntityDataPacket blockEntityData = new BlockEntityDataPacket();
		blockEntityData.x = pos.x;
		blockEntityData.y = pos.y;
		blockEntityData.z = pos.z;
		blockEntityData.namedTag = getNbt(pos, getName());
		player.dataPacket(blockEntityData);
	}
}