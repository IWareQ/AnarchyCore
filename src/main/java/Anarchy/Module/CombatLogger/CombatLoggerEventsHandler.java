package Anarchy.Module.CombatLogger;

import Anarchy.Manager.Functions.FunctionsAPI;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.player.PlayerCommandPreprocessEvent;
import cn.nukkit.event.player.PlayerDeathEvent;
import cn.nukkit.event.player.PlayerQuitEvent;

public class CombatLoggerEventsHandler implements Listener {

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onEntityDamage(EntityDamageEvent event) {
		if (event instanceof EntityDamageByEntityEvent) {
			Entity victim = event.getEntity();
			Entity damager = ((EntityDamageByEntityEvent)event).getDamager();
			if (victim instanceof Player && damager instanceof Player && victim.getLevel() != FunctionsAPI.SPAWN && damager.getLevel() != FunctionsAPI.SPAWN && damager != victim) {
				((Player)damager).sendTip("§l§f" + String.format("%.0f", victim.getHealth()) + " §c❤");
				for (Player players : new Player[] {(Player)victim, (Player)damager}) {
					CombatLoggerAPI.addCombat(players);
				}
			}
			if (damager instanceof Player && (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK)) {
				if (damager.distance(victim) > 4) {
					for (Player player : Server.getInstance().getOnlinePlayers().values()) {
						if (player.hasPermission("Command.A")) {
							player.sendMessage("§fНовая жалоба§7! (§6AntiCheat§7)\n§fНарушитель§7: §6" + damager.getName() + "\n§fПричина§7: §6Хитбоксы");
						}
					}
				}
			}
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if (CombatLoggerAPI.inCombat(player)) {
			CombatLoggerAPI.removeCombat(player);
			player.kill();
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		if (CombatLoggerAPI.inCombat(player)) {
			CombatLoggerAPI.removeCombat(player);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		Long cooldownTime = CombatLoggerAPI.inCombat.get(player);
		Long nowTime = System.currentTimeMillis() / 1000L;
		if (CombatLoggerAPI.inCombat(player)) {
			player.sendMessage("§l§7(§3PvP§7) §r§fВы не можете использовать команды в режиме §6PvP§7! §fПодождите еще §6" + (cooldownTime / 1000L + 30 - nowTime) + " §fсек§7.");
			event.setCancelled(true);
		}
	}
}