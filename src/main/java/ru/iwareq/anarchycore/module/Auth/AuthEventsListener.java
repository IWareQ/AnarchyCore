package ru.iwareq.anarchycore.module.Auth;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerLocallyInitializedEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.level.Position;
import cn.nukkit.level.particle.FloatingTextParticle;
import cn.nukkit.utils.Config;
import ru.iwareq.anarchycore.Main;
import ru.iwareq.anarchycore.manager.WorldSystem.WorldSystemAPI;
import ru.iwareq.anarchycore.module.Commands.HideGlobalChatCommand;
import ru.iwareq.anarchycore.module.Commands.Teleport.Commands.TpdeclineCommand;
import ru.iwareq.anarchycore.module.Economy.EconomyAPI;
import ru.iwareq.anarchycore.module.Permissions.PermissionAPI;
import ru.iwareq.anarchycore.task.ScoreboardTask;
import ru.iwareq.anarchycore.util.Utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class AuthEventsListener implements Listener {

	private static final HashMap<Player, Long> playerTime = new HashMap<>();

	private static void addFloatingTimeTops(Position pos, Player player) {
		FloatingTextParticle floatingTexts = new FloatingTextParticle(pos, "§l§6Задроты сервера");
		StringBuilder stringBuilder = new StringBuilder();

		int i = 1;
		for (Map.Entry<Long, String> entry : AuthAPI.getTimes().entrySet()) {
			Long time = entry.getKey();
			String user = entry.getValue();
			stringBuilder.append("§l").append(i + ". " + user).append(" §7- §6").append(Utils.getRemainingTime(time)).append(" " +
					"§f\n");
			i++;
			floatingTexts.setText(stringBuilder + "§l§6Статистика обновляется каждый перезаход§7!");
		}

		WorldSystemAPI.Spawn.addParticle(floatingTexts, player);
	}

	private static void addFloatingMoneyTops(Position pos, Player player) {
		FloatingTextParticle floatingTexts = new FloatingTextParticle(pos, "§l§6Самые богатые игроки");
		StringBuilder stringBuilder = new StringBuilder();

		int i = 1;
		for (Map.Entry<Double, String> entry : AuthAPI.getMoneys().entrySet()) {
			Double money = entry.getKey();
			String user = entry.getValue();
			stringBuilder.append("§l").append(i + ". " + user).append(" §7- §6").append(EconomyAPI.format(money)).append(" §f$\n");
			i++;
			floatingTexts.setText(stringBuilder + "§l§6Статистика обновляется каждый перезаход§7!");
		}

		WorldSystemAPI.Spawn.addParticle(floatingTexts, player);
	}

	private static void addFloatingKillsTops(Position pos, Player player) {
		FloatingTextParticle floatingTexts = new FloatingTextParticle(pos, "§l§6Самые опасные игроки сервера");
		StringBuilder stringBuilder = new StringBuilder();
		Config config = new Config(Main.getInstance().getDataFolder() + "/kills.yml", Config.YAML);
		int i = 1;
		for (Map.Entry<String, Integer> entry : calculateScore(config).entrySet()) {
			stringBuilder.append("§l").append(i + ". " + entry.getKey()).append(" §7- §6").append(entry.getValue()).append(
					" §f⚔\n");
			i++;
			floatingTexts.setText(stringBuilder + "§l§6Статистика обновляется каждый перезаход§7!");
		}
		WorldSystemAPI.Spawn.addParticle(floatingTexts, player);
	}

	private static void addFloatingDeathsTops(Position pos, Player player) {
		FloatingTextParticle floatingTexts = new FloatingTextParticle(pos, "§l§6Press F to pay respects");
		StringBuilder stringBuilder = new StringBuilder();
		Config config = new Config(Main.getInstance().getDataFolder() + "/deaths.yml", Config.YAML);
		int i = 1;
		for (Map.Entry<String, Integer> entry : calculateScore(config).entrySet()) {
			stringBuilder.append("§l").append(i + ". " + entry.getKey()).append(" §7- §6").append(entry.getValue()).append(" §f☠\n");
			i++;
			floatingTexts.setText(stringBuilder + "§l§6Статистика обновляется каждый перезаход§7!");
		}
		WorldSystemAPI.Spawn.addParticle(floatingTexts, player);
	}

	private static LinkedHashMap<String, Integer> calculateScore(Config config) {
		LinkedHashMap<String, Integer> oldMap = new LinkedHashMap<>();
		for (Map.Entry<String, Object> entry : config.getAll().entrySet()) {
			oldMap.put(entry.getKey(), (Integer) entry.getValue());
		}
		LinkedHashMap<String, Integer> sorted = oldMap.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
		return sorted.entrySet().stream().limit(10).sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
	}

	@EventHandler()
	public void onPlayerLocallyInitialized(PlayerLocallyInitializedEvent event) {
		Player player = event.getPlayer();
		if (!player.hasPlayedBefore()) {
			player.teleport(WorldSystemAPI.Spawn.getSafeSpawn());
			player.setSpawn(WorldSystemAPI.Spawn.getSafeSpawn());
		}
	}

	@EventHandler()
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		String name = player.getName();
		HideGlobalChatCommand.PLAYERS.remove(name);
		TpdeclineCommand.PLAYERS.remove(name.toLowerCase());
		AuthAPI.setDateAndIpLast(name, Utils.getDate(), player.getAddress());
		if (playerTime.containsKey(player)) {
			AuthAPI.setGameTime(name, AuthAPI.getGameTime(name) + (System.currentTimeMillis() / 1000L - playerTime.get(player)));
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
			AuthAPI.registerPlayer(player.getName(), player.getLoginChainData().getXUID(), date, ip);
		}
		ScoreboardTask.showScoreboard(player, true);
		PermissionAPI.updatePermissions(event.getPlayer());
		PermissionAPI.updateNamedTag(event.getPlayer());
		player.sendMessage("§l§6• §rДобро пожаловать на §3DEATH §fMC §7(§cАнархия§7)\n§l§6• §rМы в §9ВК §7- §fvk§7.§fcom§7/§6deathmc§7.§6club §l§6| §rНаш сайт §7- §6death§7-§6mc§7.§6online");
		playerTime.put(player, System.currentTimeMillis() / 1000L);
		player.setCheckMovement(false);
		event.setJoinMessage("");
		WorldSystemAPI.Spawn.addParticle(new FloatingTextParticle(new Position(90.5, 93.5, 26.5), "§l§6Алтарь", "§lСтранная штука§7)"), player);
		WorldSystemAPI.Spawn.addParticle(new FloatingTextParticle(new Position(118.5, 95.5, 106.5), "§l§6Железный Приват", "§l2 §7× §f2"), player);
		WorldSystemAPI.Spawn.addParticle(new FloatingTextParticle(new Position(120.5, 95.5, 104.5), "§l§6Алмазный Приват", "§l4 §7× §f4"), player);
		WorldSystemAPI.Spawn.addParticle(new FloatingTextParticle(new Position(122.5, 95.5, 104.5), "§l§6Изумрудный Приват", "§l8 §7× §f8"), player);
		WorldSystemAPI.Spawn.addParticle(new FloatingTextParticle(new Position(124.5, 95.5, 106.5), "§l§6Изумрудный Приват", "§l10 §7× §f10"), player);
		WorldSystemAPI.Spawn.addParticle(new FloatingTextParticle(new Position(79.5, 92, 20.5), "§l§6Прыгай в портал§7!", "§lПросто прыгай и начинай выживать"), player);
		WorldSystemAPI.Spawn.addParticle(new FloatingTextParticle(new Position(121.5, 94.5, 107.5), "§l§6Как приватить§7?", "§lЧтобы запривать регион§7,\n§fпросто установи один из блоков\n§fкоторые стоят рядом§7. §fКаждый блок имеет\n§fограниченный радиус привата§7,\n§fкоторый создается вокруг блока§7!"), player);
		addFloatingKillsTops(new Position(115.5, 96, 97.5), player);
		addFloatingDeathsTops(new Position(127.5, 96, 97.5), player);

		addFloatingMoneyTops(new Position(115.5, 100, 97.5), player);
		addFloatingTimeTops(new Position(127.5, 100, 97.5), player);
	}
}