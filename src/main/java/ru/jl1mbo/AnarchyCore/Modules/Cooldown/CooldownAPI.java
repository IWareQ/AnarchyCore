package ru.jl1mbo.AnarchyCore.Modules.Cooldown;

import java.util.HashSet;

import cn.nukkit.Player;
import ru.jl1mbo.AnarchyCore.Modules.Cooldown.Utils.Cooldown;

public class CooldownAPI {
	private static HashSet<Cooldown> cooldowns = new HashSet<>();

	public static HashSet<Cooldown> getCooldowns() {
		return cooldowns;
	}

	public static void addCooldown(Player player, String command, Integer seconds) {
		cooldowns.add(new Cooldown(player, command, seconds));
	}
}