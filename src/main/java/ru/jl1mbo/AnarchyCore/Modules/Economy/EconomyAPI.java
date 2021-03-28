package ru.jl1mbo.AnarchyCore.Modules.Economy;

import ru.jl1mbo.AnarchyCore.Utils.SQLiteUtils;

public class EconomyAPI {
	public static String PREFIX = "§l§7(§3Экономика§7) §r";

	public static double myMoney(String playerName) {
		return SQLiteUtils.getDouble("Users.db", "SELECT `Money` FROM `Users` WHERE UPPER (`Name`) = '" + playerName.toUpperCase() + "';");
	}

	public static void setMoney(String playerName, Double count) {
		SQLiteUtils.query("Users.db", "UPDATE `Users` SET `Money` = '" + count + "' WHERE UPPER (`Name`) = '" + playerName.toUpperCase() + "';");
	}

	public static void addMoney(String playerName, Double count) {
		setMoney(playerName, myMoney(playerName) + count);
	}

	public static void reduceMoney(String playerName, Double count) {
		setMoney(playerName, myMoney(playerName) - count);
	}
}