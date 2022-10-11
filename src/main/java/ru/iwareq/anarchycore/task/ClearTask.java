package ru.iwareq.anarchycore.task;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.scheduler.Task;
import lombok.Getter;
import lombok.Setter;
import nukkitcoders.mobplugin.entities.Boss;
import nukkitcoders.mobplugin.entities.monster.flying.Wither;
import ru.iwareq.anarchycore.manager.WorldSystem.WorldSystemAPI;
import ru.iwareq.anarchycore.util.Utils;

import java.util.Arrays;

public class ClearTask extends Task {

	private static final int START_SECONDS = 60 * 60;

	@Getter
	@Setter
	private int seconds = START_SECONDS;

	@Override()
	public void onRun(int currentTick) {
		this.seconds--;
		if (this.seconds <= 0) {
			Server.getInstance().getLevels().values().forEach(level -> {
				Arrays.stream(level.getEntities()).filter(entity -> !(entity instanceof Player) &&
						!(entity instanceof Wither) &&
						!(entity.getLevel().equals(WorldSystemAPI.Spawn)) &&
						!(entity instanceof Boss)).forEach(Entity::close);

				level.doChunkGarbageCollection();
				level.unloadChunks(true);
			});


			System.gc();

			for (Player player : Server.getInstance().getOnlinePlayers().values()) {
				player.sendMessage("§l§7(§3Очистка§7) §rОчистка завершена§7!");
				player.sendTip("Очистка завершена!");
			}

			this.seconds = START_SECONDS;
			return;
		}

		if (this.seconds == 5 * 60) {
			Server.getInstance().broadcastMessage("§l§7(§3Очистка§7) §rОчистка произойдет через §65 §fминут§7!");
		}

		if (this.seconds <= 10) {
			for (Player players : Server.getInstance().getOnlinePlayers().values()) {
				players.sendTip("До очистки дропа и мобов осталось: §6" + Utils.getSecond(this.seconds) + "§7!");
			}
		}
	}
}
