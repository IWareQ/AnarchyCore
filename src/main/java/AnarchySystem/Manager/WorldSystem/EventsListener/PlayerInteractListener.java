package AnarchySystem.Manager.WorldSystem.EventsListener;

import AnarchySystem.Manager.WorldSystem.WorldSystemAPI;
import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {

	@EventHandler()
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();
		if (player.getLevel().equals(WorldSystemAPI.getMap()) || player.getLevel().equals(WorldSystemAPI.getSpawn())) {
			if (block.getId() == Block.BED_BLOCK) {
				event.setCancelled(true);
			}
		}
	}
}