package ru.jl1mbo.AnarchyCore.GameHandler.CombatLogger.EventsListener;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import ru.jl1mbo.AnarchyCore.GameHandler.ClanManager.ClanManager;
import ru.jl1mbo.AnarchyCore.GameHandler.CombatLogger.CombatLoggerAPI;
import ru.jl1mbo.AnarchyCore.Manager.WorldSystem.WorldSystemAPI;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.CheatCheacker.CheatCheackerAPI;

public class EntityDamageByEntityListener implements Listener {

	@EventHandler()
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		Entity victim = event.getEntity();
		Entity damager = event.getDamager();
		if (damager instanceof Player && victim instanceof Player) {
			if (!CheatCheackerAPI.isCheatChecker(((Player)victim).getName()) || !CheatCheackerAPI.isCheatChecker(((Player)damager).getName())) {
				if (!ClanManager.getPlayerClan(damager.getName()).equalsIgnoreCase(ClanManager.getPlayerClan(victim.getName()))
						|| !ClanManager.getPlayerClan(victim.getName()).equalsIgnoreCase(ClanManager.getPlayerClan(damager.getName())))
					if (victim.getLevel() != WorldSystemAPI.getSpawn() && damager != victim) {
						for (Player players : new Player[] {(Player) victim, (Player) damager}) {
							CombatLoggerAPI.addCombat(players);
						}
					}
			}
		}
	}
}