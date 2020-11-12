package Anarchy.Task;

import java.text.DecimalFormat;

import Anarchy.Manager.Sessions.PlayerSessionManager;
import Anarchy.Manager.Sessions.Session.PlayerSession;
import Anarchy.Module.Permissions.PermissionsAPI;
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
		PlayerSession playerSession = PlayerSessionManager.getPlayerSession(player);
		Scoreboard scoreboard = ScoreboardAPI.createScoreboard();
		ScoreboardDisplay scoreboardDisplay = scoreboard.addDisplay(DisplaySlot.SIDEBAR, "dumy", "§3DEATH §fMC");
		scoreboardDisplay.addLine("§r§fНик§7: §6" + player.getName(), 0);
		scoreboardDisplay.addLine("§r§fРанг§7: " + PermissionsAPI.GROUPS.get(PermissionsAPI.getGroup(player)), 1);
		//scoreboardDisplay.addLine("§r§fРанг§7: " + PermissionsAPI.GROUPS.get(playerSession.getInteger("Permission")), 1);
		scoreboardDisplay.addLine("", 2);
		scoreboardDisplay.addLine("§r§fБаланс§7: §6" + String.format("%.1f", playerSession.getDouble("Money")), 3);
		scoreboardDisplay.addLine("§r§fПинг§7: §6" + player.getPing(), 4);
		scoreboardDisplay.addLine("§r", 5);
		scoreboardDisplay.addLine("§r§fОнлайн§7: §6" + Server.getInstance().getOnlinePlayers().size(), 6);
		scoreboardDisplay.addLine("§r§fНаигранно§7: §6" + new DecimalFormat("#.#").format((float)(playerSession.getInteger("Gametime") + playerSession.getSessionTime()) / 3600).replace("§7,", "§7.") + " §fч§7.", 7);
		scoreboardDisplay.addLine("§r§fСайт§7: §6death§7-§6mc§7.§6online", 8);
		scoreboard.showFor(player);
		PlayerSessionManager.SCOREBOARDS.put(player.getName(), scoreboard);
	}
}