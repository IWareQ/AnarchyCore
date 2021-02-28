package ru.jl1mbo.AnarchyCore.SystemProcessorHandler.BanSystem.EventsListener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.BanSystem.BanSystemAPI;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.BanSystem.Utils.BanUtils;

public class PlayerJoinListener implements Listener {

	@EventHandler()
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		BanUtils banUtils = BanSystemAPI.getBanUtils(player.getName());
		if (BanSystemAPI.IsBanned(player.getName())) {
			if (!banUtils.isTimeValid()) {
				BanSystemAPI.removeBan(player.getName());
			} else {
				player.setImmobile(true);
			}
		}
	}
}