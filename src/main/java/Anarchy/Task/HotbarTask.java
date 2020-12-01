package Anarchy.Task;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import EconomyAPI.Economy.EconomyAPI;
import PermissionsAPI.Permissions.PermissionsAPI;
import SQLiteAPI.Utils.SQLiteUtils;
import ScoreboardPlugin.API.ScoreboardAPI;
import ScoreboardPlugin.Network.DisplaySlot;
import ScoreboardPlugin.Network.Scoreboard;
import ScoreboardPlugin.Network.ScoreboardDisplay;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.scheduler.Task;

public class HotbarTask extends Task {
	public static Map<String, Scoreboard> SCOREBOARDS = new HashMap<>();
	public static ArrayList<String> SCOREBOARD = new ArrayList<>();

	@Override()
	public void onRun(int currentTick) {
		for (Player player : Server.getInstance().getOnlinePlayers().values()) {
			if (SCOREBOARD.contains(player.getName())) {
				SCOREBOARDS.get(player.getName()).hideFor(player);
				showScoreboard(player);
			}
		}
	}

	public static void showScoreboard(Player player) {
		Scoreboard scoreboard = ScoreboardAPI.createScoreboard();
		Integer gameTime = SQLiteUtils.selectInteger("Users.db", "SELECT `Gametime` FROM USERS WHERE UPPER(`Username`) = '" + player.getName().toUpperCase() + "'");
		ScoreboardDisplay scoreboardDisplay = scoreboard.addDisplay(DisplaySlot.SIDEBAR, "dumy", "§3DEATH §fMC");
		scoreboardDisplay.addLine("§r§fНик§7: §6" + player.getName(), 0);
		scoreboardDisplay.addLine("§r§fРанг§7: " + PermissionsAPI.GROUPS.get(PermissionsAPI.getGroup(player.getName())), 1);
		scoreboardDisplay.addLine("", 2);
		scoreboardDisplay.addLine("§r§fБаланс§7: §6" + String.format("%.1f", EconomyAPI.myMoney(player.getName())), 3);
		scoreboardDisplay.addLine("§r§fПинг§7: §6" + player.getPing(), 4);
		scoreboardDisplay.addLine("§r", 5);
		scoreboardDisplay.addLine("§r§fОнлайн§7: §6" + Server.getInstance().getOnlinePlayers().size(), 6);
		scoreboardDisplay.addLine("§r§fНаигранно§7: §6" + new DecimalFormat("#.#").format((float)gameTime / 3600).replace("§7,", "§7.") + " §fч§7.", 7);
		scoreboardDisplay.addLine("§r§fСайт§7: §6death§7-§6mc§7.§6online", 8);
		scoreboard.showFor(player);
		SCOREBOARDS.put(player.getName(), scoreboard);
	}
}