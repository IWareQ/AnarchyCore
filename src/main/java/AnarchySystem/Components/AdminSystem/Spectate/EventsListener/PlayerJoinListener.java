package AnarchySystem.Components.AdminSystem.Spectate.EventsListener;

import AnarchySystem.Components.AdminSystem.Spectate.SpectateAPI;
import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

	@EventHandler()
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (SpectateAPI.isSpectate(player.getName())) {
			SpectateAPI.removeSpectate(player);
		}
	}
}