package ru.iwareq.anarchycore.task;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.scheduler.Task;
import nukkitcoders.mobplugin.entities.Boss;
import nukkitcoders.mobplugin.entities.monster.flying.Wither;
import ru.iwareq.anarchycore.manager.WorldSystem.WorldSystemAPI;
import ru.iwareq.anarchycore.util.Utils;

import java.util.Arrays;

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
				String text = "Очистка через §6" + Utils.getSecond(this.seconds) + "§7!";
				if (this.seconds == 0) {
					text = "Успешная очистка";
				}

				for (Player players : Server.getInstance().getOnlinePlayers().values()) {
					players.sendTip(text);
				}
			}
		} else {
			Server.getInstance().getLevels().values().forEach(level -> {
				Arrays.stream(level.getEntities()).filter(entity -> !(entity instanceof Player)
						&& !(entity instanceof Wither)
						&& !(entity.getLevel().equals(WorldSystemAPI.Spawn))
						&& !(entity instanceof Boss)).forEach(Entity::close);

				level.doChunkGarbageCollection();
				level.unloadChunks(true);
			});

			System.gc();
			Server.getInstance().broadcastMessage("§l§7(§3Очистка§7) §rОчистка успешно завершена§7!");
			this.seconds = 800;
		}
	}
}