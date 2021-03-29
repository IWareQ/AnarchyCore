package ru.jl1mbo.AnarchyCore.Modules.Auth;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerLocallyInitializedEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.level.Position;
import cn.nukkit.level.particle.FloatingTextParticle;
import cn.nukkit.utils.Config;
import ru.jl1mbo.AnarchyCore.Main;
import ru.jl1mbo.AnarchyCore.Manager.WorldSystem.WorldSystemAPI;
import ru.jl1mbo.AnarchyCore.Modules.Permissions.PermissionAPI;
import ru.jl1mbo.AnarchyCore.Task.ScoreboardTask;
import ru.jl1mbo.AnarchyCore.Utils.SQLiteUtils;
import ru.jl1mbo.AnarchyCore.Utils.Utils;

public class AuthEventsListener implements Listener {
	private static HashMap<Player, Long> playerTime = new HashMap<>();

	@EventHandler()
	public void onPlayerLocallyInitialized(PlayerLocallyInitializedEvent event) {
		Player player = event.getPlayer();
		if (!player.hasPlayedBefore()) {
			player.teleport(WorldSystemAPI.getSpawn().getSafeSpawn());
			player.setSpawn(WorldSystemAPI.getSpawn().getSafeSpawn());
		}
	}

	@EventHandler()
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		SQLiteUtils.query("Auth.db", "UPDATE `Auth` SET `DateLast` = '" + Utils.getDate() + "', `IpLast` = '" + player.getAddress() + "' WHERE UPPER (`Name`) = '" + player.getName().toUpperCase() + "'");
		if (playerTime.containsKey(player)) {
			SQLiteUtils.query("Users.db", "UPDATE `Users` SET `GameTime` = '" + (AuthAPI.getGameTime(player.getName()) + (System.currentTimeMillis() / 1000L -  playerTime.get(
								  player))) + "' WHERE UPPER (`Name`) = '" + player.getName().toUpperCase() + "'");
		}
		ScoreboardTask.showScoreboard(player, false);
		event.setQuitMessage("");
	}

	@EventHandler()
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		String date = Utils.getDate();
		String ip = player.getAddress();
		if (!AuthAPI.isRegister(player.getName())) {
			SQLiteUtils.query("Auth.db", "INSERT INTO `Auth` (`Name`, `DateReg`, `IpReg`) VALUES ('" + player.getName() + "', '" + date + "', '" + ip + "');");
			SQLiteUtils.query("Users.db", "INSERT INTO `Users` (`Name`, `XboxID`) VALUES ('" + player.getName() + "', '" + player.getLoginChainData().getXUID() + "');");
		}
		ScoreboardTask.showScoreboard(player, true);
		PermissionAPI.updatePermissions(event.getPlayer());
		PermissionAPI.updateNamedTag(event.getPlayer());
		player.sendMessage("\u00a7l\u00a76\u2022 \u00a7r\u0414\u043e\u0431\u0440\u043e \u043f\u043e\u0436\u0430\u043b\u043e\u0432\u0430\u0442\u044c \u043d\u0430 \u00a73DEATH \u00a7fMC \u00a77(\u00a7c\u0410\u043d\u0430\u0440\u0445\u0438\u044f\u00a77)\n\u00a7l\u00a76\u2022 \u00a7r\u041c\u044b \u0432 \u00a79\u0412\u041a \u00a77- \u00a7fvk\u00a77.\u00a7fcom\u00a77/\u00a76deathmc\u00a77.\u00a76club \u00a7l\u00a76| \u00a7r\u00a7f\u041d\u0430\u0448 \u0441\u0430\u0439\u0442 \u00a77- \u00a76death\u00a77-\u00a76mc\u00a77.\u00a76online");
		playerTime.put(player, System.currentTimeMillis() / 1000L);
		player.setCheckMovement(false);
		event.setJoinMessage("");
		WorldSystemAPI.getSpawn().addParticle(new FloatingTextParticle(new Position(90.5, 93.5, 26.5), "\u00a7l\u00a76\u0410\u043b\u0442\u0430\u0440\u044c",
											  "\u00a7l\u0421\u0442\u0440\u0430\u043d\u043d\u0430\u044f \u0448\u0442\u0443\u043a\u0430\u00a77)"), player);
		WorldSystemAPI.getSpawn().addParticle(new FloatingTextParticle(new Position(71.5, 91.5, 57.5), "\u00a7l\u00a76\u0416\u0435\u043b\u0435\u0437\u043d\u044b\u0439 \u041f\u0440\u0438\u0432\u0430\u0442",
											  "\u00a7l2 \u00a77\u00d7 \u00a7f2"), player);
		WorldSystemAPI.getSpawn().addParticle(new FloatingTextParticle(new Position(70.5, 91.5, 55.5), "\u00a7l\u00a76\u0410\u043b\u043c\u0430\u0437\u043d\u044b\u0439 \u041f\u0440\u0438\u0432\u0430\u0442",
											  "\u00a7l4 \u00a77\u00d7 \u00a7f4"), player);
		WorldSystemAPI.getSpawn().addParticle(new FloatingTextParticle(new Position(70.5, 91.5, 52.5),
											  "\u00a7l\u00a76\u0418\u0437\u0443\u043c\u0440\u0443\u0434\u043d\u044b\u0439 \u041f\u0440\u0438\u0432\u0430\u0442", "\u00a7l8 \u00a77\u00d7 \u00a7f8"), player);
		WorldSystemAPI.getSpawn().addParticle(new FloatingTextParticle(new Position(71.5, 91.5, 50.5),
											  "\u00a7l\u00a76\u0418\u0437\u0443\u043c\u0440\u0443\u0434\u043d\u044b\u0439 \u041f\u0440\u0438\u0432\u0430\u0442", "\u00a7l10 \u00a77\u00d7 \u00a7f10"), player);
		WorldSystemAPI.getSpawn().addParticle(new FloatingTextParticle(new Position(79.5, 92, 20.5), "\u00a7l\u00a76\u041f\u0440\u044b\u0433\u0430\u0439 \u0432 \u043f\u043e\u0440\u0442\u0430\u043b\u00a77!",
											  "\u00a7l\u041f\u0440\u043e\u0441\u0442\u043e \u043f\u0440\u044b\u0433\u0430\u0439 \u0438 \u043d\u0430\u0447\u0438\u043d\u0430\u0439 \u0432\u044b\u0436\u0438\u0432\u0430\u0442\u044c"), player);
		WorldSystemAPI.getSpawn().addParticle(new FloatingTextParticle(new Position(72.5, 92, 54.5), "\u00a7l\u00a76\u041a\u0430\u043a \u043f\u0440\u0438\u0432\u0430\u0442\u0438\u0442\u044c\u00a77?",
											  "\u00a7l\u0427\u0442\u043e\u0431\u044b \u0437\u0430\u043f\u0440\u0438\u0432\u0430\u0442\u044c \u0440\u0435\u0433\u0438\u043e\u043d\u00a77,\n\u00a7f\u043f\u0440\u043e\u0441\u0442\u043e \u0443\u0441\u0442\u0430\u043d\u043e\u0432\u0438 \u043e\u0434\u0438\u043d \u0438\u0437 \u0431\u043b\u043e\u043a\u043e\u0432\n\u00a7f\u043a\u043e\u0442\u043e\u0440\u044b\u0435 \u0441\u0442\u043e\u044f\u0442 \u0440\u044f\u0434\u043e\u043c\u00a77. \u00a7f\u041a\u0430\u0436\u0434\u044b\u0439 \u0431\u043b\u043e\u043a \u0438\u043c\u0435\u0435\u0442\n\u00a7f\u043e\u0433\u0440\u0430\u043d\u0438\u0447\u0435\u043d\u043d\u044b\u0439 \u0440\u0430\u0434\u0438\u0443\u0441 \u043f\u0440\u0438\u0432\u0430\u0442\u0430\u00a77,\n\u00a7f\u043a\u043e\u0442\u043e\u0440\u044b\u0439 \u0441\u043e\u0437\u0434\u0430\u0435\u0442\u0441\u044f \u0432\u043e\u043a\u0440\u0443\u0433 \u0431\u043b\u043e\u043a\u0430\u00a77!"),
											  player);
		addFloatingKillsTops(new Position(74.5, 92, 25.5), player);
		addFloatingDeathsTops(new Position(84.5, 92, 25.5), player);
	}

	private static void addFloatingKillsTops(Position pos, Player player) {
		FloatingTextParticle floatingTexts = new FloatingTextParticle(pos,
				"\u00a7l\u00a76\u0421\u0430\u043c\u044b\u0435 \u043e\u043f\u0430\u0441\u043d\u044b\u0435 \u0438\u0433\u0440\u043e\u043a\u0438 \u0441\u0435\u0440\u0432\u0435\u0440\u0430");
		StringBuilder stringBuilder = new StringBuilder();
		Config config = new Config(Main.getInstance().getDataFolder() + "/kills.yml", Config.YAML);
		for (Map.Entry<String, Integer> entry : calculateScore(config).entrySet()) {
			stringBuilder.append("\u00a7l" + entry.getKey()).append(" \u00a77- \u00a76").append(entry.getValue()).append(" \u00a7f\u2694\n");
			floatingTexts.setText(stringBuilder.toString() +
								  "\u00a7l\u00a76\u0421\u0442\u0430\u0442\u0438\u0441\u0442\u0438\u043a\u0430 \u043e\u0431\u043d\u043e\u0432\u043b\u044f\u0435\u0442\u0441\u044f \u043a\u0430\u0436\u0434\u044b\u0439 \u043f\u0435\u0440\u0435\u0437\u0430\u0445\u043e\u0434\u00a77!");
		}
		WorldSystemAPI.getSpawn().addParticle(floatingTexts, player);
	}

	private static void addFloatingDeathsTops(Position pos, Player player) {
		FloatingTextParticle floatingTexts = new FloatingTextParticle(pos, "\u00a7l\u00a76Press F to pay respects");
		StringBuilder stringBuilder = new StringBuilder();
		Config config = new Config(Main.getInstance().getDataFolder() + "/deaths.yml", Config.YAML);
		for (Map.Entry<String, Integer> entry : calculateScore(config).entrySet()) {
			stringBuilder.append("\u00a7l" + entry.getKey()).append(" \u00a77- \u00a76").append(entry.getValue()).append(" \u00a7f\u2620\n");
			floatingTexts.setText(stringBuilder.toString() +
								  "\u00a7l\u00a76\u0421\u0442\u0430\u0442\u0438\u0441\u0442\u0438\u043a\u0430 \u043e\u0431\u043d\u043e\u0432\u043b\u044f\u0435\u0442\u0441\u044f \u043a\u0430\u0436\u0434\u044b\u0439 \u043f\u0435\u0440\u0435\u0437\u0430\u0445\u043e\u0434\u00a77!");
		}
		WorldSystemAPI.getSpawn().addParticle(floatingTexts, player);
	}

	private static LinkedHashMap<String, Integer> calculateScore(Config config) {
		LinkedHashMap<String, Integer> oldMap = new LinkedHashMap<>();
		for (Map.Entry<String, Object> entry : config.getAll().entrySet()) {
			oldMap.put(entry.getKey(), (Integer)entry.getValue());
		}
		LinkedHashMap<String, Integer> sorted = oldMap.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1,
												e2)->e2, LinkedHashMap::new));
		LinkedHashMap<String, Integer> newMap = sorted.entrySet().stream().limit(10).sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).collect(Collectors.toMap(Map.Entry::getKey,
												Map.Entry::getValue, (e1, e2)->e2, LinkedHashMap::new));
		return newMap;
	}
}