package ru.jl1mbo.AnarchyCore.Modules.CombatLogger.Task;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.scheduler.Task;
import ru.jl1mbo.AnarchyCore.Modules.CombatLogger.CombatLoggerAPI;

import java.util.Map.Entry;

public class CombatLoggerTask extends Task {

	@Override()
	public void onRun(int currentTick) {
		for (Entry<String, Long> entry : CombatLoggerAPI.combat.entrySet()) {
			Player player = Server.getInstance().getPlayer(entry.getKey());
			if (entry.getValue() <= System.currentTimeMillis() / 1000L) {
				if (player != null) {
					player.sendPopup("Вы вышли из режима §6PvP");
					player.sendTip("Вы вышли из режима §6PvP§7! §fТеперь Вы можете использовать §6Команды §fи §6Выходить §fиз игры§7!");
					CombatLoggerAPI.removeCombat(player);
				}
			} else {
				long seconds = entry.getValue() - System.currentTimeMillis() / 1000L;
				if (seconds <= 30) {
					if (player != null) {
						CombatLoggerAPI.updateBossBar(player, "        §fВы вошли в §6PvP §fрежим§7!\n\n§fНе выходите из игры еще §6" + seconds + " §fсек§7!", (int) (((seconds + 0.) / 30) * 100));
					}
					--seconds;
				}
			}
		}
	}
}