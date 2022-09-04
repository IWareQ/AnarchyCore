package ru.iwareq.anarchycore.module.Commands.Home;

import cn.nukkit.level.Position;

public class HomeAPI {

	public static final String PREFIX = "§l§7(§3Дом§7) §r";

	private static final HomeDB DB = new HomeDB();

	public static boolean canHome(String playerName) {
		return DB.canHome(playerName);
	}

	public static void deleteHome(String playerName) {
		DB.deleteHome(playerName);
	}

	public static void createHome(String playerName, Position position) {
		DB.createHome(playerName, position);
	}

	public static Position getPosition(String playerName) {
		return DB.getPosition(playerName);
	}
}
