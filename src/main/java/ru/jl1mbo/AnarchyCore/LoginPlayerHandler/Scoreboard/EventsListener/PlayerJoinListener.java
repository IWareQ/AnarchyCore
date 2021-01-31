package ru.jl1mbo.AnarchyCore.LoginPlayerHandler.Scoreboard.EventsListener;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import ru.jl1mbo.AnarchyCore.LoginPlayerHandler.Scoreboard.ScoreboardTask;

public class PlayerJoinListener implements Listener {

	@EventHandler()
	public void onPlayerJoin(PlayerJoinEvent event) {
		ScoreboardTask.showScoreboard(event.getPlayer(), true);
	}
}