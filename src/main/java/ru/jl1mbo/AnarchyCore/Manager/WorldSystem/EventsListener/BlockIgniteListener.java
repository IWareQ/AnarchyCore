package ru.jl1mbo.AnarchyCore.Manager.WorldSystem.EventsListener;

import cn.nukkit.block.Block;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockIgniteEvent;
import ru.jl1mbo.AnarchyCore.Manager.WorldSystem.WorldSystemAPI;

public class BlockIgniteListener implements Listener {

	@EventHandler()
	public void onBlockIgnite(BlockIgniteEvent event) {
		Block block = event.getBlock();
		if (block.getLevel().equals(WorldSystemAPI.getSpawn())) {
			event.setCancelled(true);
		}
	}
}