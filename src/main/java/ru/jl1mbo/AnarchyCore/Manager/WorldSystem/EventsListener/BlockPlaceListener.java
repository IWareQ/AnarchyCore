package ru.jl1mbo.AnarchyCore.Manager.WorldSystem.EventsListener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockPlaceEvent;
import ru.jl1mbo.AnarchyCore.Manager.WorldSystem.WorldSystemAPI;

public class BlockPlaceListener implements Listener {

	@EventHandler()
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		if (player.getLevel().equals(WorldSystemAPI.getSpawn()) && !player.hasPermission("Development")) {
			event.setCancelled(true);
		}
	}
}