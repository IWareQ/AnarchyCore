package ru.jl1mbo.AnarchyCore.LoginPlayerHandler.AntiCheat.EventsListener;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import ru.jl1mbo.AnarchyCore.LoginPlayerHandler.AntiCheat.AntiCheatAPI;

public class PlayerJoinListener implements Listener {

	@EventHandler()
	public void onPlayerJoin(PlayerJoinEvent event) {
		AntiCheatAPI.addDetect(event);
	}
}