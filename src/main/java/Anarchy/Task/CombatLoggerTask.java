package Anarchy.Task;

import Anarchy.Module.CombatLogger.CombatLoggerAPI;
import cn.nukkit.scheduler.Task;

public class CombatLoggerTask extends Task {
	
	@Override()
	public void onRun(int i) {
			CombatLoggerAPI.getPlayers().forEach((player, time)->{
			if (time + 10 * 1000 < System.currentTimeMillis()) {
				if (player.isOnline()) {
					//CombatLoggerAPI.removeBossBar(player);
					player.sendPopup("  §l§fВы вышли из §3ᏢᏙᏢ §fрежима§7!");
					player.sendTip("§l§fВы вышли из §3ᏢᏙᏢ §fрежима§7. §fТеперь Вы можете использовать §3Команды §fи §3Выходить §fиз игры§7!");
				}
				CombatLoggerAPI.removeCombat(player);
			}
		});
	}
}