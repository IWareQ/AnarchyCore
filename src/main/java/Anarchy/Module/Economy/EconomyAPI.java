package Anarchy.Module.Economy;

import Anarchy.Manager.Sessions.PlayerSessionManager;
import Anarchy.Utils.SQLiteUtils;
import cn.nukkit.Player;
import cn.nukkit.Server;

public class EconomyAPI {
	public static String PREFIX = "§l§7(§6Экономика§7) §r";
	
	public static Integer myMoney(Player player) {
		return PlayerSessionManager.getPlayerSession(player.getName()).getInteger("Money");
	}
	
	public static Integer myMoney(String playerName) {
		if (Server.getInstance().getPlayerExact(playerName) != null) {
			return PlayerSessionManager.getPlayerSession(playerName).getInteger("Money");
		} else {
			return SQLiteUtils.selectInteger("Users.db", "SELECT `Money` FROM `USERS` WHERE UPPER(`Username`) = \'" + playerName.toUpperCase() + "\';");
		}
	}
	
	public static void setMoney(Player player, int value) {
		PlayerSessionManager.getPlayerSession(player.getName()).setInteger("Money", value);
	}
	
	public static void setMoney(String playerName, int value) {
		if (Server.getInstance().getPlayerExact(playerName) != null) {
			PlayerSessionManager.getPlayerSession(playerName).setInteger("Money", value);
		} else {
			SQLiteUtils.query("Users.db", "UPDATE `USERS` SET `Money` = \'" + value + "\' WHERE UPPER(`Username`) = \'" + playerName.toUpperCase() + "\';");
		}
	}
	
	public static void addMoney(Player player, int value) {
		String name = player.getName();
		setMoney(name, myMoney(name) + value);
	}
	
	public static void addMoney(String playerName, int value) {
		setMoney(playerName, myMoney(playerName) + value);
	}
	
	public static void reduceMoney(Player player, int value) {
		String name = player.getName();
		setMoney(name, myMoney(name) - value);
	}
	
	public static void reduceMoney(String playerName, int value) {
		setMoney(playerName, myMoney(playerName) - value);
	}
}