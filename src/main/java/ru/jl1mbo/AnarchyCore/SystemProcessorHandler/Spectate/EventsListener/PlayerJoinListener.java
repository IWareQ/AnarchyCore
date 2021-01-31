package ru.jl1mbo.AnarchyCore.SystemProcessorHandler.Spectate.EventsListener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.Spectate.SpectateAPI;

public class PlayerJoinListener implements Listener {

	@EventHandler()
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (SpectateAPI.isSpectate(player.getName())) {
			SpectateAPI.removeSpectate(player);
		}
	}
}