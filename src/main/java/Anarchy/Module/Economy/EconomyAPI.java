package Anarchy.Module.Economy;

import Anarchy.Utils.SQLiteUtils;
import cn.nukkit.Player;

public class EconomyAPI {
	public static String PREFIX = "§l§7(§3Экономика§7) §r";

	public static Double myMoney(String playerName) {
		return SQLiteUtils.selectDouble("Users.db", "SELECT Money FROM USERS WHERE UPPER(Username) = \'" + playerName.toUpperCase() + "\';");
	}

	public static void setMoney(String playerName, Double value) {
		SQLiteUtils.query("Users.db", "UPDATE USERS SET Money = \'" + value + "\' WHERE UPPER(Username) = \'" + playerName.toUpperCase() + "\';");
	}

	public static void addMoney(Player player, Double value) {
		setMoney(player.getName(), myMoney(player.getName()) + value);
	}

	public static void addMoney(String playerName, Double value) {
		setMoney(playerName, myMoney(playerName) + value);
	}

	public static void reduceMoney(Player player, Double value) {
		setMoney(player.getName(), myMoney(player.getName()) - value);
	}

	public static void reduceMoney(String playerName, Double value) {
		setMoney(playerName, myMoney(playerName) - value);
	}
}