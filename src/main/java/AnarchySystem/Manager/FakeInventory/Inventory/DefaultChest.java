package AnarchySystem.Manager.FakeInventory.Inventory;

import java.io.IOException;
import java.nio.ByteOrder;
import java.util.Collections;
import java.util.List;

import AnarchySystem.Manager.FakeInventory.Utils.FakeChest;
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

public class DefaultChest extends FakeChest {

    public DefaultChest() {
        super(InventoryType.CHEST, null, null);
    }

    public DefaultChest(String title) {
        super(InventoryType.CHEST, null, title);
    }

    public DefaultChest(InventoryType inventoryType, String title) {
        super(inventoryType, null, title);
    }

    private static byte[] getNbt(BlockVector3 pos, String name) {
        CompoundTag tag = new CompoundTag()
                .putString("id", BlockEntity.CHEST)
                .putInt("x", pos.x)
                .putInt("y", pos.y)
                .putInt("z", pos.z)
                .putString("CustomName", name == null ? "Сундук" : name);
        try {
            return NBTIO.write(tag, ByteOrder.LITTLE_ENDIAN, true);
        } catch (IOException e) {
            throw new RuntimeException("Unable to create NBT for Chest");
        }
    }

    @Override()
    protected List<BlockVector3> onOpenBlock(Player player) {
        BlockVector3 blockPosition = new BlockVector3(player.getFloorX(), player.getFloorY() + 2, player.getFloorZ());
        placeChest(player, blockPosition);
        return Collections.singletonList(blockPosition);
    }

    @Override()
    public void onClose(Player player) {
        super.onClose(player);
    }

    protected void placeChest(Player player, BlockVector3 pos) {
        UpdateBlockPacket updateBlock = new UpdateBlockPacket();
        updateBlock.blockRuntimeId = GlobalBlockPalette.getOrCreateRuntimeId(BlockID.CHEST, 0);
        updateBlock.flags = UpdateBlockPacket.FLAG_ALL_PRIORITY;
        updateBlock.x = pos.x;
        updateBlock.y = pos.y;
        updateBlock.z = pos.z;
        player.dataPacket(updateBlock);
        BlockEntityDataPacket blockEntityData = new BlockEntityDataPacket();
        blockEntityData.x = pos.x;
        blockEntityData.y = pos.y;
        blockEntityData.z = pos.z;
        blockEntityData.namedTag = getNbt(pos, getTitle());
        player.dataPacket(blockEntityData);
    }
}