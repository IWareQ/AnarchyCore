package ru.iwareq.anarchycore.module.CombatLogger;

import cn.nukkit.Player;
import cn.nukkit.utils.DummyBossBar;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CombatLoggerAPI {

	private static final Map<String, Long> bossBarList = new HashMap<>();
	private static final Map<String, String> damagers = new HashMap<>();
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

	public static void addCombat(Player damager, Player victim) {
		if (!inCombat(damager)) {
			createBossBar(damager, "        §fДо конца боя осталось: 30!\n\n§fПоследний бой: *****", 100);
		}

		if (!inCombat(victim)) {
			createBossBar(victim, "        §fДо конца боя осталось: 30!\n\n§fПоследний бой: *****", 100);
		}

		damagers.put(damager.getName(), victim.getName());
		damagers.put(victim.getName(), damager.getName());

		combat.put(damager.getName(), System.currentTimeMillis() / 1000L + 30);
		combat.put(victim.getName(), System.currentTimeMillis() / 1000L + 30);
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

	public static String getPlayerTarget(Player player) {
		return damagers.get(player.getName());
	}
}