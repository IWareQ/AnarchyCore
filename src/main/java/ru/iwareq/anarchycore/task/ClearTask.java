package ru.iwareq.anarchycore.task;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.level.Level;
import cn.nukkit.scheduler.Task;
import cn.nukkit.utils.ThreadCache;
import nukkitcoders.mobplugin.entities.monster.flying.Wither;
import ru.iwareq.anarchycore.entity.Bosses.EvokerBoss;
import ru.iwareq.anarchycore.entity.Bosses.HuskBoss;
import ru.iwareq.anarchycore.entity.Bosses.SpiderBoss;
import ru.iwareq.anarchycore.entity.Bosses.WitherSkeletonBoss;
import ru.iwareq.anarchycore.manager.WorldSystem.WorldSystemAPI;
import ru.iwareq.anarchycore.util.Utils;

public class ClearTask extends Task {

	private int seconds = 800;

	@Override()
	public void onRun(int currentTick) {
		if (this.seconds != 0) {
			this.seconds--;
			if (this.seconds == 60) {
				Server.getInstance().broadcastMessage("§l§7(§3Очистка§7) §rОчистка произойдет через §61 §fминуту§7!");
			}
			if (this.seconds <= 10) {
				Server.getInstance().getOnlinePlayers().values().forEach(players -> players.sendTip("Очистка через §6" + Utils.getSecond(this.seconds) + "§7!"));
			}
		} else {
			for (Level level : Server.getInstance().getLevels().values()) {
				for (Entity entity : level.getEntities()) {
					if (!(entity instanceof Player) && !(entity instanceof Wither) && !(entity.getLevel().equals(WorldSystemAPI.Spawn)) && !(entity instanceof WitherSkeletonBoss) && !(entity instanceof HuskBoss) && !(entity instanceof EvokerBoss) && !(entity instanceof SpiderBoss)) {
						entity.close();
					}
				}
				level.doChunkGarbageCollection();
				level.unloadChunks(true);
			}

			System.gc();
			Server.getInstance().broadcastMessage("§l§7(§3Очистка§7) §rОчистка успешно завершена§7!");
			this.seconds = 800;
		}
	}
}