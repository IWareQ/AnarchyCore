package ru.jl1mbo.AnarchyCore.SystemProcessorHandler.CheatCheacker.EventsListener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJumpEvent;
import cn.nukkit.math.Vector3;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.CheatCheacker.CheatCheackerAPI;

public class PlayerJumpListener implements Listener {

	@EventHandler()
	public void onPlayerJump(PlayerJumpEvent event) {
		Player player = event.getPlayer();
		if (CheatCheackerAPI.isCheatChecker(player.getName())) {
			player.teleport(new Vector3(player.getFloorX() + 0.5, player.getFloorY(), player.getFloorZ() + 0.5));
		}
	}
}