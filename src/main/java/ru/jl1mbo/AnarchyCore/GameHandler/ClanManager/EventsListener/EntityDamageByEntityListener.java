package ru.jl1mbo.AnarchyCore.GameHandler.ClanManager.EventsListener;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import ru.jl1mbo.AnarchyCore.GameHandler.ClanManager.ClanManager;

public class EntityDamageByEntityListener implements Listener {

	@EventHandler()
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		Entity victim = event.getEntity();
		Entity damager = event.getDamager();
		if (damager instanceof Player && victim instanceof Player) {
			if (ClanManager.getPlayerClan(damager.getName()).equalsIgnoreCase(ClanManager.getPlayerClan(victim.getName()))) {
				event.setCancelled(true);
			}
		}
	}
}