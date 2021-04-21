package ru.jl1mbo.AnarchyCore.Modules.AntiCheat;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.player.PlayerLoginEvent;
import cn.nukkit.event.player.PlayerMoveEvent;

public class AntiCheatEventsListener implements Listener {

	@EventHandler()
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player) {
			AntiCheatAPI.checkReach(event);
		}
	}

	@EventHandler()
	public void onPlayerLogin(PlayerLoginEvent event) {
		Player player = event.getPlayer();
		AntiCheatAPI.addDetect(event);
		if (AntiCheatAPI.checkToolBox(player)) {
			player.close("", "На нашем сервере запрещены читы!");
		}
	}

	@EventHandler()
	public void onPlayerMove(PlayerMoveEvent event) {
		AntiCheatAPI.checkFly(event);
	}
}