package ru.iwareq.anarchycore.module.Clans;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;

public class ClanEventsListener implements Listener {

	@EventHandler()
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		Entity victim = event.getEntity();
		Entity damager = event.getDamager();
		if (damager instanceof Player && victim instanceof Player) {
			if (ClanAPI.isTeam(damager.getName(), victim.getName())) {
				event.setCancelled(true);
			}
		}
	}
}