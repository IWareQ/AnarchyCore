package ru.jl1mbo.AnarchyCore.Modules.Cooldown;

import cn.nukkit.Player;
import ru.jl1mbo.AnarchyCore.Modules.Cooldown.Utils.Cooldown;

import java.util.HashSet;

public class CooldownAPI {

	private static final HashSet<Cooldown> cooldowns = new HashSet<>();

	public static HashSet<Cooldown> getCooldowns() {
		return cooldowns;
	}

	public static void addCooldown(Player player, String command, int seconds) {
		cooldowns.add(new Cooldown(player, command, seconds));
	}
}