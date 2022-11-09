package ru.iwareq.anarchycore.module.Auth;

import me.hteppl.data.database.SQLiteDatabase;
import org.jdbi.v3.core.Handle;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class AuthDB extends SQLiteDatabase {

	public AuthDB() {
		super("Users");

		this.executeScheme("CREATE TABLE IF NOT EXISTS Auth\n" +
				"(\n" +
				"    ID       INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
				"    Name     varchar(32) NOT NULL COLLATE NOCASE,\n" +
				"    DateReg  varchar(32) NOT NULL,\n" +
				"    IpReg    varchar(32) NOT NULL,\n" +
				"    DateLast varchar(32) DEFAULT NULL,\n" +
				"    IpLast   varchar(32) DEFAULT NULL\n" +
				");");

		this.executeScheme("CREATE TABLE IF NOT EXISTS Users\n" +
				"(\n" +
				"    ID             INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
				"    Name           varchar(32) NOT NULL COLLATE NOCASE,\n" +
				"    Xuid           varchar(32) NOT NULL,\n" +
				"    Permission     varchar(16) NOT NULL DEFAULT 'player',\n" +
				"    PermissionTime int(64)     NOT NULL DEFAULT '-1',\n" +
				"    Money          REAL        NOT NULL DEFAULT '0.0',\n" +
				"    GameTime       int(64)     NOT NULL DEFAULT '0',\n" +
				"    Cases          int(64)     NOT NULL DEFAULT '0'\n" +
				");");
	}

	public boolean isRegister(String playerName) {
		try (Handle handle = this.connect()) {
			return handle.select("SELECT ID FROM Auth WHERE Name = ?;", playerName.toLowerCase())
					.mapTo(String.class).findOne().isPresent();
		}
	}

	public String getGroup(String playerName) {
		try (Handle handle = this.connect()) {
			return handle.select("SELECT Permission FROM Users WHERE Name = ?;", playerName.toLowerCase())
					.mapTo(String.class).one();
		}
	}

	public void setGroup(String playerName, String groupId, long expiredTime) {
		try (Handle handle = this.connect()) {
			handle.createUpdate("UPDATE Users SET Permission = :group, PermissionTime = :expiredTime WHERE Name = :name;")
					.bind("group", groupId.toLowerCase())
					.bind("expiredTime", expiredTime)
					.bind("name", playerName.toLowerCase()).execute();
		}
	}

	public double getMoney(String playerName) {
		try (Handle handle = this.connect()) {
			return handle.select("SELECT Money FROM Users WHERE Name = ?;", playerName.toLowerCase())
					.mapTo(Double.class).one();
		}
	}

	public int getCases(String playerName) {
		try (Handle handle = this.connect()) {
			return handle.select("SELECT Cases FROM Users WHERE Name = ?;", playerName.toLowerCase())
					.mapTo(Integer.class).one();
		}
	}

	public LinkedHashMap<Double, String> getMoneys() {
		try (Handle handle = this.connect()) {
			LinkedHashMap<Double, String> result = new LinkedHashMap<>();
			handle.createQuery("SELECT Money, Name FROM Users ORDER BY Money DESC LIMIT 10;").map(rowView -> {
				result.put(rowView.getColumn("Money", Double.class), rowView.getColumn("Name", String.class));
				return 1;
			}).one();
			return result;
		}
	}

	public LinkedHashMap<Long, String> getTimes() {
		try (Handle handle = this.connect()) {
			LinkedHashMap<Long, String> result = new LinkedHashMap<>();
			handle.createQuery("SELECT GameTime, Name FROM Users ORDER BY GameTime DESC LIMIT 10;").map(rowView -> {
				result.put(rowView.getColumn("GameTime", Long.class), rowView.getColumn("Name", String.class));
				return 1L;
			}).one();
			return result;
		}
	}

	public void setMoney(String playerName, double count) {
		try (Handle handle = this.connect()) {
			handle.createUpdate("UPDATE Users SET Money = :money WHERE Name = :name;")
					.bind("money", count)
					.bind("name", playerName.toLowerCase()).execute();
		}
	}

	public void setCases(String playerName, int count) {
		try (Handle handle = this.connect()) {
			handle.createUpdate("UPDATE Users SET Cases = :cases WHERE Name = :name;")
					.bind("cases", count)
					.bind("name", playerName.toLowerCase()).execute();
		}
	}

	public long getGameTime(String playerName) {
		try (Handle handle = this.connect()) {
			return handle.select("SELECT GameTime FROM Users WHERE Name = ?;", playerName.toLowerCase())
					.mapTo(Long.class).one();
		}
	}

	public void setGameTime(String playerName, long gameTime) {
		try (Handle handle = this.connect()) {
			handle.createUpdate("UPDATE Users SET GameTime = :gameTime WHERE Name = :name;")
					.bind("gameTime", gameTime)
					.bind("name", playerName.toLowerCase()).execute();
		}
	}

	public void setDateAndIpLast(String playerName, String date, String ip) {
		try (Handle handle = this.connect()) {
			handle.createUpdate("UPDATE Auth SET DateLast = :dateLast, IpLast = :ipLast WHERE Name = :name;")
					.bind("dateLast", date)
					.bind("ipLast", ip)
					.bind("name", playerName.toLowerCase()).execute();
		}
	}

	public void registerPlayer(String name, String xuid, String date, String ip) {
		try (Handle handle = this.connect()) {
			handle.createUpdate("INSERT INTO Auth (Name, DateReg, IpReg) VALUES (:name, :date, :ip)")
					.bind("name", name.toLowerCase())
					.bind("date", date)
					.bind("ip", ip).execute();

			handle.createUpdate("INSERT INTO Users (Name, Xuid) VALUES (:name, :xuid)")
					.bind("name", name.toLowerCase())
					.bind("xuid", xuid).execute();
		}
	}

	public long getTimeGroup(String playerName) {
		try (Handle handle = this.connect()) {
			return handle.select("SELECT PermissionTime FROM Users WHERE Name = ?;", playerName.toLowerCase())
					.mapTo(Long.class).one();
		}
	}

	public Map<String, Long> getAllTimeGroup() {
		try (Handle handle = this.connect()) {
			Map<String, Long> result = new HashMap<>();
			handle.createQuery("SELECT Name, PermissionTime FROM Users WHERE PermissionTime != -1;").map(rowView -> {
				result.put(rowView.getColumn("Name", String.class).toLowerCase(), rowView.getColumn("PermissionTime",
						Long.class));
				return 1L;
			}).list();

			return result;
		}
	}

	public void updateTimeGroup(String user, long time) {
		try (Handle handle = this.connect()) {
			handle.createUpdate("UPDATE Users SET PermissionTime = :time WHERE Name = :name;")
					.bind("time", time)
					.bind("name", user).execute();
		}
	}
}
