package ru.iwareq.anarchycore.module.Clans;

import cn.nukkit.Player;

import java.util.List;

public class ClanAPI {

	public static final String PREFIX = "§l§7(§3Кланы§7) §r";

	private static final ClanDB DB = new ClanDB();

	public static void createClan(Player player, String clanName) {
		DB.createClan(player, clanName);
	}

	public static void deleteClan(Integer clanId) {
		DB.deleteClan(clanId);
	}

	public static void leaveClan(String playerName) {
		DB.leaveClan(playerName);
	}

	public static void sendRequestsClan(Player player, Integer clanId) {
		DB.sendRequestsClan(player, clanId);
	}

	public static void acceptRequestsClan(Player player, Integer clanId) {
		DB.acceptRequestsClan(player, clanId);
		DB.removeRequestClan(player.getName(), clanId);
	}

	public static void removeRequestClan(String playerName, Integer clanId) {
		DB.removeRequestClan(playerName, clanId);
	}

	public static void changePlayerRole(String playerName, String role) {
		DB.changePlayerRole(playerName, role);
	}

	public static boolean playerIsInClan(String playerName) {
		return DB.playerIsInClan(playerName);
	}

	public static List<Integer> getPlayerRequests(String playerName) {
		return DB.getPlayerRequests(playerName);
	}

	public static String getPlayerRole(String playerName) {
		return DB.getPlayerRole(playerName);
	}

	public static int getPlayerClanId(String playerName) {
		return DB.getPlayerClanId(playerName);
	}

	public static String getClanName(Integer clanId) {
		return DB.getClanName(clanId);
	}

	public static List<String> getClanRequests(Integer clanId) {
		return DB.getClanRequests(clanId);
	}

	public static List<String> getClanMembers(Integer clanId) {
		return DB.getClanMembers(clanId);
	}

	public static Integer getClanIdByClanName(String clanName) {
		return DB.getClanIdByClanName(clanName);
	}

	public static boolean isTeam(String playerName, String targetName) {
		if (playerIsInClan(playerName)) {
			return getPlayerClanId(playerName) == getPlayerClanId(targetName);
		}
		return false;
	}

	public static boolean clanNameExists(String clanName) {
		return DB.clanNameExists(clanName);
	}
}