package AnarchySystem.Components.Scoreboard;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import AnarchySystem.Components.Authorization.AuthorizationAPI;
import AnarchySystem.Components.EconomyAPI.EconomyAPI;
import AnarchySystem.Components.Permissions.PermissionAPI;
import AnarchySystem.Manager.Scoreboard.ScoreboardAPI;
import AnarchySystem.Manager.Scoreboard.Network.DisplaySlot;
import AnarchySystem.Manager.Scoreboard.Network.Scoreboard;
import AnarchySystem.Manager.Scoreboard.Network.ScoreboardDisplay;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.scheduler.Task;

public class ScoreboardTask extends Task {
	private static Map<Player, Scoreboard> SCOREBOARDS = new HashMap<>();

	public static void showScoreboard(Player player, boolean hide) {
		if (hide) {
			Scoreboard scoreboard = ScoreboardAPI.createScoreboard();
			ScoreboardDisplay scoreboardDisplay = scoreboard.addDisplay(DisplaySlot.SIDEBAR, "dumy", "§3DEATH §fMC");
			scoreboardDisplay.addLine(PermissionAPI.GROUPS.get(PermissionAPI.getGroup(player.getName())) + player.getName(), 0);
			scoreboardDisplay.addLine("§1", 1);
			scoreboardDisplay.addLine("§r§fБаланс§7: §6" + String.format("%.1f", EconomyAPI.myMoney(player.getName())), 2);
			scoreboardDisplay.addLine("§r§fОнлайн§7: §6" + Server.getInstance().getOnlinePlayers().size(), 3);
			scoreboardDisplay.addLine("§4", 4);
			scoreboardDisplay.addLine("§r§fНаигранно§7: §6" + new DecimalFormat("#.#").format((float) AuthorizationAPI.getGameTime(player.getName()) / 3600).replace("§7,", "§7.") + " §fч§7.", 5);
			scoreboardDisplay.addLine("§r§fСайт§7: §6death§7-§6mc§7.§6online", 6);
			scoreboard.showFor(player);
			SCOREBOARDS.put(player, scoreboard);
		} else {
			ScoreboardTask.SCOREBOARDS.remove(player);
		}
	}

	@Override()
	public void onRun(int currentTick) {
		for (Player player : Server.getInstance().getOnlinePlayers().values()) {
			if (player != null) {
				if (player.spawned) {
					SCOREBOARDS.get(player).hideFor(player);
					showScoreboard(player, true);
				}
			}
		}
	}
}