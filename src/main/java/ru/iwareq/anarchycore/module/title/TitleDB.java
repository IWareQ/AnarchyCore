package ru.iwareq.anarchycore.module.title;

import cn.nukkit.Player;
import me.hteppl.data.database.SQLiteDatabase;
import org.jdbi.v3.core.Handle;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;

public class TitleDB extends SQLiteDatabase {

	public TitleDB() {
		super("Title");

		this.executeScheme("CREATE TABLE IF NOT EXISTS Title\n" +
				"(\n" +
				"    ID           INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
				"    Owner        VARCHAR(32) NOT NULL COLLATE NOCASE,\n" +
				"    CurrentTitle TEXT DEFAULT NULL COLLATE NOCASE,\n" +
				"    Titles       TEXT DEFAULT NULL COLLATE NOCASE,\n" +
				"    UNIQUE (Owner)\n" +
				");");
	}

	public void register(Player player) {
		try (Handle handle = this.connect()) {
			handle.createUpdate("INSERT OR IGNORE INTO Title (Owner) VALUES (:owner);")
					.bind("owner", player.getName()).execute();
		}
	}

	public Map<TitleAPI.Type, Set<Titles>> getPlayerUnlockedTitles(Player player) {
		Map<TitleAPI.Type, Set<Titles>> result = new HashMap<>();
		for (TitleAPI.Type type : TitleAPI.Type.values()) {
			result.put(type, new HashSet<>());
		}

		try (Handle handle = this.connect()) {
			String allTitles = handle.createQuery("SELECT Titles FROM Title WHERE Owner = :owner")
					.bind("owner", player.getName())
					.mapTo(String.class).findOne().orElse("");

			if (!allTitles.isEmpty()) {
				String[] titles = allTitles.split(";");

				for (String title : titles) {
					Titles titless = Titles.valueOf(title);

					result.get(titless.getType()).add(titless);
				}
			}
		}

		return result;
	}

	public void setPlayerUnlockedTitles(Player player, Map<TitleAPI.Type, Set<Titles>> unlockedPrefixes) {
		try (Handle handle = this.connect()) {
			StringJoiner joiner = new StringJoiner(";");
			unlockedPrefixes.forEach((type, titlesSet) -> {
				for (Titles titles : titlesSet) {
					joiner.add(titles.name());
				}
			});

			handle.createUpdate("UPDATE Title SET Titles = :titles WHERE Owner = :owner")
					.bind("titles", joiner.toString())
					.bind("owner", player.getName()).execute();
		}
	}

	public void setPlayerCurrentTitle(Player player, Titles prefix) {
		try (Handle handle = this.connect()) {
			handle.createUpdate("UPDATE Title SET CurrentTitle = :currentTitle WHERE Owner = :owner")
					.bind("currentTitle", prefix.name())
					.bind("owner", player.getName()).execute();
		}
	}

	@SuppressWarnings("OptionalIsPresent")
	public Titles getPlayerCurrentTitle(Player player) {
		try (Handle handle = this.connect()) {
			Optional<String> title = handle.createQuery("SELECT CurrentTitle FROM Title WHERE Owner = :owner")
					.bind("owner", player.getName())
					.mapTo(String.class).findFirst();

			if (title.isPresent()) {
				return Titles.valueOf(title.get());
			}

			return null;
		}
	}
}
