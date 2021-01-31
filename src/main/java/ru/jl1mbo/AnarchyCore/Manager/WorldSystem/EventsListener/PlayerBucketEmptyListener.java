package ru.jl1mbo.AnarchyCore.Manager.WorldSystem.EventsListener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerBucketEmptyEvent;
import ru.jl1mbo.AnarchyCore.Manager.WorldSystem.WorldSystemAPI;

public class PlayerBucketEmptyListener implements Listener {

	@EventHandler()
	public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
		Player player = event.getPlayer();
		if (player.getLevel().equals(WorldSystemAPI.getSpawn()) && !player.hasPermission("Development")) {
			event.setCancelled(true);
		}
	}
}