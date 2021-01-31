package ru.jl1mbo.AnarchyCore.LoginPlayerHandler.AntiCheat.EventsListener;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import ru.jl1mbo.AnarchyCore.LoginPlayerHandler.AntiCheat.AntiCheatAPI;

public class EntityDamageByEntityListener implements Listener {

	@EventHandler()
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		AntiCheatAPI.checkReach(event);
	}
}