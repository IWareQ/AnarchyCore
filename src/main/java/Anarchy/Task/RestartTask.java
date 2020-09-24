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
		if (MinuteTask.SECONDS_RESTART == 60) {
			Server.getInstance().broadcastMessage("\u00a7l\u00a77(\u00a73\u041f\u0435\u0440\u0435\u0437\u0430\u0433\u0440\u0443\u0437\u043a\u0430\u00a77) \u00a7r\u00a7f\u0421\u0435\u0440\u0432\u0435\u0440 \u043f\u0435\u0440\u0435\u0437\u0430\u0433\u0440\u0443\u0437\u0438\u0442\u0441\u044f \u0447\u0435\u0440\u0435\u0437 \u00a731 \u00a7f\u043c\u0438\u043d\u0443\u0442\u0443!");
		} else if (MinuteTask.SECONDS_RESTART == 30) {
			Server.getInstance().broadcastMessage("\u00a7l\u00a77(\u00a73\u041f\u0435\u0440\u0435\u0437\u0430\u0433\u0440\u0443\u0437\u043a\u0430\u00a77) \u00a7r\u00a7f\u0421\u0435\u0440\u0432\u0435\u0440 \u043f\u0435\u0440\u0435\u0437\u0430\u0433\u0440\u0443\u0437\u0438\u0442\u0441\u044f \u0447\u0435\u0440\u0435\u0437 \u00a7330 \u00a7f\u0441\u0435\u043a\u0443\u043d\u0434!");
		} else if (MinuteTask.SECONDS_RESTART == 10) {
			Server.getInstance().broadcastMessage("\u00a7l\u00a77(\u00a73\u041f\u0435\u0440\u0435\u0437\u0430\u0433\u0440\u0443\u0437\u043a\u0430\u00a77) \u00a7r\u00a7f\u0421\u0435\u0440\u0432\u0435\u0440 \u043f\u0435\u0440\u0435\u0437\u0430\u0433\u0440\u0443\u0437\u0438\u0442\u0441\u044f \u0447\u0435\u0440\u0435\u0437 \u00a7310 \u00a7f\u0441\u0435\u043a\u0443\u043d\u0434!");
		}
		if (MinuteTask.SECONDS_RESTART <= 10) {
			if (MinuteTask.SECONDS_RESTART == 0) {
				for (Player player : Server.getInstance().getOnlinePlayers().values()) {
					CombatLoggerAPI.removeCombat(player);
					SpectateAPI.removeSpectate(player);
					player.close("", "\u00a7l\u00a76\u041f\u0435\u0440\u0435\u0437\u0430\u0433\u0440\u0443\u0437\u043a\u0430");
				}
				Server.getInstance().shutdown();
				return;
			}
			for (Player player : Server.getInstance().getOnlinePlayers().values()) {
				player.sendTitle("\u00a7l\u00a7c\u0412\u041d\u0418\u041c\u0410\u041d\u0418\u0415", "\u00a7f\u00a7l\u0421\u0435\u0440\u0432\u0435\u0440 \u043f\u0435\u0440\u0435\u0437\u0430\u0433\u0440\u0443\u0437\u0438\u0442\u0441\u044f \u0447\u0435\u0440\u0435\u0437 \u00a76" + MinuteTask.SECONDS_RESTART + " \u00a7f" + getSecond(MinuteTask.SECONDS_RESTART), 0, 20, 0);
				player.level.addSound(player, Sound.RANDOM_CLICK, 1, 1, player);
			}
		}
		MinuteTask.SECONDS_RESTART--;
	}
	
	private String getSecond(int second) {
		int preLastDigit = second % 100 / 10;
		if (preLastDigit == 1) {
			return "\u0441\u0435\u043a\u0443\u043d\u0434";
		}
		switch (second % 10) {
			case 1: 
			return "\u0441\u0435\u043a\u0443\u043d\u0434\u0443";
			
			case 2: 
			
			case 3: 
			
			case 4: 
			return "\u0441\u0435\u043a\u0443\u043d\u0434\u044b";
			
			default: 
			return "\u0441\u0435\u043a\u0443\u043d\u0434";
			
		}
	}
}