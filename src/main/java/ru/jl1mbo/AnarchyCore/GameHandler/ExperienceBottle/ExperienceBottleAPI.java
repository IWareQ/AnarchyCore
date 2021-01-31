package ru.jl1mbo.AnarchyCore.GameHandler.ExperienceBottle;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.plugin.PluginManager;
import ru.jl1mbo.AnarchyCore.Main;
import ru.jl1mbo.AnarchyCore.GameHandler.ExperienceBottle.EventsListener.InventoryTransactionListener;
import ru.jl1mbo.AnarchyCore.GameHandler.ExperienceBottle.EventsListener.PlayerInteractListener;

public class ExperienceBottleAPI {

	public static void register() {
		PluginManager pluginManager = Server.getInstance().getPluginManager();
		pluginManager.registerEvents(new InventoryTransactionListener(), Main.getInstance());
		pluginManager.registerEvents(new PlayerInteractListener(), Main.getInstance());
	}

	public static double convert(Player player) {
		return convertLevelToExperience(player.getExperienceLevel()) + player.getExperience();
	}

	public static double convertLevelToExperience(double level) {
		double experience = 0;
		if (level <= 16) {
			experience = (level * level) + 6 * level;
		} else if (level >= 17 && level <= 31) {
			experience = 2.5 * (level * level) - 40.5 * level + 360;
		} else if (level >= 32) {
			experience = 4.5 * (level * level) - 162.5 * level + 2220;
		}
		return experience;
	}
}