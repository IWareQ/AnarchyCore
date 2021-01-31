package ru.jl1mbo.AnarchyCore.LoginPlayerHandler.Authorization.EventsListener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerQuitEvent;
import ru.jl1mbo.AnarchyCore.LoginPlayerHandler.Authorization.AuthorizationAPI;

public class PlayerQuitListener implements Listener {

	@EventHandler()
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if (PlayerJoinListener.startPlayerTime != null) {
			long stopTime = System.currentTimeMillis() / 1000L - PlayerJoinListener.startPlayerTime / 1000L;
			AuthorizationAPI.updateGameTime(player.getName(), stopTime);
		}
		event.setQuitMessage("");
	}
}