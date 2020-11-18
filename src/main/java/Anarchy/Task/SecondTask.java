package Anarchy.Task;

import Anarchy.AnarchyMain;
import Anarchy.Module.CombatLogger.CombatLoggerAPI;
import Anarchy.Module.Commands.Spectate.SpectateAPI;
import Anarchy.Module.Commands.Spectate.Utils.SpectatePlayer;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.math.Vector3;
import cn.nukkit.scheduler.Task;

public class SecondTask extends Task {

	@Override()
	public void onRun(int currentTick) {
		CombatLoggerAPI.getPlayers().forEach((player, time)-> {
			if (time < System.currentTimeMillis() / 1000) {
				if (player != null) {
					player.sendPopup("  §l§fВы вышли из режима §6PvP§7!");
					player.sendTip("§l§fВы вышли из режима §6PvP§7! §fТеперь Вы можете использовать §6Команды §fи §6Выходить §fиз игры§7!");
				}
				CombatLoggerAPI.removeCombat(player);
			}
		});
		CombatLoggerAPI.getPlayers().forEach((player, time)-> {
			long nowTime = System.currentTimeMillis() / 1000;
			long combatTime = time - nowTime;
			if (combatTime <= 30) {
				if (player != null) {
					CombatLoggerAPI.updateBossBar(player, "        §l§fВы вошли в §6PvP §fрежим§7!\n\n§l§fНе выходите из игры еще §6" + combatTime + " §fсек§7.!", 100);
				}
			}
			--combatTime;
		});
		for (Player player : Server.getInstance().getOnlinePlayers().values()) {
			if (SpectateAPI.SPECTATE_PLAYERS.containsKey(player.getName())) {
				SpectatePlayer spectatePlayer = SpectateAPI.SPECTATE_PLAYERS.get(player.getName());
				Player target = Server.getInstance().getPlayer(spectatePlayer.getSpectateName());
				if ((int)player.distance(target) >= 21) {
					player.sendMessage(AnarchyMain.PREFIX + "§fВы не можете отлететь дальше §620 §fблоков от Наблюдаемого§7!\n§l§6• §r§fДля окончания наблюдения возьмите в руку §6Редстоун§7!");
					player.teleport(new Vector3(target.getFloorX(), target.getFloorY(), target.getFloorZ()));
				}
			}
		}
	}
}