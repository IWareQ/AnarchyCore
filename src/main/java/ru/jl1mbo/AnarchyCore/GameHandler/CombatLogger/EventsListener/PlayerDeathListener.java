package ru.jl1mbo.AnarchyCore.GameHandler.CombatLogger.EventsListener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerDeathEvent;
import ru.jl1mbo.AnarchyCore.GameHandler.CombatLogger.CombatLoggerAPI;

public class PlayerDeathListener implements Listener {

    @EventHandler()
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (CombatLoggerAPI.inCombat(player)) {
            CombatLoggerAPI.removeCombat(player);
        }
    }
}