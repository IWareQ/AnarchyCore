package ru.jl1mbo.AnarchyCore.SystemProcessorHandler.EconomyAPI.EventsListener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.EconomyAPI.EconomyAPI;

public class PlayerJoinListener implements Listener {

	@EventHandler()
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (!EconomyAPI.isRegister(player.getName())) {
			EconomyAPI.registerPlayer(player.getName());
		}
	}
}