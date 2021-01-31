package ru.jl1mbo.AnarchyCore.Manager.WorldSystem.EventsListener;

import cn.nukkit.blockentity.BlockEntityItemFrame;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.ItemFrameDropItemEvent;
import ru.jl1mbo.AnarchyCore.Manager.WorldSystem.WorldSystemAPI;

public class ItemFrameDropItemListener implements Listener {

	@EventHandler()
	public void onItemFrameDropItem(ItemFrameDropItemEvent event) {
		BlockEntityItemFrame itemFrame = event.getItemFrame();
		if (itemFrame.getLevel().equals(WorldSystemAPI.getSpawn())) {
			event.setCancelled(true);
		}
	}
}