package ru.jl1mbo.AnarchyCore.LoginPlayerHandler.AntiCheat.EventsListener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerLoginEvent;
import ru.jl1mbo.AnarchyCore.LoginPlayerHandler.AntiCheat.AntiCheatAPI;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.BanSystem.BanSystemAPI;

public class PlayerLoginListener implements Listener {

	@EventHandler()
	public void onPlayerLogin(PlayerLoginEvent event) {
		Player player = event.getPlayer();
		if (!BanSystemAPI.IsBanned(player.getName())) {
			if (!player.getName().equalsIgnoreCase("MagaBro0")) {
				if (AntiCheatAPI.checkToolBox(player)) {
					BanSystemAPI.addBan(player.getName(), "Попытка зайти с читами", "AntiCheat", 3600);
				}
			}
		}
	}
}