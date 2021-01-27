package AnarchySystem.Manager.WorldSystem.EventsListener;

import AnarchySystem.Manager.WorldSystem.WorldSystemAPI;
import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerDropItemEvent;

public class PlayerDropItemListener implements Listener{
	
	@EventHandler()
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		if (player.getLevel().equals(WorldSystemAPI.getSpawn()) && !player.hasPermission("Development")) {
			event.setCancelled(true);
		}
	}
}