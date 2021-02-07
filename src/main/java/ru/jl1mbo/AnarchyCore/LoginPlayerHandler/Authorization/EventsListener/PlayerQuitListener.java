package ru.jl1mbo.AnarchyCore.LoginPlayerHandler.Authorization.EventsListener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerQuitEvent;
import ru.jl1mbo.AnarchyCore.LoginPlayerHandler.Authorization.AuthorizationAPI;
import ru.jl1mbo.AnarchyCore.Utils.Utils;

public class PlayerQuitListener implements Listener {

	@EventHandler()
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if (AuthorizationAPI.playerTime.get(player.getName().toLowerCase()) != null) {
			long stopTime = System.currentTimeMillis() / 1000L - AuthorizationAPI.playerTime.get(player.getName().toLowerCase());
			AuthorizationAPI.updatePlayerInfo(player.getName(), stopTime, player.getAddress(), Utils.getDate());
		}
		event.setQuitMessage("");
	}
}