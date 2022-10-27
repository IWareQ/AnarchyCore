package ru.iwareq.anarchycore.module.title.manager;

import cn.nukkit.Player;
import lombok.Getter;
import lombok.Setter;
import ru.iwareq.anarchycore.module.title.TitleAPI;
import ru.iwareq.anarchycore.module.title.TitleDB;
import ru.iwareq.anarchycore.module.title.Titles;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Getter
public class TitleManager {

	private final Map<TitleAPI.Type, Set<Titles>> unlockedTitles = new HashMap<>();

	private final Player player;

	@Setter
	private Titles currentTitle = null;

	public TitleManager(Player player) {
		this.player = player;
	}

	public void addTitle(Titles title) {
		this.unlockedTitles.get(title.getType()).add(title);
	}

	public TitleManager load(TitleDB db) {
		db.register(this.player);

		this.unlockedTitles.putAll(db.getPlayerUnlockedTitles(this.player));
		this.setCurrentTitle(db.getPlayerCurrentTitle(this.player));

		return this;
	}

	public void save(TitleDB db) {
		db.setPlayerUnlockedTitles(this.player, this.unlockedTitles);
		if (this.currentTitle != null) {
			db.setPlayerCurrentTitle(this.player, this.currentTitle);
		}
	}
}
