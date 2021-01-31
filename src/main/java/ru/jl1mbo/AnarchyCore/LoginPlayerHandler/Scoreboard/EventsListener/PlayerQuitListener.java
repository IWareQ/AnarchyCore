package ru.jl1mbo.AnarchyCore.LoginPlayerHandler.Scoreboard.EventsListener;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerQuitEvent;
import ru.jl1mbo.AnarchyCore.LoginPlayerHandler.Scoreboard.ScoreboardTask;

public class PlayerQuitListener implements Listener {

	@EventHandler()
	public void onPlayerQuit(PlayerQuitEvent event) {
		ScoreboardTask.showScoreboard(event.getPlayer(), false);
	}
}