package ru.iwareq.anarchycore.task;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.scheduler.Task;
import ru.iwareq.anarchycore.module.CombatLogger.CombatLoggerAPI;
import ru.iwareq.anarchycore.util.Utils;

public class RestartTask extends Task {

	private int seconds = 3 * 60 * 60;

	@Override()
	public void onRun(int currentTick) {
		if (this.seconds != 0) {
			this.seconds--;
			if (this.seconds == 60) {
				Server.getInstance().broadcastMessage("§l§7(§3Перезагрузка§7) §rСервер перезагрузится через §61 §fминуту!");
			} else if (this.seconds == 10) {
				Server.getInstance().broadcastMessage("§l§7(§3Перезагрузка§7) §rСервер перезагрузится через §610 §fсекунд!");
			}

			if (this.seconds <= 9) {
				for (Player player : Server.getInstance().getOnlinePlayers().values()) {
					player.sendTitle("§lПерезагрузка", "§lСервер перезагрузится через §6" + Utils.getSecond(this.seconds), 0, 20, 0);
				}
			}
		} else {
			for (Player player : Server.getInstance().getOnlinePlayers().values()) {
				CombatLoggerAPI.removeCombat(player);
				player.close(player.getLeaveMessage(), "§l§6Перезагрузка");
			}

			Server.getInstance().shutdown();
		}
	}
}