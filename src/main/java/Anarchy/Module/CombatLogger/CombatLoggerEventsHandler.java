package Anarchy.Module.CombatLogger;

import Anarchy.Manager.Functions.FunctionsAPI;
import cn.nukkit.Player;
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
		EntityDamageByEntityEvent ev;
		Entity entity = event.getEntity();
		if (!entity.getLevel().equals(FunctionsAPI.WORLD2)) {
			if (event instanceof EntityDamageByEntityEvent && (ev = (EntityDamageByEntityEvent)event).getDamager() instanceof Player && entity instanceof Player) {
				Player player = (Player)entity;
				Player damager = (Player)ev.getDamager();
				double health = player.getHealth();
				damager.sendTip("§l§f" + String.format("%.1f", health) + " §c❤");
				if (damager != player) {
					for (Player players : new Player[]{player, damager}) {
						CombatLoggerAPI.addCombat(players);
					}
				}
			}
		}
		if (entity.getLevel().equals(FunctionsAPI.WORLD2)) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if (CombatLoggerAPI.inCombat(player)) {
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
		Long cooldownTime = CombatLoggerAPI.inCombat.get(player) / 1000L + 30;
		Long nowTime = System.currentTimeMillis() / 1000L;
		if (CombatLoggerAPI.inCombat(player)) {
			player.sendMessage("§l§7(§3PvP§7) §r§fВы не можете использовать команды в режиме §6PvP§7! §fПодождите еще §6" + (cooldownTime - nowTime) + " §fсек§7.");
			event.setCancelled(true);
		}
	}
}