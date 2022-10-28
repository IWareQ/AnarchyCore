package ru.iwareq.anarchycore.module.Auth;

import java.util.LinkedHashMap;
import java.util.Map;

public class AuthAPI {

	private static final AuthDB DB = new AuthDB();

	public static boolean isRegister(String playerName) {
		return DB.isRegister(playerName);
	}

	public static String getGroup(String playerName) {
		return DB.getGroup(playerName);
	}

	public static void setGroup(String playerName, String groupId, long expiredTime) {
		DB.setGroup(playerName, groupId, expiredTime);
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

	public static int getCases(String playerName) {
		return DB.getCases(playerName);
	}

	public static void setDateAndIpLast(String playerName, String date, String ip) {
		DB.setDateAndIpLast(playerName, date, ip);
	}

	public static void registerPlayer(String name, String xuid, String date, String ip) {
		DB.registerPlayer(name, xuid, date, ip);
	}

	public static long getTimeGroup(String playerName) {
		return DB.getTimeGroup(playerName);
	}

	public static Map<String, Long> getAllTimeGroup() {
		return DB.getAllTimeGroup();
	}

	public static void updateTimeGroup(String user, long time) {
		DB.updateTimeGroup(user, time);
	}

	public static void setCases(String name, int count) {
		DB.setCases(name, count);
	}
}
