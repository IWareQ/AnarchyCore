package ru.jl1mbo.AnarchyCore.SystemProcessorHandler.BanSystem.EventsListener;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.BanSystem.BanSystemAPI;

public class EntityDamageByEntityListener implements Listener {

	@EventHandler()
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		Entity victim = event.getEntity();
		Entity damager = event.getDamager();
		if (BanSystemAPI.IsBanned(damager.getName())) {
			if (victim instanceof Player) {
				event.setCancelled(true);
			}
		}
	}
}