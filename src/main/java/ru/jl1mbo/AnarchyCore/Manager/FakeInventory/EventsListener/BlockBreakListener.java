package ru.jl1mbo.AnarchyCore.Manager.FakeInventory.EventsListener;

import java.util.List;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.math.BlockVector3;
import lombok.RequiredArgsConstructor;
import ru.jl1mbo.AnarchyCore.Manager.FakeInventory.Utils.FakeChests;

@RequiredArgsConstructor()
public class BlockBreakListener implements Listener {
	private static final FakeChests fakeChests = new FakeChests();

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();
		List<BlockVector3> positions = fakeChests.getFakeInventoryPositions(player);
		if (block != null && positions != null) {
			for (BlockVector3 pos : positions) {
				if (pos.getX() == block.getX() && pos.getY() == block.getZ() && pos.getZ() == block.getZ()) {
					event.setCancelled(true);
				}
			}
		}
	}
}