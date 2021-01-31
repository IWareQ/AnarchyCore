package ru.jl1mbo.AnarchyCore.GameHandler.CombatLogger.EventsListener;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import ru.jl1mbo.AnarchyCore.GameHandler.CombatLogger.CombatLoggerAPI;
import ru.jl1mbo.AnarchyCore.Manager.WorldSystem.WorldSystemAPI;

public class EntityDamageByEntityListener implements Listener {

	@EventHandler()
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		Entity victim = event.getEntity();
		Entity damager = event.getDamager();
		if (damager instanceof Player) {
			((Player) damager).sendTip("§l§f" + String.format("%.0f", victim.getHealth()) + " §c❤");
			if (victim instanceof Player && victim.getLevel() != WorldSystemAPI.getSpawn() && damager != victim) {
				for (Player players : new Player[] {(Player) victim, (Player) damager}) {
					CombatLoggerAPI.addCombat(players);
				}
			}
		}
	}
}