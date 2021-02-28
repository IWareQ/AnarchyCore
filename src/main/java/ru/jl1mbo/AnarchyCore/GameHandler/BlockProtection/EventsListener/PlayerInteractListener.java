package ru.jl1mbo.AnarchyCore.GameHandler.BlockProtection.EventsListener;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerInteractEvent;
import ru.jl1mbo.AnarchyCore.GameHandler.BlockProtection.BlockProtectionAPI;
import ru.jl1mbo.AnarchyCore.Manager.WorldSystem.WorldSystemAPI;

public class PlayerInteractListener implements Listener {

	@EventHandler()
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();
		if (player.getLevel() != WorldSystemAPI.getMap()) {
			player.sendTip("Этот биом §6не доступен §fдля строительства");
		}
		if (BlockProtectionAPI.canInteractHere(player, block.getLocation())) {
			player.sendTip("Территория §6не доступна §fдля взаимодействия");
			event.setCancelled(true);
		}
	}
}