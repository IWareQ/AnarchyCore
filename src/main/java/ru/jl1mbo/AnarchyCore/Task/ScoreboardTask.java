package ru.jl1mbo.AnarchyCore.Task;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.scheduler.Task;
import ru.jl1mbo.AnarchyCore.Manager.Scoreboard.Network.DisplaySlot;
import ru.jl1mbo.AnarchyCore.Manager.Scoreboard.Network.Scoreboard;
import ru.jl1mbo.AnarchyCore.Manager.Scoreboard.Network.ScoreboardDisplay;
import ru.jl1mbo.AnarchyCore.Manager.Scoreboard.ScoreboardAPI;
import ru.jl1mbo.AnarchyCore.Modules.AdminSystem.AdminAPI;
import ru.jl1mbo.AnarchyCore.Modules.Auth.AuthAPI;
import ru.jl1mbo.AnarchyCore.Modules.Economy.EconomyAPI;
import ru.jl1mbo.AnarchyCore.Modules.Permissions.PermissionAPI;
import ru.jl1mbo.AnarchyCore.Utils.Utils;

import java.util.HashMap;
import java.util.Map;

public class ScoreboardTask extends Task {

	private static final HashMap<Player, Scoreboard> SCOREBOARDS = new HashMap<>();

	public static void showScoreboard(Player player, boolean hide) {
		Scoreboard scoreboard = null;
		if (hide) {
			if (AdminAPI.isBanned(player.getName())) {
				Map<String, String> banData = AdminAPI.getBanData(player.getName());
				scoreboard = ScoreboardAPI.createScoreboard();
				ScoreboardDisplay scoreboardDisplay = scoreboard.addDisplay(DisplaySlot.SIDEBAR, "dumy", "§6Аккаунт заблокирован");
				scoreboardDisplay.addLine(PermissionAPI.getPlayerGroup(player.getName()).getGroupName() + " " + player.getName(), 0);
				scoreboardDisplay.addLine("§1", 1);
				scoreboardDisplay.addLine("Причина§7: §6" + banData.get("Reason"), 2);
				scoreboardDisplay.addLine("§3", 3);
				scoreboardDisplay.addLine("§6" + Utils.getRemainingTime(Long.parseLong(banData.get("Time"))), 4);
				scoreboardDisplay.addLine("§5", 5);
				scoreboardDisplay.addLine("§6death§7-§6mc§7.§6online", 6);
				ScoreboardAPI.setScoreboard(player, scoreboard);
			}
			scoreboard = ScoreboardAPI.createScoreboard();
			ScoreboardDisplay scoreboardDisplay = scoreboard.addDisplay(DisplaySlot.SIDEBAR, "dumy", "§3DEATH §fMC");
			scoreboardDisplay.addLine(PermissionAPI.getPlayerGroup(player.getName()).getGroupName() + " " + player.getName(), 0);
			scoreboardDisplay.addLine("§1", 1);
			scoreboardDisplay.addLine("§rБаланс§7: §6" + String.format("%.1f", EconomyAPI.myMoney(player.getName())), 2);
			scoreboardDisplay.addLine("§rОнлайн§7: §6" + Server.getInstance().getOnlinePlayers().size(), 3);
			scoreboardDisplay.addLine("§4", 4);
			long time = AuthAPI.getGameTime(player.getName());
			scoreboardDisplay.addLine("§rНаигранно§7: §6" + (time / 86400 % 24) + "§7/§6" + (time / 3600 % 24) + "§7/§6" + (time / 60 % 60), 5);
			scoreboardDisplay.addLine("§r§6death§7-§6mc§7.§6online", 6);
			ScoreboardAPI.setScoreboard(player, scoreboard);
		} else {
			ScoreboardAPI.removeScorebaord(player, scoreboard);
		}
	}

	@Override()
	public void onRun(int currentTick) {
		for (Player player : Server.getInstance().getOnlinePlayers().values()) {
			if (player.isOnline() && player.spawned) {
				showScoreboard(player, false);
				showScoreboard(player, true);
			}
		}
	}
}