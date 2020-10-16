package Anarchy.Manager.FakeChests.Inventory;

import java.io.IOException;
import java.nio.ByteOrder;
import java.util.Collections;
import java.util.List;

import Anarchy.Manager.FakeChests.Utils.FakeChest;
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

public class DefaultCraftingTable extends FakeChest {

	public DefaultCraftingTable() {
		super(InventoryType.CRAFTING, null, null);
	}

	public DefaultCraftingTable(String title) {
		super(InventoryType.CRAFTING, null, title);
	}

	public DefaultCraftingTable(InventoryType inventoryType, String title) {
		super(inventoryType, null, title);
	}

	@Override()
	protected List<BlockVector3> onOpenBlock(Player player) {
		BlockVector3 blockPosition = new BlockVector3((int)player.x, ((int)player.y) + 2, (int)player.z);
		placeCraftingTable(player, blockPosition);
		return Collections.singletonList(blockPosition);
	}

	protected void placeCraftingTable(Player player, BlockVector3 pos) {
		UpdateBlockPacket updateBlock = new UpdateBlockPacket();
		updateBlock.blockRuntimeId = GlobalBlockPalette.getOrCreateRuntimeId(BlockID.CRAFTING_TABLE, 0);
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

	private static byte[] getNbt(BlockVector3 pos, String name) {
		CompoundTag tag = new CompoundTag().putString("id", BlockEntity.CHEST).putInt("x", pos.x).putInt("y", pos.y).putInt("z", pos.z);
		try {
			return NBTIO.write(tag, ByteOrder.LITTLE_ENDIAN, true);
		} catch (IOException e) {
			throw new RuntimeException("Unable to create NBT for CraftingTable");
		}
	}
}