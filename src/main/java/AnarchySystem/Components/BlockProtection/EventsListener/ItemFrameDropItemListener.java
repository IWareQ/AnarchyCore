package AnarchySystem.Components.BlockProtection.EventsListener;

import AnarchySystem.Components.BlockProtection.BlockProtectionAPI;
import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.ItemFrameDropItemEvent;

public class ItemFrameDropItemListener implements Listener {

    @EventHandler()
    public void onItemFrameDropItem(ItemFrameDropItemEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        if (BlockProtectionAPI.canInteractHere(player, block.getLocation())) {
            player.sendTip(BlockProtectionAPI.BUSY);
            event.setCancelled(true);
        }
    }
}