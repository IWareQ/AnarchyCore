package AnarchySystem.Components.BlockProtection.EventsListener;

import AnarchySystem.Components.BlockProtection.BlockProtectionAPI;
import cn.nukkit.block.Block;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockPistonEvent;

public class BlockPistonListener implements Listener {

    @EventHandler()
    public void onBlockPiston(BlockPistonEvent event) {
        Block block = event.getBlock();
        if (BlockProtectionAPI.REGIONS.get(block.getId()) != null || block.getId() == 52) {
            event.setCancelled(true);
        }
    }
}