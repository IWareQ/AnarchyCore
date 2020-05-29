package Anarchy.Task;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Sound;
import cn.nukkit.scheduler.Task;

public class RestartTask extends Task {
	@Override
	public void onRun(int i) {
		if (MinuteTask.SECONDS == 60) {
			Server.getInstance().broadcastMessage("§l§eПерезагрузка! §r§fСервер перезагрузится через §a1 §fминуту!");
		} else if (MinuteTask.SECONDS == 30) {
			Server.getInstance().broadcastMessage("§l§eПерезагрузка! §r§fСервер перезагрузится через §a30 §fсекунд!");
		} else if (MinuteTask.SECONDS == 10) {
			Server.getInstance().broadcastMessage("§l§eПерезагрузка! §r§fСервер перезагрузится через §a10 §fсекунд!");
		}

		if (MinuteTask.SECONDS<= 10) {
			if (MinuteTask.SECONDS == 0) {
				for (Player player: Server.getInstance().getOnlinePlayers().values()) {
					player.close("", "§f§lПерезагрузка");
				}
				Server.getInstance().shutdown();
				return;
			}

			for (Player player: Server.getInstance().getOnlinePlayers().values()) {
				player.sendTitle("§lПерезагрузка", "§fСервер перезагрузится через §e" + MinuteTask.SECONDS + " §f" + getSecond(MinuteTask.SECONDS), 0, 20, 0);
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