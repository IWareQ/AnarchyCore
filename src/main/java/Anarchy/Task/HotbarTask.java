package Anarchy.Task;

import java.text.DecimalFormat;

import Anarchy.Manager.Sessions.PlayerSessionManager;
import Anarchy.Manager.Sessions.Session.PlayerSession;
import Anarchy.Module.Permissions.PermissionsAPI;
import Anarchy.Utils.StringUtils;
import ScoreboardPlugin.API.ScoreboardAPI;
import ScoreboardPlugin.Network.DisplaySlot;
import ScoreboardPlugin.Network.Scoreboard;
import ScoreboardPlugin.Network.ScoreboardDisplay;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.scheduler.Task;

public class HotbarTask extends Task {

	@Override()
	public void onRun(int i) {
		for (Player player : Server.getInstance().getOnlinePlayers().values()) {
			if (PlayerSessionManager.SCOREBOARD.contains(player.getName())) {
				PlayerSessionManager.SCOREBOARDS.get(player.getName()).hideFor(player);
				showScoreboard(player);
			}
		}
	}

	public static void showScoreboard(Player player) {
		String playerName = player.getName();
		PlayerSession playerSession = PlayerSessionManager.getPlayerSession(player);
		Scoreboard scoreboard = ScoreboardAPI.createScoreboard();
		ScoreboardDisplay scoreboardDisplay = scoreboard.addDisplay(DisplaySlot.SIDEBAR, "dumy", "§3DEATH §fMC");
		scoreboardDisplay.addLine("§l§f" + StringUtils.getDate(), 0);
		scoreboardDisplay.addLine(" ", 1);
		scoreboardDisplay.addLine("§fНик§7: §6" + playerName, 2);
		scoreboardDisplay.addLine("§fСтатус§7: " + PermissionsAPI.GROUPS.get(playerSession.getInteger("Permission")), 3);
		scoreboardDisplay.addLine(" ", 4);
		scoreboardDisplay.addLine("§fБаланс§7: §6" + String.format("%.1f", playerSession.getDouble("Money")), 5);
		scoreboardDisplay.addLine("§fОнлайн§7: §6" + Server.getInstance().getOnlinePlayers().size(), 6);
		scoreboardDisplay.addLine("§fВ Игре§7: §6" + playerSession.getSessionTime() / 60 + " §fмин§7.", 7);
		scoreboardDisplay.addLine("§fВсего§7: §6" + new DecimalFormat("#.#").format((float)(playerSession.getInteger("Gametime") + playerSession.getSessionTime()) / 3600).replace("§7,", "§7.") + " §fч§7.", 8);
		scoreboard.showFor(player);
		PlayerSessionManager.SCOREBOARDS.put(player.getName(), scoreboard);
	}
}