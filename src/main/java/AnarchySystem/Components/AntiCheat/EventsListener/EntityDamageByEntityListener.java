package AnarchySystem.Components.AntiCheat.EventsListener;

import AnarchySystem.Components.AntiCheat.AntiCheatAPI;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageByEntityListener implements Listener {

    @EventHandler()
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        AntiCheatAPI.checkReach(event);
    }
}