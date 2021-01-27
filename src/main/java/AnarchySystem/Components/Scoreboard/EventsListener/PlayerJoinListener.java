package AnarchySystem.Components.Scoreboard.EventsListener;

import AnarchySystem.Components.Scoreboard.ScoreboardTask;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

	@EventHandler()
	public void onPlayerJoin(PlayerJoinEvent event) {
		ScoreboardTask.showScoreboard(event.getPlayer(), true);
	}
}