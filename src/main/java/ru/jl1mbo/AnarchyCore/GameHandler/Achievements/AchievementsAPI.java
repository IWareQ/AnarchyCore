package ru.jl1mbo.AnarchyCore.GameHandler.Achievements;

import java.util.ArrayList;
import java.util.HashMap;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.plugin.PluginManager;
import cn.nukkit.utils.Config;
import ru.jl1mbo.AnarchyCore.Main;
import ru.jl1mbo.AnarchyCore.GameHandler.Achievements.Achievements.AmmunitionAchievements;
import ru.jl1mbo.AnarchyCore.GameHandler.Achievements.Achievements.TheFirstStepsAchievements;
import ru.jl1mbo.AnarchyCore.GameHandler.Achievements.Commands.AchievementsCommand;
import ru.jl1mbo.AnarchyCore.GameHandler.Achievements.Utils.Achievement;
import ru.jl1mbo.AnarchyCore.Utils.ConfigUtils;

public class AchievementsAPI {
	private static final HashMap<String, Achievement> achievements = new HashMap<>();

	public static void register() {
		Server.getInstance().getCommandMap().register("", new AchievementsCommand());
		registerAchievement(new TheFirstStepsAchievements());
		registerAchievement(new AmmunitionAchievements());
	}

	public static boolean isRegister(String playerName) {
		return ConfigUtils.getAchievementsConfig().exists(playerName.toLowerCase());
	}

	public static void resetAchievements(String playerName) {
		Config config = ConfigUtils.getAchievementsConfig();
		config.set(playerName.toLowerCase() + ".achievements", new ArrayList<String>());
		config.set(playerName.toLowerCase() + ".completed", 0);
		config.save();
		config.reload();
	}

	public static int getAchievementPoints(String playerName, String achievementId) {
		return Main.getInstance().getConfig().getInt(playerName.toLowerCase() + ".achievements." + achievementId.toLowerCase());
	}

	public static void addPoints(Player player, String achievementId, int count) {
		Config config = ConfigUtils.getAchievementsConfig();
		String playerName = player.getName().toLowerCase();
		int points = config.getInt(playerName.toLowerCase() + ".achievements." + achievementId.toLowerCase());
		try {
			Achievement achievement = getAchievements().get(achievementId);
			if (achievement.getAchievementMaxPoints() <= points) {
				return;
			}
			config.set(playerName.toLowerCase() + ".achievements." + achievementId.toLowerCase(), points + count);
			if (achievement.getAchievementMaxPoints() <= points + count) {
				achievement.action(player);
				config.set(playerName.toLowerCase() + ".completed", config.getInt(playerName.toLowerCase() + ".completed") + 1);
			}
			config.save();
			config.reload();
		} catch (NullPointerException ex) {
			Server.getInstance().getLogger().error("§r§fНевозможно найти достижение §6" + achievementId.toLowerCase() + "§7, §fт§7.§fк оно не существует§7!");
		}
	}

	public static boolean isCompleted(String playerName, String achievementId) {
		try {
			Achievement achievement = getAchievements().get(achievementId);
			return Main.getInstance().getConfig().getInt(playerName.toLowerCase() + ".achievements." + achievementId.toLowerCase()) >= achievement.getAchievementMaxPoints();
		} catch (NullPointerException e) {
			Server.getInstance().getLogger().error("§r§fНевозможно найти достижение §6" + achievementId.toLowerCase() + "§7, §fт§7.§fк оно не существует§7!");
			return false;
		}
	}

	public static int getCompletedAchievementsCount(String playerName) {
		return Main.getInstance().getConfig().getInt(playerName.toLowerCase() + ".completed");
	}

	public static HashMap<String, Achievement> getAchievements() {
		return achievements;
	}

	public static void registerAchievement(Achievement achievement) {
		achievements.put(achievement.getAchievementId(), achievement);
	}
}