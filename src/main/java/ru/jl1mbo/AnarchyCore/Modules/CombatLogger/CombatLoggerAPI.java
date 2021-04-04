package ru.jl1mbo.AnarchyCore.Modules.CombatLogger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cn.nukkit.Player;
import cn.nukkit.utils.DummyBossBar;

public class CombatLoggerAPI {
	private static final Map<String, Long> bossBarList = new HashMap<>();
	public static ConcurrentHashMap<String, Long> combat = new ConcurrentHashMap<>();

	public static void createBossBar(Player player, String text, Integer length) {
		DummyBossBar dummyBossBar = new DummyBossBar.Builder(player).text(text).length(length).build();
		player.createBossBar(dummyBossBar);
		getMap().put(player.getName(), dummyBossBar.getBossBarId());
	}

	public static void updateBossBar(Player player, String text, Integer length) {
		DummyBossBar dummyBossBar = getDummyBossBar(player);
		if (dummyBossBar != null) {
			dummyBossBar.setText(text);
			dummyBossBar.setLength(length);
		}
	}

	public static DummyBossBar getDummyBossBar(Player player) {
		Long barId = getBarId(player);
		if (barId != null) {
			return player.getDummyBossBar(barId);
		}
		return null;
	}

	public static void addCombat(Player player) {
		if (!inCombat(player)) {
			createBossBar(player, "        §fВы вошли в §6PvP §fрежим§7!\n\n§fНе выходите из игры еще §630 §fсек§7.!", 100);
		}
		combat.put(player.getName(), System.currentTimeMillis() / 1000L + 30);
	}

	public static boolean inCombat(Player player) {
		return combat.containsKey(player.getName());
	}

	public static void removeCombat(Player player) {
		combat.remove(player.getName());
		removeBossBar(player);
	}

	private static Long getBarId(Player player) {
		return getMap().get(player.getName());
	}

	private static Map<String, Long> getMap() {
		return bossBarList;
	}

	private static void removeBossBar(Player player) {
		Long barId = getBarId(player);
		if (barId != null) {
			player.removeBossBar(barId);
			getMap().remove(player.getName());
		}
	}
}