package Anarchy.Task;

import Anarchy.Module.CombatLogger.CombatLoggerAPI;
import Anarchy.Module.Commands.Spectate.SpectateAPI;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Sound;
import cn.nukkit.scheduler.Task;

public class RestartTask extends Task {
	
	@Override()
	public void onRun(int i) {
		if (MinuteTask.SECONDS == 60) {
			Server.getInstance().broadcastMessage("§l§6| §fСервер перезагрузится через §31 §fминуту!");
		} else if (MinuteTask.SECONDS == 30) {
			Server.getInstance().broadcastMessage("§l§6| §fСервер перезагрузится через §330 §fсекунд!");
		} else if (MinuteTask.SECONDS == 10) {
			Server.getInstance().broadcastMessage("§l§6| §fСервер перезагрузится через §310 §fсекунд!");
		}
		if (MinuteTask.SECONDS <= 10) {
			if (MinuteTask.SECONDS == 0) {
				for (Player player : Server.getInstance().getOnlinePlayers().values()) {
					CombatLoggerAPI.removeCombat(player);
					SpectateAPI.removeSpectate(player);
					player.close("", "§l§6Перезагрузка");
				}
				Server.getInstance().shutdown();
				return;
			}
			for (Player player : Server.getInstance().getOnlinePlayers().values()) {
				player.sendTitle("§l§cВНИМАНИЕ", "§f§lСервер перезагрузится через §6" + MinuteTask.SECONDS + " §f" + getSecond(MinuteTask.SECONDS), 0, 20, 0);
				player.level.addSound(player, Sound.RANDOM_CLICK, 1, 1, player);
			}
		}
		MinuteTask.SECONDS--;
	}
	
	private String getSecond(int second) {
		int preLastDigit = second % 100 / 10;
		if (preLastDigit == 1) {
			return "секунд";
		}
		switch (second % 10) {
			case 1: 
			return "секунду";
			
			case 2: 
			
			case 3: 
			
			case 4: 
			return "секунды";
			
			default: 
			return "секунд";
			
		}
	}
}