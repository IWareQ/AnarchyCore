package ru.jl1mbo.AnarchyCore.Task;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.scheduler.Task;
import ru.jl1mbo.AnarchyCore.Modules.CombatLogger.CombatLoggerAPI;
import ru.jl1mbo.AnarchyCore.Utils.Utils;

public class RestartTask extends Task {
	public static int seconds = 3600;

	@Override()
	public void onRun(int tick) {
		if (seconds != 0) {
			seconds--;
			if (seconds == 60) {
				Server.getInstance().broadcastMessage("§l§7(§3Перезагрузка§7) §rСервер перезагрузится через §61 §fминуту!");
			} else if (seconds == 10) {
				Server.getInstance().broadcastMessage("§l§7(§3Перезагрузка§7) §rСервер перезагрузится через §610 §fсекунд!");
			}
			if (seconds <= 9) {
				Server.getInstance().getOnlinePlayers().values().forEach(players -> players.sendTitle("§lПерезагрузка",
						"§lСервер перезагрузится через §6" + Utils.getSecond(seconds), 0, 20, 0));
			}
		} else {
			for (Player players : Server.getInstance().getOnlinePlayers().values()) {
				CombatLoggerAPI.removeCombat(players);
				players.close("", "§l§6Перезагрузка");
			}
			Server.getInstance().shutdown();
		}
	}
}