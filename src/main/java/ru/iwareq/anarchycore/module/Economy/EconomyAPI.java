package ru.iwareq.anarchycore.module.Economy;

import ru.iwareq.anarchycore.module.Auth.AuthAPI;

public class EconomyAPI {

	public static String PREFIX = "§l§7(§3Экономика§7) §r";

	public static String format(double value) {
		return String.format("%.2f", value).replace(',', '.');
	}

	public static void addMoney(String playerName, double count) {
		AuthAPI.setMoney(playerName, AuthAPI.getMoney(playerName) + count);
	}

	public static void reduceMoney(String playerName, double count) {
		AuthAPI.setMoney(playerName, AuthAPI.getMoney(playerName) - count);
	}
}