package ru.iwareq.anarchycore.module.AntiCheat;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerLoginEvent;

public class AntiCheatEventsListener implements Listener {


	@EventHandler()
	public void onPlayerLogin(PlayerLoginEvent event) {
		Player player = event.getPlayer();
		AntiCheatAPI.addDetect(event);
		if (AntiCheatAPI.checkToolBox(player)) {
			player.close("", "На нашем сервере запрещены читы!");
		}
	}
}
