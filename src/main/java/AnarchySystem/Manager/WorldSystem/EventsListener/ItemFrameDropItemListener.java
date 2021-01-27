package AnarchySystem.Manager.WorldSystem.EventsListener;

import AnarchySystem.Manager.WorldSystem.WorldSystemAPI;
import cn.nukkit.blockentity.BlockEntityItemFrame;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.ItemFrameDropItemEvent;

public class ItemFrameDropItemListener implements Listener {

	@EventHandler()
	public void onItemFrameDropItem(ItemFrameDropItemEvent event) {
		BlockEntityItemFrame itemFrame = event.getItemFrame();
		if (itemFrame.getLevel().equals(WorldSystemAPI.getSpawn())) {
			event.setCancelled(true);
		}
	}
}