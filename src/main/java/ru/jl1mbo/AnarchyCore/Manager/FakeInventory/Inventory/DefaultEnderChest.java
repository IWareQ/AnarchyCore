package ru.jl1mbo.AnarchyCore.Manager.FakeInventory.Inventory;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.level.GlobalBlockPalette;
import cn.nukkit.math.BlockVector3;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.BlockEntityDataPacket;
import cn.nukkit.network.protocol.UpdateBlockPacket;
import ru.jl1mbo.AnarchyCore.Manager.FakeInventory.Utils.FakeChest;

import java.io.IOException;
import java.nio.ByteOrder;
import java.util.Collections;
import java.util.List;

public class DefaultEnderChest extends FakeChest {

	public DefaultEnderChest() {
		super(InventoryType.ENDER_CHEST, null, null);
	}

	public DefaultEnderChest(String title) {
		super(InventoryType.ENDER_CHEST, null, title);
	}

	public DefaultEnderChest(InventoryType inventoryType, String title) {
		super(inventoryType, null, title);
	}

	private static byte[] getNbt(BlockVector3 pos, String name) {
		CompoundTag namedTag = new CompoundTag().putString("id", BlockEntity.ENDER_CHEST).putInt("x", pos.x).putInt("y", pos.y).putInt("z", pos.z).putString("CustomName", name == null ? "Сундук края" : name);
		try {
			return NBTIO.write(namedTag, ByteOrder.LITTLE_ENDIAN, true);
		} catch (IOException exception) {
			throw new RuntimeException("Unable to create NBT for EnderChest");
		}
	}

	@Override()
	protected List<BlockVector3> onOpenBlock(Player player) {
		BlockVector3 blockPosition = new BlockVector3(player.getFloorX(), player.getFloorY(), player.getFloorZ());
		if (player.getFloorY() > 200) {
			blockPosition = blockPosition.add(0, -2, 0);
		} else {
			blockPosition = blockPosition.add(0, 2, 0);
		}
		if (player.getFloorY() <= 256) {
			placeEnderChest(player, blockPosition);
		}
		return Collections.singletonList(blockPosition);
	}

	@Override()
	public void onClose(Player player) {
		super.onClose(player);
	}

	protected void placeEnderChest(Player player, BlockVector3 pos) {
		UpdateBlockPacket updateBlock = new UpdateBlockPacket();
		updateBlock.blockRuntimeId = GlobalBlockPalette.getOrCreateRuntimeId(Block.ENDER_CHEST, 0);
		updateBlock.flags = UpdateBlockPacket.FLAG_ALL_PRIORITY;
		updateBlock.x = pos.x;
		updateBlock.y = pos.y;
		updateBlock.z = pos.z;
		player.dataPacket(updateBlock);
		BlockEntityDataPacket blockEntityData = new BlockEntityDataPacket();
		blockEntityData.x = pos.x;
		blockEntityData.y = pos.y;
		blockEntityData.z = pos.z;
		blockEntityData.namedTag = getNbt(pos, this.getTitle());
		player.dataPacket(blockEntityData);
	}
}