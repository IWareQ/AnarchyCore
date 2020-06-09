package Anarchy.Manager.FakeChests;

import java.util.List;

import Anarchy.Manager.FakeChests.Utils.FakeChests;
import cn.nukkit.block.Block;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.math.BlockVector3;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FakeChestsEventListener implements Listener {
	private final FakeChests fakeChests = new FakeChests();

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBreak(BlockBreakEvent event) {
		Block block = event.getBlock();
		List<BlockVector3> positions = fakeChests.getFakeInventoryPositions(event.getPlayer());
		if (block != null && positions != null) {
			for (BlockVector3 pos: positions) {
				if (pos.x == block.x && pos.y == block.y && pos.z == block.z) {
					event.setCancelled();
					return;
				}
			}
		}
	}
}