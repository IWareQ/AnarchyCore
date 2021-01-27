package AnarchySystem.Components.CombatLogger.EventsListener;

import AnarchySystem.Components.CombatLogger.CombatLoggerAPI;
import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerCommandPreprocessEvent;

public class PlayerCommandPreprocessListener implements Listener {

    @EventHandler()
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        Long cooldownTime = CombatLoggerAPI.inCombat.get(player);
        Long nowTime = System.currentTimeMillis() / 1000L;
        if (CombatLoggerAPI.inCombat(player)) {
            player.sendMessage("§l§7(§3PvP§7) §r§fВы не можете использовать команды в режиме §6PvP§7! §fПодождите еще §6" + (cooldownTime - nowTime) + " §fсек§7.");
            event.setCancelled(true);
        }
    }
}