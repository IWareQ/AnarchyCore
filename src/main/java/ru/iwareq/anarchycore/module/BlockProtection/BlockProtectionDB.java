package ru.iwareq.anarchycore.module.BlockProtection;

import cn.nukkit.Player;
import cn.nukkit.level.Location;
import cn.nukkit.level.Position;
import me.hteppl.data.database.SQLiteDatabase;
import org.jdbi.v3.core.Handle;

import java.util.List;

public class BlockProtectionDB extends SQLiteDatabase {

	public BlockProtectionDB() {
		super("regions");

		this.executeScheme("CREATE TABLE IF NOT EXISTS Regions\n" +
				"(\n" +
				"    ID    INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
				"    Name  varchar(32) NOT NULL COLLATE NOCASE,\n" +
				"\n" +
				"    MainX INT         NOT NULL,\n" +
				"    MainY INT         NOT NULL,\n" +
				"    MainZ INT         NOT NULL,\n" +
				"\n" +
				"    Pos1X INT         NOT NULL,\n" +
				"    Pos1Y INT         NOT NULL,\n" +
				"    Pos1Z INT         NOT NULL,\n" +
				"\n" +
				"    Pos2X INT         NOT NULL,\n" +
				"    Pos2Y INT         NOT NULL,\n" +
				"    Pos2Z INT         NOT NULL\n" +
				");");

		this.executeScheme("CREATE TABLE IF NOT EXISTS Members\n" +
				"(\n" +
				"    ID       INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
				"    Name     varchar(32) NOT NULL COLLATE NOCASE,\n" +
				"    RegionID INT         NOT NULL\n" +
				");");
	}

	public void createRegion(Player player, int mainX, int mainY, int mainZ, int pos1X, int pos1Y, int pos1Z,
	                         int pos2X, int pos2Y, int pos2Z) {
		try (Handle handle = this.connect()) {
			handle.createUpdate("INSERT INTO Regions (Name, MainX, MainY, MainZ, Pos1X, Pos1Y, Pos1Z, Pos2X, Pos2Y, " +
							"Pos2Z) VALUES (:name, :mainX, :mainY, :mainZ, :pos1X, :pos1Y, :pos1Z, :pos2X, :pos2Y, " +
							":pos2Z)")
					.bind("name", player.getName().toLowerCase())
					.bind("mainX", mainX)
					.bind("mainY", mainY)
					.bind("mainZ", mainZ)
					.bind("pos1X", pos1X)
					.bind("pos1Y", pos1Y)
					.bind("pos1Z", pos1Z)
					.bind("pos2X", pos2X)
					.bind("pos2Y", pos2Y)
					.bind("pos2Z", pos2Z)
					.execute();
		}
	}

	public boolean isRegionMember(String playerName, int regionId) {
		try (Handle handle = this.connect()) {
			return handle.createQuery("SELECT ID FROM Members WHERE name = :name AND regionId = :regionId")
					.bind("name", playerName.toLowerCase())
					.bind("regionId", regionId)
					.mapTo(Integer.class).findOne().isPresent();
		}
	}

	public String getRegionOwner(int regionId) {
		try (Handle handle = this.connect()) {
			return handle.createQuery("SELECT Name FROM Regions WHERE ID = :id")
					.bind("id", regionId)
					.mapTo(String.class).one();
		}
	}

	public int getRegionIDByPosition(Position position) {
		try (Handle handle = this.connect()) {
			return handle.createQuery("SELECT ID FROM Regions WHERE (Pos1X <= :posX AND :posX <= Pos2X) AND (Pos1Y <=" +
							" :posY AND :posY <= Pos2Y) AND (Pos1Z <= :posZ AND :posZ <= Pos2Z)")
					.bind("posX", position.getFloorX())
					.bind("posY", position.getFloorY())
					.bind("posZ", position.getFloorZ())
					.mapTo(Integer.class).findOne().orElse(-1);
		}
	}

	public boolean canCreateRegion(int minX, int maxX, int minY, int maxY, int minZ, int maxZ) {
		try (Handle handle = this.connect()) {
			return !handle.createQuery("SELECT ID FROM Regions WHERE Pos2X >= :minX AND Pos1X <= :maxX AND Pos2Y >= " +
							":minY AND Pos1Y <= :maxY AND Pos2Z >= :minZ AND Pos1Z <= :maxZ")
					.bind("minX", minX)
					.bind("maxX", maxX)

					.bind("minY", minY)
					.bind("maxY", maxY)

					.bind("minZ", minZ)
					.bind("maxZ", maxZ)
					.mapTo(Integer.class).findOne().isPresent();
		}
	}

	public Location getRegionBlockLocation(int regionId) {
		try (Handle handle = this.connect()) {
			int mainX = handle.createQuery("SELECT MainX FROM Regions WHERE ID = :id")
					.bind("id", regionId)
					.mapTo(Integer.class).one();
			int mainY = handle.createQuery("SELECT MainY FROM Regions WHERE ID = :id")
					.bind("id", regionId)
					.mapTo(Integer.class).one();
			int mainZ = handle.createQuery("SELECT MainZ FROM Regions WHERE ID = :id")
					.bind("id", regionId)
					.mapTo(Integer.class).one();

			return new Location(mainX, mainY, mainZ);
		}
	}

	public int getRegionsCount(String playerName) {
		try (Handle handle = this.connect()) {
			return handle.createQuery("SELECT COUNT(*) FROM Regions WHERE Name = :name")
					.bind("name", playerName.toLowerCase())
					.mapTo(Integer.class).one();
		}
	}

	public List<String> getRegionMembers(int regionId) {
		try (Handle handle = this.connect()) {
			return handle.createQuery("SELECT Name FROM Members WHERE RegionID = :regionId")
					.bind("regionId", regionId)
					.mapTo(String.class).list();
		}
	}

	public void deleteRegion(int regionId) {
		try (Handle handle = this.connect()) {
			handle.createUpdate("DELETE FROM Regions WHERE ID = :id")
					.bind("id", regionId).execute();

			handle.createUpdate("DELETE FROM Members WHERE RegionID = :regionId")
					.bind("regionId", regionId).execute();
		}
	}

	public void removeMember(String targetName, int regionId) {
		try (Handle handle = this.connect()) {
			handle.createUpdate("DELETE FROM Members WHERE Name = :name AND RegionID = :regionId")
					.bind("name", targetName.toLowerCase())
					.bind("regionId", regionId)
					.execute();
		}
	}

	public List<Integer> getRegionsByName(String name) {
		try (Handle handle = this.connect()) {
			return handle.select("SELECT ID FROM Regions WHERE Name = ?", name.toLowerCase())
					.mapTo(Integer.class).list();
		}
	}

	public List<Integer> getRegionsMembers(String name) {
		try (Handle handle = this.connect()) {
			return handle.select("SELECT RegionID FROM Members WHERE Name = ?", name.toLowerCase())
					.mapTo(Integer.class).list();
		}
	}

	public void addMember(String targetName, int regionID) {
		try (Handle handle = this.connect()) {
			handle.createUpdate("INSERT INTO Members (Name, RegionID) VALUES (:name, :regionId)")
					.bind("name", targetName.toLowerCase())
					.bind("regionId", regionID)
					.execute();
		}
	}
}
