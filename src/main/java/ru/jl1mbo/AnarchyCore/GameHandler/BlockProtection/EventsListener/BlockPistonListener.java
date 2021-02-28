package ru.jl1mbo.AnarchyCore.GameHandler.BlockProtection.EventsListener;

import cn.nukkit.block.Block;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockPistonEvent;
import ru.jl1mbo.AnarchyCore.GameHandler.BlockProtection.BlockProtectionAPI;

public class BlockPistonListener implements Listener {

	@EventHandler()
	public void onBlockPiston(BlockPistonEvent event) {
		Block block = event.getBlock();
		if (BlockProtectionAPI.getAllBlocks().get(block.getId()) != null || block.getId() == 52) {
			event.setCancelled(true);
		}
	}
}