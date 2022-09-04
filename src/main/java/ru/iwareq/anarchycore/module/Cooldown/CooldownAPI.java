package ru.iwareq.anarchycore.module.Cooldown;

import cn.nukkit.Player;
import ru.iwareq.anarchycore.module.Cooldown.Utils.Cooldown;
import ru.iwareq.anarchycore.module.Cooldown.Utils.CooldownTp;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class CooldownAPI {

	public static final Map<String, CooldownTp> cooldownsTp = new ConcurrentHashMap<>();

	private static final Set<Cooldown> cooldowns = new HashSet<>();

	public static Set<Cooldown> getCooldowns() {
		return cooldowns;
	}

	public static Map<String, CooldownTp> getCooldownsTp() {
		return cooldownsTp;
	}

	public static void addCooldown(Player player, String command, int seconds) {
		if (player.isOp()) {
			return;
		}
		cooldowns.add(new Cooldown(player, command, seconds));
	}

	public static void addTask(Player player, Runnable runnable, int seconds) {
		cooldownsTp.put(player.getName().toLowerCase(), new CooldownTp(player, runnable, seconds));

	}

	public static boolean canTask(Player player) {
		return cooldownsTp.containsKey(player.getName().toLowerCase());
	}

	public static void removeTask(Player player) {
		cooldownsTp.remove(player.getName().toLowerCase());
	}
}