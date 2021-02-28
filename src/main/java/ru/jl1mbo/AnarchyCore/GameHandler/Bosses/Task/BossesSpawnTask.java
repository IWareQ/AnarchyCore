package ru.jl1mbo.AnarchyCore.GameHandler.Bosses.Task;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.level.Position;
import cn.nukkit.scheduler.Task;
import ru.jl1mbo.AnarchyCore.Manager.WorldSystem.WorldSystemAPI;

public class BossesSpawnTask extends Task {
	private static int minutes = 24;

	@Override()
	public void onRun(int tick) {
		if (minutes != 0) {
			minutes--;
		} else {
			if (Server.getInstance().getOnlinePlayers().size() >= 5) {
				Entity entity = Entity.createEntity(getBossName(), new Position(0, 500, 0));
				if (entity != null) {
					entity.spawnToAll();
					WorldSystemAPI.randomPosition(entity);
					Server.getInstance().broadcastMessage("§l§7(§3Боссы§7) §r§fНа карте появился Босс§7!\n§l§6• §r§fКоординаты§7: §6" + entity.getFloorX() + "§7, §6 " +
														  entity.getFloorY() + "§7, §6" + entity.getFloorZ());
				}
			}
			minutes = 24;
		}
	}

	private static String getBossName() {
		List<String> bossList = Arrays.asList("SpiderBoss", "WitherSkeletonBoss", "HuskBoss", "EvokerBoss");
		return bossList.get(new Random().nextInt(bossList.size()));
	}
}