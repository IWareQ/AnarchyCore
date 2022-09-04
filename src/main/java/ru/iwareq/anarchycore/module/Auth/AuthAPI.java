package ru.iwareq.anarchycore.module.Auth;

import java.util.LinkedHashMap;
import java.util.TreeMap;

public class AuthAPI {

	private static final AuthDB DB = new AuthDB();

	public static boolean isRegister(String playerName) {
		return DB.isRegister(playerName);
	}

	public static String getGroup(String playerName) {
		return DB.getGroup(playerName);
	}

	public static void setGroup(String playerName, String groupId) {
		DB.setGroup(playerName, groupId);
	}

	public static double getMoney(String playerName) {
		return DB.getMoney(playerName);
	}

	public static LinkedHashMap<Double, String> getMoneys() {
		return DB.getMoneys();
	}

	public static LinkedHashMap<Long, String> getTimes() {
		return DB.getTimes();
	}

	public static void setMoney(String playerName, double count) {
		DB.setMoney(playerName, count);
	}

	public static long getGameTime(String playerName) {
		return DB.getGameTime(playerName);
	}

	public static void setGameTime(String playerName, long gameTime) {
		DB.setGameTime(playerName, gameTime);
	}

	public static void setDateAndIpLast(String playerName, String date, String ip) {
		DB.setDateAndIpLast(playerName, date, ip);
	}

	public static void registerPlayer(String name, String xuid, String date, String ip) {
		DB.registerPlayer(name, xuid, date, ip);
	}
}
