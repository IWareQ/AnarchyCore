package ru.jl1mbo.AnarchyCore.SystemProcessorHandler.CheatCheacker.EventsListener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerQuitEvent;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.BanSystem.BanSystemAPI;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.CheatCheacker.CheatCheackerAPI;

public class PlayerQuitListener implements Listener {

	@EventHandler()
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if (CheatCheackerAPI.isCheatChecker(player.getName())) {
			CheatCheackerAPI.removeCheatCheacker(player.getName());
			BanSystemAPI.addBan(player.getName(), "Перезашел при проверке", "CheatCheacker", 30 * 86400);
		}
	}
}