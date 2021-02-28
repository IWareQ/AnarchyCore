package ru.jl1mbo.AnarchyCore.SystemProcessorHandler.BanSystem.EventsListener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerDropItemEvent;
import ru.jl1mbo.AnarchyCore.Manager.WorldSystem.WorldSystemAPI;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.BanSystem.BanSystemAPI;

public class PlayerDropItemListener implements Listener {

	@EventHandler()
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		if (BanSystemAPI.IsBanned(player.getName())) {
			event.setCancelled(true);
		}
	}
}