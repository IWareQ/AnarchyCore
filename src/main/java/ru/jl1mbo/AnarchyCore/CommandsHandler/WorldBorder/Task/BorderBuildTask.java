package ru.jl1mbo.AnarchyCore.CommandsHandler.WorldBorder.Task;

import cn.nukkit.block.Block;
import cn.nukkit.math.Vector3;
import cn.nukkit.scheduler.Task;
import ru.jl1mbo.AnarchyCore.Manager.WorldSystem.WorldSystemAPI;

public class BorderBuildTask extends Task {

	@Override
	public void onRun(int tick) {
		Thread thread = new Thread(() -> {
			for (int x = -2000; x <= 2000; x++) {
				if (!WorldSystemAPI.getMap().getChunk(WorldSystemAPI.getMap().getBlock(new Vector3(x, 1, -2000)).getChunkX(), WorldSystemAPI.getMap().getBlock(new Vector3(x, 1, -2000)).getChunkZ()).isGenerated()
						&& !WorldSystemAPI.getMap().getChunk(WorldSystemAPI.getMap().getBlock(new Vector3(x, 1, -2000)).getChunkX(), WorldSystemAPI.getMap().getBlock(new Vector3(x, 1, -2000)).getChunkZ()).isLoaded()) {
					WorldSystemAPI.getMap().getChunk(WorldSystemAPI.getMap().getBlock(new Vector3(x, 1, -2000)).getChunkX(), WorldSystemAPI.getMap().getBlock(new Vector3(x, 1, -2000)).getChunkZ()).setGenerated(true);
				}
				if (WorldSystemAPI.getMap().getBlock(new Vector3(x, 1, -2000)).getId() != Block.BORDER_BLOCK) {
					WorldSystemAPI.getMap().setBlockAt(x, 1, -2000, Block.BORDER_BLOCK);
				} else if (WorldSystemAPI.getMap().getBlock(new Vector3(x, 1, 2000)).getId() != Block.BORDER_BLOCK) {
					WorldSystemAPI.getMap().setBlockAt(x, 1, 2000, Block.BORDER_BLOCK);
				}
			}
			for (int z = -2000; z <= 2000; z++) {
				if (WorldSystemAPI.getMap().getBlock(new Vector3(-2000, 1, z)).getId() != Block.BORDER_BLOCK) {
					WorldSystemAPI.getMap().setBlockAt(-2000, 1, z, Block.BORDER_BLOCK);
				} else if (WorldSystemAPI.getMap().getBlock(new Vector3(2000, 1, z)).getId() != Block.BORDER_BLOCK) {
					WorldSystemAPI.getMap().setBlockAt(2000, 1, z, Block.BORDER_BLOCK);
				}
			}
		});
		thread.start();
	}
}