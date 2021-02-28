package ru.jl1mbo.AnarchyCore.SystemProcessorHandler.CheatCheacker.EventsListener;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.CheatCheacker.CheatCheackerAPI;

public class EntityDamageListener implements Listener {

	@EventHandler()
	public void onEntityDamage(EntityDamageEvent event) {
		Entity entity = event.getEntity();
		if (entity instanceof Player) {
			if (CheatCheackerAPI.isCheatChecker(((Player)entity).getName())) {
				event.setCancelled(true);
			}
		}
		if (event instanceof EntityDamageByEntityEvent) {
			if (((EntityDamageByEntityEvent)event).getDamager() instanceof Player) {
				Player player = (Player)((EntityDamageByEntityEvent)event).getDamager();
				if (CheatCheackerAPI.isCheatChecker(player.getName())) {
					event.setCancelled(true);
				}
			}
		}
	}
}