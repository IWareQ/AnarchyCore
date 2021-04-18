package ru.jl1mbo.AnarchyCore.Modules.CombatLogger;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.player.PlayerCommandPreprocessEvent;
import cn.nukkit.event.player.PlayerDeathEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import ru.jl1mbo.AnarchyCore.Manager.WorldSystem.WorldSystemAPI;
import ru.jl1mbo.AnarchyCore.Modules.AdminSystem.AdminAPI;
import ru.jl1mbo.AnarchyCore.Modules.Clans.ClanAPI;

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
							for (Player players : new Player[] {(Player) victim, (Player) damager}) {
								CombatLoggerAPI.addCombat(players);
							}
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