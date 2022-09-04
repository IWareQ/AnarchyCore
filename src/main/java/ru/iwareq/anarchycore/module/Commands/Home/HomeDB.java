package ru.iwareq.anarchycore.module.Commands.Home;

import cn.nukkit.level.Position;
import me.hteppl.data.database.SQLiteDatabase;
import org.jdbi.v3.core.Handle;
import ru.iwareq.anarchycore.manager.WorldSystem.WorldSystemAPI;

public class HomeDB extends SQLiteDatabase {

	public HomeDB() {
		super("home");

		this.executeScheme("CREATE TABLE IF NOT EXISTS Homes\n" +
				"(\n" +
				"    ID   INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
				"    Name varchar(32) NOT NULL COLLATE NOCASE,\n" +
				"    X    int(16)     NOT NULL,\n" +
				"    Y    int(16)     NOT NULL,\n" +
				"    Z    int(16)     NOT NULL\n" +
				");");
	}

	public Position getPosition(String playerName) {
		try (Handle handle = this.connect()) {
			return handle.select("SELECT X, Y, Z FROM Homes WHERE Name = ?", playerName.toLowerCase())
					.map(rowView -> new Position(rowView.getColumn("X", Integer.class) + 0.5, rowView.getColumn("Y",
							Integer.class), rowView.getColumn("Z", Integer.class) + 0.5,
							WorldSystemAPI.Map)).one();
		}
	}

	public boolean canHome(String playerName) {
		try (Handle handle = this.connect()) {
			return handle.select("SELECT ID FROM Homes WHERE Name = ?", playerName.toLowerCase())
					.mapTo(Integer.class).findOne().isPresent();
		}
	}

	public void deleteHome(String playerName) {
		try (Handle handle = this.connect()) {
			handle.createUpdate("DELETE FROM Homes WHERE Name = :name")
					.bind("name", playerName.toLowerCase())
					.execute();
		}
	}

	public void createHome(String playerName, Position position) {
		try (Handle handle = this.connect()) {
			handle.createUpdate("INSERT INTO Homes (Name, X, Y, Z) VALUES (:name, :x, :y, :z);")
					.bind("name", playerName.toLowerCase())
					.bind("x", position.getFloorX())
					.bind("y", position.getFloorY())
					.bind("z", position.getFloorZ())
					.execute();
		}
	}
}