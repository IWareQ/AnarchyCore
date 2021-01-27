package AnarchySystem.Components.Achievements.EventsListener;

import AnarchySystem.Components.Achievements.AchievementsAPI;
import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

	@EventHandler()
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (!AchievementsAPI.isRegister(player.getName())) {
			AchievementsAPI.resetAchievements(player.getName());
		}
	}
}