package ru.jl1mbo.AnarchyCore.GameHandler.CombatLogger.EventsListener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerQuitEvent;
import ru.jl1mbo.AnarchyCore.GameHandler.CombatLogger.CombatLoggerAPI;

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