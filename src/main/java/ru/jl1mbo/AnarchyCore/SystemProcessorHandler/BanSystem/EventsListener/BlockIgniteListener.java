package ru.jl1mbo.AnarchyCore.SystemProcessorHandler.BanSystem.EventsListener;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockIgniteEvent;
import ru.jl1mbo.AnarchyCore.Manager.WorldSystem.WorldSystemAPI;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.BanSystem.BanSystemAPI;

public class BlockIgniteListener implements Listener {

	@EventHandler()
	public void onBlockIgnite(BlockIgniteEvent event) {
		Entity entity = event.getEntity();
		if (entity instanceof Player) {
			if (BanSystemAPI.IsBanned(((Player)entity).getName())) {
				event.setCancelled(true);
			}
		}
	}
}