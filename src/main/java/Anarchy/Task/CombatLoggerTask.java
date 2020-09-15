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
		CombatLoggerAPI.getPlayers().forEach((player,time)->{
			long nowTime = System.currentTimeMillis() / 1000L;
			long combatTime = time / 1000L + 30 - nowTime;
			if (time / 1000L + 30 - nowTime <= 30) {
				CombatLoggerAPI.updateBossBar(player, "        §l§fВы вошли в §6PvP §fрежим§7!\n\n§l§fНе выходите из игры еще §6" + combatTime + " §fсек§7.!", 100);
			}
			--combatTime;
		});
	}
}