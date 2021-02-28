package ru.jl1mbo.AnarchyCore.SystemProcessorHandler.CheatCheacker.EventsListener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockPlaceEvent;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.CheatCheacker.CheatCheackerAPI;

public class BlockPlaceListener implements Listener {

	@EventHandler()
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		if (CheatCheackerAPI.isCheatChecker(player.getName())) {
			event.setCancelled(true);
		}
	}
}