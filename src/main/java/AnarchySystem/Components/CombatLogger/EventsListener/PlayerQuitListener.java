package AnarchySystem.Components.CombatLogger.EventsListener;

import AnarchySystem.Components.CombatLogger.CombatLoggerAPI;
import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @EventHandler()
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (CombatLoggerAPI.inCombat(player)) {
            CombatLoggerAPI.removeCombat(player);
            player.kill();
        }
    }
}