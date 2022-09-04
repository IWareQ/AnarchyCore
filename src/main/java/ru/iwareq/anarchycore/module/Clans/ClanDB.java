package ru.iwareq.anarchycore.module.Clans;

import cn.nukkit.Player;
import me.hteppl.data.database.SQLiteDatabase;
import org.jdbi.v3.core.Handle;

import java.util.List;

public class ClanDB extends SQLiteDatabase {

	public ClanDB() {
		super("clans");

		this.executeScheme("CREATE TABLE IF NOT EXISTS Clans\n" +
				"(\n" +
				"    ID   INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
				"    Name varchar(32) NOT NULL COLLATE NOCASE\n" +
				");\n");

		this.executeScheme("CREATE TABLE IF NOT EXISTS Members\n" +
				"(\n" +
				"    ID     INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
				"    Name   varchar(32) NOT NULL COLLATE NOCASE,\n" +
				"    Role   varchar(32) NOT NULL COLLATE NOCASE,\n" +
				"    ClanID int(16)     NOT NULL\n" +
				");");

		this.executeScheme("CREATE TABLE IF NOT EXISTS RequestsClan\n" +
				"(\n" +
				"    ID     INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
				"    Name   varchar(32) NOT NULL COLLATE NOCASE,\n" +
				"    ClanID int(16)     NOT NULL\n" +
				");");

		this.executeScheme("CREATE TABLE IF NOT EXISTS RequestsPlayer\n" +
				"(\n" +
				"    ID     INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
				"    Name   varchar(32) NOT NULL COLLATE NOCASE,\n" +
				"    ClanID int(16)     NOT NULL\n" +
				");");
	}

	public void createClan(Player player, String clanName) {
		try (Handle handle = this.connect()) {
			handle.createUpdate("INSERT INTO Clans (Name) VALUES (:clanName)")
					.bind("clanName", clanName).execute();

			handle.createUpdate("INSERT INTO Members (Name, Role, ClanID) VALUES (:name, :role, :clanId)")
					.bind("name", player.getName().toLowerCase())
					.bind("role", "Глава")
					.bind("clanId", this.getClanIdByClanName(clanName))
					.execute();
		}
	}

	public void deleteClan(int clanId) {
		try (Handle handle = this.connect()) {
			handle.createUpdate("DELETE FROM Clans WHERE ID = :id")
					.bind("id", clanId).execute();

			handle.createUpdate("DELETE FROM Members WHERE ClanID = :clanId")
					.bind("clanId", clanId)
					.execute();
		}
	}

	public void leaveClan(String playerName) {
		try (Handle handle = this.connect()) {
			handle.createUpdate("DELETE FROM Members WHERE Name = :name")
					.bind("name", playerName.toLowerCase())
					.execute();
		}
	}

	public void sendRequestsClan(Player player, int clanId) {
		try (Handle handle = this.connect()) {
			handle.createUpdate("INSERT INTO RequestsPlayer (name, clanId) VALUES (:name, :clanId)")
					.bind("name", player.getName().toLowerCase())
					.bind("clanId", clanId)
					.execute();

			handle.createUpdate("INSERT INTO RequestsClan (name, clanId) VALUES (:name, :clanId)")
					.bind("name", player.getName().toLowerCase())
					.bind("clanId", clanId)
					.execute();
		}
	}

	public void acceptRequestsClan(Player player, Integer clanId) {
		try (Handle handle = this.connect()) {
			handle.createUpdate("INSERT INTO Members (name, role, clanId) VALUES (:name, :role, :clanId)")
					.bind("name", player.getName().toLowerCase())
					.bind("role", "Участник")
					.bind("clanId", clanId)
					.execute();
		}
	}

	public void removeRequestClan(String playerName, int clanId) {
		try (Handle handle = this.connect()) {
			handle.createUpdate("DELETE FROM RequestsPlayer WHERE Name = :name AND ClanID = :clanId")
					.bind("name", playerName.toLowerCase())
					.bind("clanId", clanId)
					.execute();

			handle.createUpdate("DELETE FROM RequestsClan WHERE Name = :name AND ClanID = :clanId")
					.bind("name", playerName.toLowerCase())
					.bind("clanId", clanId)
					.execute();
		}
	}

	public void changePlayerRole(String playerName, String role) {
		try (Handle handle = this.connect()) {
			handle.createUpdate("UPDATE Members SET Role = :role WHERE Name = :name")
					.bind("role", role)
					.bind("name", playerName.toLowerCase())
					.execute();
		}
	}

	public boolean playerIsInClan(String playerName) {
		try (Handle handle = this.connect()) {
			return handle.select("SELECT ID FROM Members WHERE Name = ?", playerName.toLowerCase())
					.mapTo(Integer.class).findOne().isPresent();
		}
	}

	public List<Integer> getPlayerRequests(String playerName) {
		try (Handle handle = this.connect()) {
			return handle.select("SELECT ClanID FROM RequestsPlayer WHERE Name = ?", playerName.toLowerCase())
					.mapTo(Integer.class).list();
		}
	}

	public String getPlayerRole(String playerName) {
		try (Handle handle = this.connect()) {
			return handle.select("SELECT Role FROM Members WHERE Name = ?", playerName.toLowerCase())
					.mapTo(String.class).one();
		}
	}

	public int getPlayerClanId(String playerName) {
		try (Handle handle = this.connect()) {
			return handle.select("SELECT ClanID FROM Members WHERE Name = ?", playerName.toLowerCase())
					.mapTo(Integer.class).one();
		}
	}

	public String getClanName(int clanId) {
		try (Handle handle = this.connect()) {
			return handle.select("SELECT Name FROM Clans WHERE ID = ?", clanId)
					.mapTo(String.class).one();
		}
	}

	public List<String> getClanRequests(int clanId) {
		try (Handle handle = this.connect()) {
			return handle.select("SELECT Name FROM RequestsClan WHERE ClanID = ?", clanId)
					.mapTo(String.class).list();
		}
	}

	public List<String> getClanMembers(Integer clanId) {
		try (Handle handle = this.connect()) {
			return handle.select("SELECT Name FROM Members WHERE clanId = ?", clanId)
					.mapTo(String.class).list();
		}
	}

	public int getClanIdByClanName(String clanName) {
		try (Handle handle = this.connect()) {
			return handle.select("SELECT id FROM Clans WHERE name = ?", clanName)
					.mapTo(Integer.class).one();
		}
	}

	public boolean clanNameExists(String clanName) {
		try (Handle handle = this.connect()) {
			return !handle.select("SELECT ID FROM Clans WHERE Name = ?", clanName)
					.mapTo(Integer.class).findOne().isPresent();
		}
	}
}
