package ru.iwareq.anarchycore.module.AdminSystem;

import cn.nukkit.Server;
import cn.nukkit.level.Position;
import me.hteppl.data.database.SQLiteDatabase;
import org.jdbi.v3.core.Handle;

public class AdminDB extends SQLiteDatabase {

	public AdminDB() {
		super("admin");

		this.executeScheme("CREATE TABLE IF NOT EXISTS Bans\n" +
				"(\n" +
				"    ID     INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
				"    Name   varchar(32) NOT NULL COLLATE NOCASE,\n" +
				"    Reason TEXT        NOT NULL,\n" +
				"    Time   int(11)     NOT NULL\n" +
				");");

		this.executeScheme("CREATE TABLE IF NOT EXISTS Mutes\n" +
				"(\n" +
				"    ID     INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
				"    Name   varchar(32) NOT NULL COLLATE NOCASE,\n" +
				"    Reason TEXT        NOT NULL,\n" +
				"    Time   int(11)     NOT NULL\n" +
				");");

		this.executeScheme("CREATE TABLE IF NOT EXISTS Spectates\n" +
				"(\n" +
				"    ID     INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
				"    Name   varchar(32) NOT NULL COLLATE NOCASE,\n" +
				"    Target varchar(32) NOT NULL COLLATE NOCASE,\n" +
				"    World  varchar(32) NOT NULL COLLATE NOCASE,\n" +
				"    X      int(16)     NOT NULL,\n" +
				"    Y      int(16)     NOT NULL,\n" +
				"    Z      int(16)     NOT NULL,\n" +
				"    nbtHex TEXT        NOT NULL\n" +
				");");
	}

	public boolean isBanned(String playerName) {
		try (Handle handle = this.connect()) {
			return handle.select("SELECT ID FROM Bans WHERE Name = ?;", playerName)
					.mapTo(Integer.class).findOne().isPresent();
		}
	}

	public boolean isMuted(String playerName) {
		try (Handle handle = this.connect()) {
			return handle.select("SELECT ID FROM Mutes WHERE Name = ?;", playerName)
					.mapTo(Integer.class).findOne().isPresent();
		}
	}

	public void ban(String playerName, String reason, long endBan) {
		try (Handle handle = this.connect()) {
			handle.createUpdate("INSERT INTO Bans (Name, Reason, Time) VALUES (:name, :reason, :time);")
					.bind("name", playerName)
					.bind("reason", reason)
					.bind("time", endBan)
					.execute();
		}
	}

	public void mute(String playerName, String reason, long endMute) {
		try (Handle handle = this.connect()) {
			handle.createUpdate("INSERT INTO Mutes (Name, Reason, Time) VALUES (:name, :reason, :time);")
					.bind("name", playerName)
					.bind("reason", reason)
					.bind("time", endMute)
					.execute();
		}
	}

	public void unban(String playerName) {
		try (Handle handle = this.connect()) {
			handle.createUpdate("DELETE FROM Bans WHERE Name = :name;")
					.bind("name", playerName)
					.execute();
		}
	}

	public void unmute(String playerName) {
		try (Handle handle = this.connect()) {
			handle.createUpdate("DELETE FROM Mutes WHERE Name = :name;")
					.bind("name", playerName)
					.execute();
		}
	}

	public long getBanTime(String playerName) {
		try (Handle handle = this.connect()) {
			return handle.select("SELECT Time FROM Bans WHERE Name = ?;", playerName)
					.mapTo(Long.class).one();
		}
	}

	public String getBanReason(String playerName) {
		try (Handle handle = this.connect()) {
			return handle.select("SELECT Reason FROM Bans WHERE Name = ?;", playerName)
					.mapTo(String.class).one();
		}
	}

	public long getMuteTime(String playerName) {
		try (Handle handle = this.connect()) {
			return handle.select("SELECT Time FROM Mutes WHERE Name = ?;", playerName)
					.mapTo(Long.class).one();
		}
	}

	public String getMuteReason(String playerName) {
		try (Handle handle = this.connect()) {
			return handle.select("SELECT Reason FROM Mutes WHERE Name = ?;", playerName)
					.mapTo(String.class).one();
		}
	}

	public void spectate(String playerName, String targetName, String worldName, int floorX, int floorY, int floorZ, String nbtHex) {
		try (Handle handle = this.connect()) {
			handle.createUpdate("INSERT INTO Spectates (Name, Target, World, X, Y, Z, NbtHex) VALUES (:name, " +
							":targetName, :worldName, :x, :y, :z, :nbtHex);")
					.bind("name", playerName)
					.bind("targetName", targetName)
					.bind("worldName", worldName)
					.bind("x", floorX)
					.bind("y", floorY)
					.bind("z", floorZ)
					.bind("nbtHex", nbtHex)
					.execute();
		}
	}

	public void spectateTargetUpdate(String playerName, String targetName) {
		try (Handle handle = this.connect()) {
			handle.createUpdate("UPDATE Spectates SET Target = :targetName WHERE Name = :playerName;")
					.bind("name", playerName)
					.bind("targetName", targetName)
					.execute();
		}
	}

	public String getSpectateTarget(String playerName) {
		try (Handle handle = this.connect()) {
			return handle.select("SELECT Target FROM Spectates WHERE Name = ?;", playerName)
					.mapTo(String.class).one();
		}
	}

	public void removeSpectate(String playerName) {
		try (Handle handle = this.connect()) {
			handle.createUpdate("DELETE FROM Spectates WHERE Name = :playerName;")
					.bind("playerName", playerName)
					.execute();
		}
	}

	public Position getSpectateStartPosition(String playerName) {
		try (Handle handle = this.connect()) {
			return handle.select("SELECT X, Y, Z, World FROM Spectates WHERE Name = ?;", playerName)
					.map(rowView -> {
						int x = rowView.getColumn("X", Integer.class);
						int y = rowView.getColumn("Y", Integer.class);
						int z = rowView.getColumn("Z", Integer.class);

						String worldName = rowView.getColumn("World", String.class);

						return new Position(x, y, z, Server.getInstance().getLevelByName(worldName));
					}).one();
		}
	}

	public String getSpectateNbtHex(String playerName) {
		try (Handle handle = this.connect()) {
			return handle.select("SELECT NbtHex FROM Spectates WHERE Name = ?;", playerName)
					.mapTo(String.class).one();
		}
	}
}
