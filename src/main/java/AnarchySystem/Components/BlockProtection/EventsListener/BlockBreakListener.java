package AnarchySystem.Components.BlockProtection.EventsListener;

import AnarchySystem.Components.BlockProtection.BlockProtectionAPI;
import AnarchySystem.Components.BlockProtection.Utils.SQLiteUtils;
import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {

    @EventHandler()
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        if (BlockProtectionAPI.canInteractHere(player, block.getLocation())) {
            player.sendTip(BlockProtectionAPI.BUSY);
            event.setCancelled(true);
            return;
        }
        int regionID = BlockProtectionAPI.getRegionIDByLocation(block.getLocation());
        if (regionID != -1) {
            Integer mainX = SQLiteUtils.selectInteger("SELECT Main_X FROM AREAS WHERE (Region_ID = '" + regionID + "');");
            Integer mainY = SQLiteUtils.selectInteger("SELECT Main_Y FROM AREAS WHERE (Region_ID = '" + regionID + "');");
            Integer mainZ = SQLiteUtils.selectInteger("SELECT Main_Z FROM AREAS WHERE (Region_ID = '" + regionID + "');");
            if (block.getFloorX() == mainX && block.getFloorY() == mainY && block.getFloorZ() == mainZ) {
                if (BlockProtectionAPI.isRegionOwner(player.getName(), regionID)) {
                    player.sendMessage(BlockProtectionAPI.PREFIX + "§fРегион §7#§6" + regionID + " §fуспешно удален§7!");
                    SQLiteUtils.query("DELETE FROM AREAS WHERE Region_ID = '" + regionID + "';");
                    SQLiteUtils.query("DELETE FROM MEMBERS WHERE Region_ID = '" + regionID + "';");
                } else {
                    player.sendMessage(BlockProtectionAPI.PREFIX + "§fВы не можете удалить чужой регион§7!");
                    event.setCancelled(true);
                }
            }
        }
    }
}