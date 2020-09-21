package Anarchy.Task;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.level.Level;
import cn.nukkit.level.Sound;
import cn.nukkit.scheduler.Task;

public class ClearTask extends Task {
	
	@Override()
	public void onRun(int i) {
		if (MinuteTask.TIMER_CLEAR == 5) {
			Server.getInstance().broadcastMessage("§l§7(§3Очистка§7) §r§fОчистка произойдет через §65 §fминут§7!");
		} else if (MinuteTask.SECONDS_CLEAR == 60) {
			Server.getInstance().broadcastMessage("§l§7(§3Очистка§7) §r§fОчистка произойдет через §61 §fминуту§7!");
		} else if (MinuteTask.SECONDS_CLEAR == 10) {
			Server.getInstance().broadcastMessage("§l§7(§3Очистка§7) §r§fОчистка произойдет через §610 §fсекунд§7!");
		}
		if (MinuteTask.SECONDS_CLEAR <= 10) {
			if (MinuteTask.SECONDS_CLEAR == 0) {
				for (Level level : Server.getInstance().getLevels().values()) {
					for (Entity entity : level.getEntities()) {
						if (!(entity instanceof Player)) {
							entity.close();
						}
					}
				}
				Server.getInstance().broadcastMessage("§l§7(§3Очистка§7) §r§fОчистка успешно завершена§7!");
				for (Player player : Server.getInstance().getOnlinePlayers().values()) {
					player.sendTip("§l§fОчистка успешно завершена§7!");
				}
				Server.getInstance().getScheduler().cancelTask(Server.getInstance().getScheduler().scheduleRepeatingTask(new ClearTask(), 20).getTaskId());
				MinuteTask.TIMER_CLEAR = 10;
				MinuteTask.SECONDS_CLEAR = 60;
			}
			for (Player player : Server.getInstance().getOnlinePlayers().values()) {
				player.sendTip("§f§lОчистка через §6" + MinuteTask.SECONDS_CLEAR + " §fсек§7!");
				player.level.addSound(player, Sound.RANDOM_CLICK, 1, 1, player);
			}
		}
		MinuteTask.SECONDS_CLEAR--;
	}
}