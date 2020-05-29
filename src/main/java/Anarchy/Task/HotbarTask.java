package Anarchy.Task;

import Anarchy.Manager.Sessions.PlayerSessionManager;
import Anarchy.Manager.Sessions.Session.PlayerSession;
import Anarchy.Module.Permissions.PermissionsAPI;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.scheduler.Task;
import de.theamychan.scoreboard.api.ScoreboardAPI;
import de.theamychan.scoreboard.network.DisplaySlot;
import de.theamychan.scoreboard.network.Scoreboard;
import de.theamychan.scoreboard.network.ScoreboardDisplay;

import java.text.DecimalFormat;

public class HotbarTask extends Task {

	@Override
	public void onRun(int i) {
		for (Player player: Server.getInstance().getOnlinePlayers().values()) {
			if (PlayerSessionManager.SCOREBOARD.contains(player.getName())) {
				PlayerSessionManager.SCOREBOARDS.get(player.getName()).hideFor(player);
				showScoreboard(player);
			}
		}
	}

	public static void showScoreboard(Player player) {
		PlayerSession playerSession = PlayerSessionManager.getPlayerSession(player);
		Scoreboard scoreboard = ScoreboardAPI.createScoreboard();
		ScoreboardDisplay scoreboardDisplay = scoreboard.addDisplay(DisplaySlot.SIDEBAR, "dumy", "§l§6Hall§fMine");
		scoreboardDisplay.addLine(" ", 0);
		scoreboardDisplay.addLine("§l§fНик §7- §a" + player.getName(), 1);
		scoreboardDisplay.addLine("§l§fГруппа §7- §f" + PermissionsAPI.GROUPS.get(playerSession.getInteger("Permission")), 2);
		scoreboardDisplay.addLine("§l§fМонет §7- §e" + playerSession.getInteger("Money") + "", 2);
		scoreboardDisplay.addLine("§l§fОнлайн §7- §3" + Server.getInstance().getOnlinePlayers().size(), 3);
		scoreboardDisplay.addLine("§l§fВ Игре §7- §6" + playerSession.getSessionTime() / 60 + " §fмин.   ", 4);
		scoreboardDisplay.addLine("§l§fВсего §7- §c" + new DecimalFormat("#.#").format((float)(playerSession.getInteger("Gametime") + playerSession.getSessionTime()) / 3600).replace("§f,", "§f.") + " §fч.", 5);
		scoreboard.showFor(player);
		PlayerSessionManager.SCOREBOARDS.put(player.getName(), scoreboard);
	}
}