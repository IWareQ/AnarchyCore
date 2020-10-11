package Anarchy.Module.Economy;

import Anarchy.Manager.Sessions.PlayerSessionManager;
import Anarchy.Utils.SQLiteUtils;
import cn.nukkit.Player;
import cn.nukkit.Server;

public class EconomyAPI {
	public static String PREFIX = "§l§7(§3Экономика§7) §r";

	public static Double myMoney(Player player) {
		return PlayerSessionManager.getPlayerSession(player.getName()).getDouble("Money");
	}

	public static Double myMoney(String playerName) {
		if (Server.getInstance().getPlayerExact(playerName) != null) {
			return PlayerSessionManager.getPlayerSession(playerName).getDouble("Money");
		} else {
			return SQLiteUtils.selectDouble("Users.db", "SELECT Money FROM USERS WHERE UPPER(Username) = '" + playerName.toUpperCase() + "';");
		}
	}

	public static void setMoney(Player player, Double value) {
		PlayerSessionManager.getPlayerSession(player.getName()).setDouble("Money", value);
	}

	public static void setMoney(String playerName, Double value) {
		if (Server.getInstance().getPlayerExact(playerName) != null) {
			PlayerSessionManager.getPlayerSession(playerName).setDouble("Money", value);
		} else {
			SQLiteUtils.query("Users.db", "UPDATE USERS SET Money = '" + value + "' WHERE UPPER(Username) = '" + playerName.toUpperCase() + "';");
		}
	}

	public static void addMoney(Player player, Double value) {
		String playerName = player.getName();
		setMoney(playerName, myMoney(playerName) + value);
	}

	public static void addMoney(String playerName, Double value) {
		setMoney(playerName, myMoney(playerName) + value);
	}

	public static void reduceMoney(Player player, Double value) {
		String playerName = player.getName();
		setMoney(playerName, myMoney(playerName) - value);
	}

	public static void reduceMoney(String playerName, Double value) {
		setMoney(playerName, myMoney(playerName) - value);
	}
}