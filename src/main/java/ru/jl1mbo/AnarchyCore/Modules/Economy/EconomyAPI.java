package ru.jl1mbo.AnarchyCore.Modules.Economy;

import ru.jl1mbo.MySQLUtils.MySQLUtils;

public class EconomyAPI {
	public static String PREFIX = "§l§7(§3Экономика§7) §r";

	public static double myMoney(String playerName) {
		return MySQLUtils.getDouble("SELECT `Money` FROM `Users` WHERE UPPER (`Name`) = '" + playerName.toUpperCase() + "'");
	}

	public static void setMoney(String playerName, double count) {
		MySQLUtils.query("UPDATE `Users` SET `Money` = '" + count + "' WHERE UPPER (`Name`) = '" + playerName.toUpperCase() + "'");
	}

	public static void addMoney(String playerName, double count) {
		setMoney(playerName, myMoney(playerName) + count);
	}

	public static void reduceMoney(String playerName, double count) {
		setMoney(playerName, myMoney(playerName) - count);
	}
}