package AnarchySystem.Components.Commands.WorldBorder.Task;

import AnarchySystem.Manager.WorldSystem.WorldSystemAPI;
import cn.nukkit.block.Block;
import cn.nukkit.math.Vector3;
import cn.nukkit.scheduler.Task;

public class BorderBuildTask extends Task {

	@Override
	public void onRun(int currentTick) {
		Thread thread = new Thread(() -> {
			for (int x = -5; x <= 5; x++) {
				if (WorldSystemAPI.getMap().getBlock(new Vector3(x, 135, -5)).getId() != Block.BORDER_BLOCK) {
					WorldSystemAPI.getMap().setBlockAt(x, 135, -5, Block.BORDER_BLOCK);
				} else if (WorldSystemAPI.getMap().getBlock(new Vector3(x, 135, 5)).getId() != Block.BORDER_BLOCK) {
					WorldSystemAPI.getMap().setBlockAt(x, 135, 5, Block.BORDER_BLOCK);
				}
			}
			for (int z = -5; z <= 5; z++) {
				if (WorldSystemAPI.getMap().getBlock(new Vector3(-5, 135, z)).getId() != Block.BORDER_BLOCK) {
					WorldSystemAPI.getMap().setBlockAt(-5, 135, z, Block.BORDER_BLOCK);
				} else if (WorldSystemAPI.getMap().getBlock(new Vector3(5, 135, z)).getId() != Block.BORDER_BLOCK) {
					WorldSystemAPI.getMap().setBlockAt(5, 135, z, Block.BORDER_BLOCK);
				}
			}
		});
		thread.start();
	}
}