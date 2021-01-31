package ru.jl1mbo.AnarchyCore.GameHandler.Achievements.EventsListener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import ru.jl1mbo.AnarchyCore.GameHandler.Achievements.AchievementsAPI;

public class PlayerJoinListener implements Listener {

	@EventHandler()
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (!AchievementsAPI.isRegister(player.getName())) {
			AchievementsAPI.resetAchievements(player.getName());
		}
	}
}