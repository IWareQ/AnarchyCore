package Anarchy.Manager.Sessions.Session;

import java.util.HashMap;
import java.util.Map;

import Anarchy.Utils.SQLiteUtils;

public class PlayerSession {
	private Map<String, String> playerData;
	private Map<String, String> queryData = new HashMap<>();
	private String playerName;
	private Long startSession;

	public PlayerSession(String playerName) {
		this.playerName = playerName;
		this.startSession = System.currentTimeMillis();
		playerData = (Map<String, String>) SQLiteUtils.selectStringMap("SELECT * FROM `Users` WHERE UPPER(`Username`) = '" + playerName.toUpperCase() + "';");
	}

	public void saveSession() {
		queryData.clear();
		queryData.putAll(playerData);
		queryData.remove("Account_ID");
		queryData.remove("Username");
		queryData.remove("Gametime");
		SQLiteUtils.query("UPDATE `Users` SET " + SQLiteUtils.buildQueryFromMap(queryData) + ", `Gametime` = " + (Integer.parseInt(playerData.get("Gametime")) + getSessionTime()) + " WHERE UPPER(`Username`) = '" + playerName.toUpperCase() + "';");
	}

	public String getName() {
		return this.playerName;
	}

	public Long getStartSession() {
		return startSession;
	}

	public Long getSessionTime() {
		return System.currentTimeMillis() / 1000L - startSession / 1000L;
	}

	public Map<String, String> getPlayerData() {
		return playerData;
	}

	public String getString(String key) {
		return this.playerData.get(key);
	}

	public int getInteger(String key) {
		return Integer.parseInt(this.playerData.get(key));
	}

	public Double getDouble(String key) {
		return Double.parseDouble(this.playerData.get(key));
	}

	public Long getLong(String key) {
		return Long.getLong(this.playerData.get(key));
	}

	public void addInteger(String key) {
		int value = this.getInteger(key);
		playerData.remove(key);
		playerData.put(key, String.valueOf(value + 1));
	}

	public void addInteger(String key, int add) {
		int value = this.getInteger(key);
		playerData.remove(key);
		playerData.put(key, String.valueOf(value + add));
	}

	public void setString(String key, String value) {
		playerData.remove(key);
		playerData.put(key, value);
	}

	public void setInteger(String key, int value) {
		playerData.remove(key);
		playerData.put(key, String.valueOf(value));
	}

	public void setDouble(String key, Double value) {
		playerData.remove(key);
		playerData.put(key, String.valueOf(value));
	}
}