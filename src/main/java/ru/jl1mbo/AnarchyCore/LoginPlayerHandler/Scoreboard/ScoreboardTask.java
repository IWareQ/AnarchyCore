package ru.jl1mbo.AnarchyCore.LoginPlayerHandler.Scoreboard;

import java.util.HashMap;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.plugin.PluginManager;
import cn.nukkit.scheduler.Task;
import ru.jl1mbo.AnarchyCore.Main;
import ru.jl1mbo.AnarchyCore.LoginPlayerHandler.Authorization.AuthorizationAPI;
import ru.jl1mbo.AnarchyCore.LoginPlayerHandler.Scoreboard.EventsListener.PlayerJoinListener;
import ru.jl1mbo.AnarchyCore.LoginPlayerHandler.Scoreboard.EventsListener.PlayerQuitListener;
import ru.jl1mbo.AnarchyCore.Manager.Scoreboard.ScoreboardAPI;
import ru.jl1mbo.AnarchyCore.Manager.Scoreboard.Network.DisplaySlot;
import ru.jl1mbo.AnarchyCore.Manager.Scoreboard.Network.Scoreboard;
import ru.jl1mbo.AnarchyCore.Manager.Scoreboard.Network.ScoreboardDisplay;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.EconomyAPI.EconomyAPI;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.Permissions.PermissionAPI;

public class ScoreboardTask extends Task {
	private static Map<Player, Scoreboard> SCOREBOARDS = new HashMap<>();

	public static void register() {
		PluginManager pluginManager = Server.getInstance().getPluginManager();
		pluginManager.registerEvents(new PlayerJoinListener(), Main.getInstance());
		pluginManager.registerEvents(new PlayerQuitListener(), Main.getInstance());
	}

	public static void showScoreboard(Player player, boolean hide) {
		if (hide) {
			Scoreboard scoreboard = ScoreboardAPI.createScoreboard();
			ScoreboardDisplay scoreboardDisplay = scoreboard.addDisplay(DisplaySlot.SIDEBAR, "dumy", "§3DEATH §fMC");
			scoreboardDisplay.addLine(PermissionAPI.getAllGroups().get(PermissionAPI.getGroup(player.getName())).getGroupName() + player.getName(), 0);
			scoreboardDisplay.addLine("§1", 1);
			scoreboardDisplay.addLine("§r§fБаланс§7: §6" + String.format("%.1f", EconomyAPI.myMoney(player.getName())), 2);
			scoreboardDisplay.addLine("§r§fОнлайн§7: §6" + Server.getInstance().getOnlinePlayers().size(), 3);
			scoreboardDisplay.addLine("§4", 4);
			scoreboardDisplay.addLine("§r§fНаигранно§7: §6" + (AuthorizationAPI.getGameTime(player.getName()) /  86400 % 24) + "§7/§6" + (AuthorizationAPI.getGameTime(
										  player.getName()) / 3600 % 24) + "§7/§6" + (AuthorizationAPI.getGameTime(player.getName()) / 60 % 60), 5);
			scoreboardDisplay.addLine("§r§6death§7-§6mc§7.§6online", 6);
			scoreboard.showFor(player);
			SCOREBOARDS.put(player, scoreboard);
		} else {
			Scoreboard scoreboard = SCOREBOARDS.get(player);
			if (scoreboard != null) {
				scoreboard.hideFor(player);
			}
			ScoreboardTask.SCOREBOARDS.remove(player);
		}
	}

	@Override()
	public void onRun(int tick) {
		for (Player player : Server.getInstance().getOnlinePlayers().values()) {
			if (player.isOnline()) {
				if (player.spawned) {
					//SCOREBOARDS.get(player).hideFor(player);
					showScoreboard(player, false);
					showScoreboard(player, true);
				}
			}
		}
	}
}