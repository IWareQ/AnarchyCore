package AnarchySystem.Components.Scoreboard.EventsListener;

import AnarchySystem.Components.Scoreboard.ScoreboardTask;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

	@EventHandler()
	public void onPlayerQuit(PlayerQuitEvent event) {
		ScoreboardTask.showScoreboard(event.getPlayer(), false);
	}
}