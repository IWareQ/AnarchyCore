package ru.iwareq.anarchycore.module.CombatLogger;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.player.PlayerCommandPreprocessEvent;
import cn.nukkit.event.player.PlayerDeathEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import ru.iwareq.anarchycore.manager.WorldSystem.WorldSystemAPI;
import ru.iwareq.anarchycore.module.AdminSystem.AdminAPI;
import ru.iwareq.anarchycore.module.Clans.ClanAPI;

public class CombatEventsListener implements Listener {

	@EventHandler()
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		Entity victim = event.getEntity();
		Entity damager = event.getDamager();
		if (damager instanceof Player && victim instanceof Player) {
			if (!AdminAPI.isBanned(victim.getName())) {
				if (!AdminAPI.isCheatCheck(victim.getName())) {
					if (!ClanAPI.isTeam(damager.getName(), victim.getName())) {
						if (victim.getLevel() != WorldSystemAPI.Spawn && damager != victim) {
							CombatLoggerAPI.addCombat((Player) damager, (Player) victim);
						}
					}
				}
			}
		}
	}

	@EventHandler()
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		if (CombatLoggerAPI.inCombat(player)) {
			player.sendMessage("§l§7(§3PvP§7) §rВы не можете использовать команды в режиме §6PvP§7!");
			event.setCancelled(true);
		}
	}

	@EventHandler()
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		if (CombatLoggerAPI.inCombat(player)) {
			CombatLoggerAPI.removeCombat(player);
		}
	}

	@EventHandler()
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if (CombatLoggerAPI.inCombat(player)) {
			CombatLoggerAPI.removeCombat(player);
			player.kill();
		}
	}
}