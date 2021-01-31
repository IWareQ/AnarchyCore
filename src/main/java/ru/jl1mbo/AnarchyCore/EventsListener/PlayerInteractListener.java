package ru.jl1mbo.AnarchyCore.EventsListener;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerInteractEvent;
import ru.jl1mbo.AnarchyCore.Manager.WorldSystem.WorldSystemAPI;

public class PlayerInteractListener implements Listener {

	@EventHandler()
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (player.getLevel().equals(WorldSystemAPI.getMap()) || player.getLevel().equals(WorldSystemAPI.getSpawn())) {
			if (event.getBlock().getId() == Block.BED_BLOCK) {
				event.setCancelled(true);
			}
		}
	}
}