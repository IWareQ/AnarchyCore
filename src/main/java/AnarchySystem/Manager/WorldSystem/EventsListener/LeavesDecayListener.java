package AnarchySystem.Manager.WorldSystem.EventsListener;

import AnarchySystem.Manager.WorldSystem.WorldSystemAPI;
import cn.nukkit.block.Block;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.LeavesDecayEvent;

public class LeavesDecayListener implements Listener {

	@EventHandler()
	public void onLeavesDecay(LeavesDecayEvent event) {
		Block block = event.getBlock();
		if (block.getLevel().equals(WorldSystemAPI.getSpawn())) {
			event.setCancelled(true);
		}
	}
}