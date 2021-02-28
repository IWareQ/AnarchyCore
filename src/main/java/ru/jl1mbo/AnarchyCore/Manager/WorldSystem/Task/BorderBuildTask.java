package ru.jl1mbo.AnarchyCore.Manager.WorldSystem.Task;

import cn.nukkit.block.Block;
import cn.nukkit.math.Vector3;
import cn.nukkit.scheduler.Task;
import ru.jl1mbo.AnarchyCore.Manager.WorldSystem.WorldSystemAPI;

public class BorderBuildTask extends Task {

	@Override
	public void onRun(int tick) {
		new Thread(() -> {
			for (int x = -2000; x <= 2000; x++) {
				if (WorldSystemAPI.getMap().getBlock(new Vector3(x, 0, -2000)).getId() != Block.BORDER_BLOCK) {
					WorldSystemAPI.getMap().setBlockAt(x, 0, -2000, Block.BORDER_BLOCK);
				} else if (WorldSystemAPI.getMap().getBlock(new Vector3(x, 0, 2000)).getId() != Block.BORDER_BLOCK) {
					WorldSystemAPI.getMap().setBlockAt(x, 0, 2000, Block.BORDER_BLOCK);
				}
			}
			for (int z = -2000; z <= 2000; z++) {
				if (WorldSystemAPI.getMap().getBlock(new Vector3(-2000, 0, z)).getId() != Block.BORDER_BLOCK) {
					WorldSystemAPI.getMap().setBlockAt(-2000, 0, z, Block.BORDER_BLOCK);
				} else if (WorldSystemAPI.getMap().getBlock(new Vector3(2000, 0, z)).getId() != Block.BORDER_BLOCK) {
					WorldSystemAPI.getMap().setBlockAt(2000, 0, z, Block.BORDER_BLOCK);
				}
			}
		}).start();
	}
}