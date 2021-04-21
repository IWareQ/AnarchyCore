package ru.jl1mbo.AnarchyCore.Task;

import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.scheduler.Task;
import ru.jl1mbo.AnarchyCore.Manager.WorldSystem.WorldSystemAPI;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class BossSpawnTask extends Task {

	private static int minutes = 24;

	private static String getBossName() {
		List<String> bossList = Arrays.asList("SpiderBoss", "WitherSkeletonBoss", "HuskBoss", "EvokerBoss");
		return bossList.get(new Random().nextInt(bossList.size()));
	}

	@Override()
	public void onRun(int currentTick) {
		if (minutes != 0) {
			minutes--;
		} else {
			if (Server.getInstance().getOnlinePlayers().size() >= 7) {
				WorldSystemAPI.randomPosition(WorldSystemAPI.Map, pos -> {
					Entity entity = Entity.createEntity(getBossName(), pos);
					if (entity != null) {
						entity.spawnToAll();
						Server.getInstance().broadcastMessage("§l§7(§3Боссы§7) §rНа карте появился Босс§7!\n§l§6• §rКоординаты§7: §6" + entity.getFloorX() + "§7, §6 " + entity.getFloorY() + "§7, §6" + entity.getFloorZ());
					}
				});
				minutes = 24;
			}
		}
	}
}