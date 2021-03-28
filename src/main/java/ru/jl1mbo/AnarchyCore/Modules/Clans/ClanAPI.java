package ru.jl1mbo.AnarchyCore.Modules.Clans;

import java.util.ArrayList;
import java.util.List;

import cn.nukkit.Player;
import ru.jl1mbo.AnarchyCore.Utils.SQLiteUtils;

public class ClanAPI {
	public static String PREFIX = "§l§7(§3Кланы§7) §r";

	public static void createClan(Player player, String clanName) {
		SQLiteUtils.query("Clan.db", "INSERT INTO `Clans` (`ClanName`) VALUES ('" + clanName + "')");
		SQLiteUtils.query("Clan.db", "INSERT INTO `Members` (`Name`, `Role`, `ClanID`) VALUES ('" + player.getName() + "', 'Глава', '" + getClanIdByClanName(clanName) + "')");
	}

	public static void deleteClan(Integer clanId) {
		SQLiteUtils.query("Clan.db", "DELETE FROM `Clans` WHERE (`ID`) = '" + clanId + "'");
		SQLiteUtils.query("Clan.db", "DELETE FROM `Members` WHERE (`ClanID`) = '" + clanId + "'");
	}

	public static void leaveClan(String playerName) {
		SQLiteUtils.query("Clan.db", "DELETE FROM `Members` WHERE UPPER (`Name`) = '" + playerName.toUpperCase() + "'");
	}

	public static void sendRequestsClan(Player player, Integer clanId) {
		SQLiteUtils.query("Clan.db", "INSERT INTO `RequestsPlayer` (`Name`, `ClanID`) VALUES ('" + player.getName() + "', '" + clanId + "')");
		SQLiteUtils.query("Clan.db", "INSERT INTO `RequestsClan` (`Name`, `ClanID`) VALUES ('" + player.getName() + "', '" + clanId + "')");
	}

	public static void acceptRequestsClan(Player player, Integer clanId) {
		SQLiteUtils.query("Clan.db", "INSERT INTO `Members` (`Name`, `Role`, `ClanID`) VALUES ('" + player.getName() + "', 'Участник', '" + clanId + "')");
		removeRequestClan(player.getName(), clanId);
	}

	public static void removeRequestClan(String playerName, Integer clanId) {
		SQLiteUtils.query("Clan.db", "DELETE FROM `RequestsPlayer` WHERE UPPER (`Name`) = '" + playerName.toUpperCase() + "' AND (`ClanID`) = '" + clanId + "'");
		SQLiteUtils.query("Clan.db", "DELETE FROM `RequestsClan` WHERE UPPER (`Name`) = '" + playerName.toUpperCase() + "' AND (`ClanID`) = '" + clanId + "'");
	}

	public static void changePlayerRole(String playerName, String role) {
		SQLiteUtils.query("Clan.db", "UPDATE `Members` SET `Role` = '" + role + "' WHERE UPPER (`Name`) = '" + playerName.toUpperCase() + "'");
	}

	public static boolean playerIsInClan(String playerName) {
		return SQLiteUtils.getInteger("Clan.db", "SELECT `MemberID` FROM `Members` WHERE UPPER (`Name`) = '" + playerName.toUpperCase() + "'") != -1;
	}

	public static ArrayList<Integer> getPlayerRequests(String playerName) {
		return SQLiteUtils.getIntegerList("Clan.db", "SELECT `ClanID` FROM `RequestsPlayer` WHERE UPPER (`Name`) = '" + playerName.toUpperCase() + "'");
	}

	public static String getPlayerRole(String playerName) {
		return SQLiteUtils.getString("Clan.db", "SELECT `Role` FROM `Members` WHERE UPPER (`Name`) = '" + playerName.toUpperCase() + "'");
	}

	public static int getPlayerClanId(String playerName) {
		return SQLiteUtils.getInteger("Clan.db", "SELECT `ClanID` FROM `Members` WHERE UPPER (`Name`) = '" + playerName.toUpperCase() + "'");
	}

	public static String getClanName(Integer clanId) {
		return SQLiteUtils.getString("Clan.db", "SELECT `ClanName` FROM `Clans` WHERE (`ID`) = '" + clanId + "'");
	}

	public static List<String> getClanRequests(Integer clanId) {
		return SQLiteUtils.getStringList("Clan.db", "SELECT `Name` FROM `RequestsClan` WHERE (`ClanID`) = '" + clanId + "'");
	}

	public static ArrayList<String> getClanMembers(Integer clanId) {
		return SQLiteUtils.getStringList("Clan.db", "SELECT `Name` FROM `Members` WHERE (`ClanID`) = '" + clanId + "'");
	}

	public static Integer getClanIdByClanName(String clanName) {
		return SQLiteUtils.getInteger("Clan.db", "SELECT `ID` FROM `Clans` WHERE UPPER (`ClanName`) = '" + clanName.toUpperCase() + "'");
	}

	public static boolean isTeam(String playerName, String  targetName) {
		if (playerIsInClan(playerName)) {
			return getPlayerClanId(playerName) == getPlayerClanId(targetName);
		}
		return false;
	}

	public static boolean clanNameExists(String clanName) {
		return SQLiteUtils.getInteger("Clan.db", "SELECT `ID` FROM `Clans` WHERE UPPER (`ClanName`) = '" + clanName + "'") == -1;
	}
}