package ru.jl1mbo.AnarchyCore.LoginPlayerHandler.Authorization.EventsListener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerLocallyInitializedEvent;
import cn.nukkit.level.Position;
import ru.jl1mbo.AnarchyCore.Manager.WorldSystem.WorldSystemAPI;

public class PlayerLocallyInitializedListener implements Listener {

	@EventHandler()
	public void onPlayerLocallyInitialized(PlayerLocallyInitializedEvent event) {
		Player player = event.getPlayer();
		if (!player.hasPlayedBefore()) {
			player.teleport(new Position(79.5, 93, 55.5, WorldSystemAPI.getSpawn()));
			player.setSpawn(new Position(79.5, 93, 55.5, WorldSystemAPI.getSpawn()));
		}
	}
}