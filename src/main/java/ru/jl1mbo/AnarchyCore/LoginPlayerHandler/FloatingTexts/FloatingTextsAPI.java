package ru.jl1mbo.AnarchyCore.LoginPlayerHandler.FloatingTexts;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Position;
import cn.nukkit.level.particle.FloatingTextParticle;
import cn.nukkit.plugin.PluginManager;
import cn.nukkit.utils.Config;
import ru.jl1mbo.AnarchyCore.Main;
import ru.jl1mbo.AnarchyCore.LoginPlayerHandler.FloatingTexts.EventsListener.PlayerJoinListener;
import ru.jl1mbo.AnarchyCore.Manager.WorldSystem.WorldSystemAPI;

public class FloatingTextsAPI {

	public static void register() {
		PluginManager pluginManager = Server.getInstance().getPluginManager();
		pluginManager.registerEvents(new PlayerJoinListener(), Main.getInstance());
	}

	public static void addFloatingTexts(Position pos, String title, String text, Player player) {
		WorldSystemAPI.getSpawn().addParticle(new FloatingTextParticle(pos, title, text), player);
	}

	public static void addFloatingKillsTops(Position pos, Player player) {
		FloatingTextParticle floatingTexts = new FloatingTextParticle(pos, "§l§6Самые опасные Игроки сервера");
		StringBuilder stringBuilder = new StringBuilder();
		Config config = new Config(Main.getInstance().getDataFolder() + "/kills.yml", Config.YAML);
		for (Map.Entry<String, Integer> entry : calculateScore(config).entrySet()) {
			stringBuilder.append("§l" + entry.getKey()).append(" §7- §6").append(entry.getValue()).append(" §f⚔\n");
			floatingTexts.setText(stringBuilder.toString() + "§l§6Статистика обновляется каждый перезаход§7!");
		}
		WorldSystemAPI.getSpawn().addParticle(floatingTexts, player);
	}

	public static void addFloatingDeathsTops(Position pos, Player player) {
		FloatingTextParticle floatingTexts = new FloatingTextParticle(pos, "§l§6Press F to pay respects");
		StringBuilder stringBuilder = new StringBuilder();
		Config config = new Config(Main.getInstance().getDataFolder() + "/deaths.yml", Config.YAML);
		for (Map.Entry<String, Integer> entry : calculateScore(config).entrySet()) {
			stringBuilder.append("§l" + entry.getKey()).append(" §7- §6").append(entry.getValue()).append(" §f☠\n");
			floatingTexts.setText(stringBuilder.toString() + "§l§6Статистика обновляется каждый перезаход§7!");
		}
		WorldSystemAPI.getSpawn().addParticle(floatingTexts, player);
	}

	private static Map<String, Integer> calculateScore(Config config) {
		Map<String, Integer> hashMap = new HashMap<>();
		for (Map.Entry<String, Object> entry : config.getAll().entrySet()) {
			hashMap.put(entry.getKey(), (Integer) entry.getValue());
		}
		Map<String, Integer> sorted = hashMap;
		sorted = hashMap.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
				 LinkedHashMap::new));
		return sorted;
	}
}