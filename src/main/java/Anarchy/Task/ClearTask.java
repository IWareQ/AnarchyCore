package Anarchy.Task;

import Anarchy.Manager.Functions.FunctionsAPI;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.item.EntityMinecartChest;
import cn.nukkit.level.Level;
import cn.nukkit.level.Sound;
import cn.nukkit.scheduler.Task;

public class ClearTask extends Task {
	public static int TIMER_CLEAR = 600;
	
	@Override()
	public void onRun(int i) {
		if (TIMER_CLEAR == 300) {
			Server.getInstance().broadcastMessage("§l§7(§3Очистка§7) §r§fОчистка произойдет через §65 §fминут§7!");
		} else if (MinuteTask.TIMER_CLEAR == 60) {
			Server.getInstance().broadcastMessage("§l§7(§3Очистка§7) §r§fОчистка произойдет через §61 §fминуту§7!");
		}
		if (TIMER_CLEAR <= 10) {
			if (TIMER_CLEAR == 0) {
				for (Level level : Server.getInstance().getLevels().values()) {
					for (Entity entity : level.getEntities()) {
						if (!(entity instanceof Player) && !(entity.getLevel().equals(FunctionsAPI.SPAWN)) && !(entity instanceof EntityMinecartChest)) {
							entity.close();
						}
					}
				}
				Server.getInstance().broadcastMessage("§l§7(§3Очистка§7) §r§fОчистка успешно завершена§7!");
				TIMER_CLEAR = 600;
				//MinuteTask.SECONDS_CLEAR = 60;
			}
			for (Player player : Server.getInstance().getOnlinePlayers().values()) {
				player.sendTip("§f§lОчистка через §6" + TIMER_CLEAR + " §fсек§7!");
				player.level.addSound(player, Sound.RANDOM_CLICK, 1, 1, player);
			}
		}
		TIMER_CLEAR--;
	}
}