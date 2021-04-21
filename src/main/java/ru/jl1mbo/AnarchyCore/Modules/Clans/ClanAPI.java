package ru.jl1mbo.AnarchyCore.Modules.Clans;

import cn.nukkit.Player;
import ru.jl1mbo.MySQLUtils.MySQLUtils;

import java.util.List;

public class ClanAPI {

	public static String PREFIX = "§l§7(§3Кланы§7) §r";

	public static void createClan(Player player, String clanName) {
		MySQLUtils.query("INSERT INTO `Clans` (`ClanName`) VALUES ('" + clanName + "')");
		MySQLUtils.query("INSERT INTO `ClanMembers` (`Name`, `Role`, `ClanID`) VALUES ('" + player.getName() + "', 'Глава', '" + getClanIdByClanName(clanName) + "')");
	}

	public static void deleteClan(Integer clanId) {
		MySQLUtils.query("DELETE FROM `Clans` WHERE (`ID`) = '" + clanId + "'");
		MySQLUtils.query("DELETE FROM `ClanMembers` WHERE (`ClanID`) = '" + clanId + "'");
	}

	public static void leaveClan(String playerName) {
		MySQLUtils.query("DELETE FROM `ClanMembers` WHERE UPPER (`Name`) = '" + playerName.toUpperCase() + "'");
	}

	public static void sendRequestsClan(Player player, Integer clanId) {
		MySQLUtils.query("INSERT INTO `RequestsPlayer` (`Name`, `ClanID`) VALUES ('" + player.getName() + "', '" + clanId + "')");
		MySQLUtils.query("INSERT INTO `RequestsClan` (`Name`, `ClanID`) VALUES ('" + player.getName() + "', '" + clanId + "')");
	}

	public static void acceptRequestsClan(Player player, Integer clanId) {
		MySQLUtils.query("INSERT INTO `ClanMembers` (`Name`, `Role`, `ClanID`) VALUES ('" + player.getName() + "', 'Участник', '" + clanId + "')");
		removeRequestClan(player.getName(), clanId);
	}

	public static void removeRequestClan(String playerName, Integer clanId) {
		MySQLUtils.query("DELETE FROM `RequestsPlayer` WHERE UPPER (`Name`) = '" + playerName.toUpperCase() + "' AND (`ClanID`) = '" + clanId + "'");
		MySQLUtils.query("DELETE FROM `RequestsClan` WHERE UPPER (`Name`) = '" + playerName.toUpperCase() + "' AND (`ClanID`) = '" + clanId + "'");
	}

	public static void changePlayerRole(String playerName, String role) {
		MySQLUtils.query("UPDATE `ClanMembers` SET `Role` = '" + role + "' WHERE UPPER (`Name`) = '" + playerName.toUpperCase() + "'");
	}

	public static boolean playerIsInClan(String playerName) {
		return MySQLUtils.getInteger("SELECT `ID` FROM `ClanMembers` WHERE UPPER (`Name`) = '" + playerName.toUpperCase() + "'") != -1;
	}

	public static List<Integer> getPlayerRequests(String playerName) {
		return MySQLUtils.getIntegerList("SELECT `ClanID` FROM `RequestsPlayer` WHERE UPPER (`Name`) = '" + playerName.toUpperCase() + "'");
	}

	public static String getPlayerRole(String playerName) {
		return MySQLUtils.getString("SELECT `Role` FROM `ClanMembers` WHERE UPPER (`Name`) = '" + playerName.toUpperCase() + "'");
	}

	public static int getPlayerClanId(String playerName) {
		return MySQLUtils.getInteger("SELECT `ClanID` FROM `ClanMembers` WHERE UPPER (`Name`) = '" + playerName.toUpperCase() + "'");
	}

	public static String getClanName(Integer clanId) {
		return MySQLUtils.getString("SELECT `ClanName` FROM `Clans` WHERE (`ID`) = '" + clanId + "'");
	}

	public static List<String> getClanRequests(Integer clanId) {
		return MySQLUtils.getStringList("SELECT `Name` FROM `RequestsClan` WHERE (`ClanID`) = '" + clanId + "'");
	}

	public static List<String> getClanMembers(Integer clanId) {
		return MySQLUtils.getStringList("SELECT `Name` FROM `ClanMembers` WHERE (`ClanID`) = '" + clanId + "'");
	}

	public static Integer getClanIdByClanName(String clanName) {
		return MySQLUtils.getInteger("SELECT `ID` FROM `Clans` WHERE UPPER (`ClanName`) = '" + clanName.toUpperCase() + "'");
	}

	public static boolean isTeam(String playerName, String targetName) {
		if (playerIsInClan(playerName)) {
			return getPlayerClanId(playerName) == getPlayerClanId(targetName);
		}
		return false;
	}

	public static boolean clanNameExists(String clanName) {
		return MySQLUtils.getInteger("SELECT `ID` FROM `Clans` WHERE UPPER (`ClanName`) = '" + clanName + "'") == -1;
	}
}