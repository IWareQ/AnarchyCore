package Anarchy.Task;

import Anarchy.Module.CombatLogger.CombatLoggerAPI;
import cn.nukkit.scheduler.Task;

public class CombatLoggerTask extends Task {
	
	@Override()
	public void onRun(int i) {
		CombatLoggerAPI.getPlayers().forEach((player,time)->{
			if (time + 30 * 1000 < System.currentTimeMillis()) {
				if (player.isOnline()) {
					CombatLoggerAPI.removeBossBar(player);
					player.sendPopup("  §l§fВы вышли из режима §6PvP§7!");
					player.sendTip("§l§fВы вышли из режима §6PvP§7! §fТеперь Вы можете использовать §6Команды §fи §6Выходить §fиз игры§7!");
				}
				CombatLoggerAPI.removeCombat(player);
			}
		});
	}
}