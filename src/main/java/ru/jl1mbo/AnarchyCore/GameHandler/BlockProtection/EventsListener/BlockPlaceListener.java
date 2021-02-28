package ru.jl1mbo.AnarchyCore.GameHandler.BlockProtection.EventsListener;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockPlaceEvent;
import ru.jl1mbo.AnarchyCore.GameHandler.BlockProtection.BlockProtectionAPI;

public class BlockPlaceListener implements Listener {

	@EventHandler()
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();
		if (BlockProtectionAPI.canInteractHere(player, block.getLocation())) {
			player.sendTip("Территория §6не доступна §fдля взаимодействия");
			event.setCancelled(true);
		}
		if (BlockProtectionAPI.getAllBlocks().containsKey(block.getId())) {
			BlockProtectionAPI.placeRegion(player, block);
		}
	}
}