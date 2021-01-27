package AnarchySystem.Components.BlockProtection.EventsListener;

import java.util.List;

import AnarchySystem.Components.BlockProtection.BlockProtectionAPI;
import cn.nukkit.block.Block;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityExplodeEvent;

public class EntityExplodeListener implements Listener {

    @EventHandler()
    public void onEntityExplode(EntityExplodeEvent event) {
        List<Block> blocks = event.getBlockList();
        blocks.removeIf((block) -> BlockProtectionAPI.REGIONS.get(block.getId()) != null || block.getId() == 52);
        event.setBlockList(blocks);
    }
}