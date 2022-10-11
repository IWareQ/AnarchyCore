package ru.iwareq.anarchycore.task;

import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.scheduler.Task;
import ru.iwareq.anarchycore.manager.WorldSystem.WorldSystemAPI;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class BossSpawnTask extends Task {

	private static final List<String> BOSSES = Arrays.asList("SpiderBoss", "WitherSkeletonBoss", "HuskBoss", "EvokerBoss");

	private static int minutes = 24;

	private static String getBossName() {
		return BOSSES.get(new Random().nextInt(BOSSES.size()));
	}

	@Override()
	public void onRun(int currentTick) {
		if (minutes != 0) {
			minutes--;
		} else {
			if (Server.getInstance().getOnlinePlayers().size() >= 7) {
				WorldSystemAPI.findRandomPositionAndTp(WorldSystemAPI.Map, pos -> {
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
