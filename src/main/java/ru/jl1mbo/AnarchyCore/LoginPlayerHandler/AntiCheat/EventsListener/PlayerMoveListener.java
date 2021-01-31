package ru.jl1mbo.AnarchyCore.LoginPlayerHandler.AntiCheat.EventsListener;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerMoveEvent;
import ru.jl1mbo.AnarchyCore.LoginPlayerHandler.AntiCheat.AntiCheatAPI;

public class PlayerMoveListener implements Listener {

	@EventHandler()
	public void onPlayerMove(PlayerMoveEvent event) {
		AntiCheatAPI.checkFly(event);
	}
}