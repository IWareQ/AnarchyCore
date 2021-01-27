package AnarchySystem.Components.CombatLogger.EventsListener;

import AnarchySystem.Components.CombatLogger.CombatLoggerAPI;
import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    @EventHandler()
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (CombatLoggerAPI.inCombat(player)) {
            CombatLoggerAPI.removeCombat(player);
        }
    }
}