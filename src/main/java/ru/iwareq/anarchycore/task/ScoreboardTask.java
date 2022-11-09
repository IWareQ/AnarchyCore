package ru.iwareq.anarchycore.task;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.scheduler.PluginTask;
import lombok.Getter;
import ru.iwareq.anarchycore.AnarchyCore;
import ru.iwareq.anarchycore.manager.Scoreboard.Network.DisplaySlot;
import ru.iwareq.anarchycore.manager.Scoreboard.Network.Scoreboard;
import ru.iwareq.anarchycore.manager.Scoreboard.Network.ScoreboardDisplay;
import ru.iwareq.anarchycore.manager.Scoreboard.ScoreboardAPI;
import ru.iwareq.anarchycore.module.Auth.AuthAPI;
import ru.iwareq.anarchycore.module.Economy.EconomyAPI;
import ru.iwareq.anarchycore.module.EventsListener.EventsListener;
import ru.iwareq.anarchycore.module.Permissions.PermissionAPI;
import ru.iwareq.anarchycore.module.title.TitleAPI;
import ru.iwareq.anarchycore.module.title.Titles;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class ScoreboardTask extends PluginTask<AnarchyCore> {

	public static final Map<String, Scoreboard> SCOREBOARDS = new HashMap<>();
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yy HH:mm");
	@Getter
	private static ScoreboardTask instance;

	public ScoreboardTask(AnarchyCore owner) {
		super(owner);
		instance = this;

		DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
	}

	public static void showScoreboard(Player player) {
		Scoreboard scoreboard = ScoreboardAPI.createScoreboard();

		ScoreboardDisplay scoreboardDisplay = scoreboard.addDisplay(DisplaySlot.SIDEBAR, "dumy", "  LITENEX");
		scoreboardDisplay.addLine("§rНик: " + player.getName(), 0);

		String time = " ∞";
		if (PermissionAPI.getTimeGroup(player.getName()) > 0) {
			time = " (" + PermissionAPI.getTimeGroup(player.getName()) / 86400 + " дн)";
		}

		scoreboardDisplay.addLine("§rРанг: " + PermissionAPI.getPlayerGroup(player.getName()).getGroupName() + time, 1);
		Titles currentTitle = TitleAPI.getManager(player).getCurrentTitle();
		scoreboardDisplay.addLine("§rТитул: " + (currentTitle == null ? "нету" : currentTitle.getName()), 2);
		scoreboardDisplay.addLine("§3", 3);
		scoreboardDisplay.addLine("§rБаланс: " + EconomyAPI.format(AuthAPI.getMoney(player.getName())), 4);
		scoreboardDisplay.addLine("§rПинг: " + player.getPing(), 4);
		scoreboardDisplay.addLine("§rУ: " + EventsListener.getKills(player.getName()) + " | С: " + EventsListener.getDeaths(player), 5);
		scoreboardDisplay.addLine("§6", 6);

		int seconds = AnarchyCore.getInstance().getClearTask().getSeconds();
		if (seconds <= 5 * 60 && seconds > 0) {
			scoreboardDisplay.addLine("§rДо очистки: " + (seconds / 60) + "м " + (seconds % 60) + "с", 7);
			scoreboardDisplay.addLine("§8", 8);
			scoreboardDisplay.addLine("§rОнлайн: " + Server.getInstance().getOnlinePlayers().size(), 9);
			scoreboardDisplay.addLine("§r" + getCurrentDate() + " | #1", 10);
		} else {
			scoreboardDisplay.addLine("§rОнлайн: " + Server.getInstance().getOnlinePlayers().size(), 7);
			scoreboardDisplay.addLine("§8", 8);
			scoreboardDisplay.addLine("§r" + getCurrentDate() + " | #1", 9);
		}


		ScoreboardAPI.setScoreboard(player, scoreboard);
		SCOREBOARDS.put(player.getName().toLowerCase(), scoreboard);
	}

	public static void hideScoreboard(Player player) {
		if (SCOREBOARDS.containsKey(player.getName().toLowerCase())) {
			ScoreboardAPI.removeScorebaord(player, SCOREBOARDS.get(player.getName().toLowerCase()));
			SCOREBOARDS.remove(player.getName());
		}
	}

	public static String getCurrentDate() {
		return DATE_FORMAT.format(new Date());
	}

	@Override()
	public void onRun(int currentTick) {
		for (Player player : Server.getInstance().getOnlinePlayers().values()) {
			if (player.isOnline() && player.spawned && SCOREBOARDS.containsKey(player.getName().toLowerCase())) {
				hideScoreboard(player);
				showScoreboard(player);
			}
		}
	}
}
