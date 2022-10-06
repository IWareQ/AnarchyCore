package ru.iwareq.anarchycore.task;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.scheduler.Task;
import ru.iwareq.anarchycore.manager.Scoreboard.Network.DisplaySlot;
import ru.iwareq.anarchycore.manager.Scoreboard.Network.Scoreboard;
import ru.iwareq.anarchycore.manager.Scoreboard.Network.ScoreboardDisplay;
import ru.iwareq.anarchycore.manager.Scoreboard.ScoreboardAPI;
import ru.iwareq.anarchycore.module.AdminSystem.AdminAPI;
import ru.iwareq.anarchycore.module.Auth.AuthAPI;
import ru.iwareq.anarchycore.module.Clans.ClanAPI;
import ru.iwareq.anarchycore.module.Economy.EconomyAPI;
import ru.iwareq.anarchycore.module.EventsListener.EventsListener;
import ru.iwareq.anarchycore.module.Permissions.PermissionAPI;
import ru.iwareq.anarchycore.util.Utils;

import java.util.HashMap;
import java.util.Map;

public class ScoreboardTask extends Task {

	public static final Map<String, Scoreboard> SCOREBOARDS = new HashMap<>();
	private static final String[] texts = new String[]{"test1", "test2", "test3"};
	private static int sec = 0;

	public static void showScoreboard(Player player, boolean show) {
		Scoreboard scoreboard = ScoreboardAPI.createScoreboard();
		if (show) {
			if (AdminAPI.isBanned(player.getName())) {
				ScoreboardDisplay scoreboardDisplay = scoreboard.addDisplay(DisplaySlot.SIDEBAR, "dumy", "§6Аккаунт заблокирован");
				scoreboardDisplay.addLine(PermissionAPI.getPlayerGroup(player.getName()).getGroupName() + " " + player.getName(), 0);
				scoreboardDisplay.addLine("§1", 1);
				scoreboardDisplay.addLine("Причина§7: §6" + AdminAPI.getBanReason(player.getName()), 2);
				scoreboardDisplay.addLine("§3", 3);
				scoreboardDisplay.addLine("§6" + Utils.getRemainingTime(AdminAPI.getBanTime(player.getName())), 4);
				scoreboardDisplay.addLine("§5", 5);
				scoreboardDisplay.addLine("§6test", 6);
				ScoreboardAPI.setScoreboard(player, scoreboard);
				SCOREBOARDS.put(player.getName().toLowerCase(), scoreboard);
				return;
			}

			ScoreboardDisplay scoreboardDisplay = scoreboard.addDisplay(DisplaySlot.SIDEBAR, "dumy", "§3DEATH §fMC");
			scoreboardDisplay.addLine(PermissionAPI.getPlayerGroup(player.getName()).getGroupName() + " " + player.getName(), 0);
			scoreboardDisplay.addLine("§1", 1);
			scoreboardDisplay.addLine("§rКлан§7: " + (ClanAPI.playerIsInClan(player.getName()) ?
			                                          ClanAPI.getClanName(ClanAPI.getPlayerClanId(player.getName())) : "нету"), 2);
			scoreboardDisplay.addLine("§rПинг§7: " + player.getPing(), 3);
			scoreboardDisplay.addLine("§rБаланс§7: §6" + EconomyAPI.format(AuthAPI.getMoney(player.getName())), 4);
			scoreboardDisplay.addLine("§rОнлайн§7: §6" + Server.getInstance().getOnlinePlayers().size(), 5);
			scoreboardDisplay.addLine("§6", 6);
			scoreboardDisplay.addLine("У: " + EventsListener.getKills(player.getName()) + " | С: " + EventsListener.getDeaths(player),
					7);

			long time = AuthAPI.getGameTime(player.getName());
			scoreboardDisplay.addLine("§rНаигранно§7: §6" + (time / 86400 % 24) + "§7/§6" + (time / 3600 % 24) + "§7" +
					"/§6" + (time / 60 % 60), 8);

			scoreboardDisplay.addLine(texts[sec], 9);


			ScoreboardAPI.setScoreboard(player, scoreboard);
			SCOREBOARDS.put(player.getName().toLowerCase(), scoreboard);
		} else {
			if (SCOREBOARDS.containsKey(player.getName().toLowerCase())) {
				ScoreboardAPI.removeScorebaord(player, SCOREBOARDS.get(player.getName().toLowerCase()));
				SCOREBOARDS.remove(player.getName());
			}
		}
	}

	@Override()
	public void onRun(int currentTick) {
		for (Player player : Server.getInstance().getOnlinePlayers().values()) {
			if (player.isOnline() && player.spawned && SCOREBOARDS.containsKey(player.getName().toLowerCase())) {
				sec++;

				showScoreboard(player, false);
				showScoreboard(player, true);


				if (sec + 1 >= texts.length) {
					sec = 0;
				}
			}
		}
	}
}