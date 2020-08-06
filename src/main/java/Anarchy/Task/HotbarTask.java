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
		scoreboardDisplay.addLine("§r§f15§7/§f06§7/§f2020", 0);
		scoreboardDisplay.addLine(" ", 1);
		scoreboardDisplay.addLine("§fНик §7- §3" + player.getName(), 2);
		scoreboardDisplay.addLine("§fСтатус §7- " + PermissionsAPI.GROUPS.get(playerSession.getInteger("Permission")), 3);
		scoreboardDisplay.addLine(" ", 4);
		scoreboardDisplay.addLine("§fБаланс §7- §3" + playerSession.getInteger("Money") + "", 5);
		scoreboardDisplay.addLine("§fОнлайн §7- §3" + Server.getInstance().getOnlinePlayers().size(), 6);
		scoreboardDisplay.addLine("§fВ Игре §7- §3" + playerSession.getSessionTime() / 60 + " §fмин§7.", 7);
		scoreboardDisplay.addLine("§fВсего §7- §3" + new DecimalFormat("#.#").format((float)(playerSession.getInteger("Gametime") + playerSession.getSessionTime()) / 3600).replace("§7,", "§7.") + " §fч§7.", 8);
		scoreboard.showFor(player);
		PlayerSessionManager.SCOREBOARDS.put(player.getName(), scoreboard);
	}
}