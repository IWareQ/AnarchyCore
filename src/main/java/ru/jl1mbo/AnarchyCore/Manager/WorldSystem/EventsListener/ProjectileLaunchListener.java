package ru.jl1mbo.AnarchyCore.Manager.WorldSystem.EventsListener;

import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.ProjectileLaunchEvent;
import ru.jl1mbo.AnarchyCore.Manager.WorldSystem.WorldSystemAPI;

public class ProjectileLaunchListener implements Listener {

	@EventHandler()
	public void onProjectileLaunch(ProjectileLaunchEvent event) {
		Entity entity = event.getEntity();
		if (entity.getLevel().equals(WorldSystemAPI.getSpawn())) {
			event.setCancelled(true);
		}
	}
}