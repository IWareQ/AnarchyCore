package ru.jl1mbo.AnarchyCore.GameHandler.Achievements;

import java.util.ArrayList;
import java.util.HashMap;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.utils.Config;
import ru.jl1mbo.AnarchyCore.Main;
import ru.jl1mbo.AnarchyCore.GameHandler.Achievements.Achievements.AmmunitionAchievements;
import ru.jl1mbo.AnarchyCore.GameHandler.Achievements.Achievements.TheFirstStepsAchievements;
import ru.jl1mbo.AnarchyCore.GameHandler.Achievements.Commands.AchievementsCommand;
import ru.jl1mbo.AnarchyCore.GameHandler.Achievements.Task.AmmunitionTask;
import ru.jl1mbo.AnarchyCore.GameHandler.Achievements.Utils.Achievement;

public class AchievementsAPI {
	public static Config config;
	private static final HashMap<String, Achievement> achievements = new HashMap<>();

	public static void register() {
		config = new Config(Main.getInstance().getDataFolder() + "/Achievements/Users.yml", Config.YAML);
		Server.getInstance().getCommandMap().register("", new AchievementsCommand());
		Server.getInstance().getScheduler().scheduleRepeatingTask(new AmmunitionTask(), 20);
		registerAchievement(new TheFirstStepsAchievements());
		registerAchievement(new AmmunitionAchievements());
	}

	public static boolean isRegister(String playerName) {
		return config.exists(playerName.toLowerCase());
	}

	public static void resetAchievements(String playerName) {
		config.set(playerName.toLowerCase() + ".achievements", new ArrayList<String>());
		config.set(playerName.toLowerCase() + ".completed", 0);
		config.save();
		config.reload();
	}

	public static int getAchievementPoints(String playerName, String achievementId) {
		return config.getInt(playerName.toLowerCase() + ".achievements." + achievementId.toLowerCase());
	}

	public static void addPoints(Player player, String achievementId, int count) {
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
				config.set(playerName.toLowerCase() + ".completed", getCompletedAchievementsCount(playerName) + 1);
			}
			config.save();
			config.reload();
		} catch (NullPointerException ex) {
			Server.getInstance().getLogger().error("Невозможно найти достижение §6" + achievementId.toLowerCase() + "§7, §fт§7.§fк оно не существует§7!");
		}
	}

	public static boolean isCompleted(String playerName, String achievementId) {
		try {
			Achievement achievement = getAchievements().get(achievementId);
			return config.getInt(playerName.toLowerCase() + ".achievements." + achievementId.toLowerCase()) >= achievement.getAchievementMaxPoints();
		} catch (NullPointerException e) {
			Server.getInstance().getLogger().error("Невозможно найти достижение §6" + achievementId.toLowerCase() + "§7, §fт§7.§fк оно не существует§7!");
			return false;
		}
	}

	public static int getCompletedAchievementsCount(String playerName) {
		return config.getInt(playerName.toLowerCase() + ".completed");
	}

	public static HashMap<String, Achievement> getAchievements() {
		return achievements;
	}

	public static void registerAchievement(Achievement achievement) {
		achievements.put(achievement.getAchievementId(), achievement);
	}
}