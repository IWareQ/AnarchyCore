package ru.iwareq.anarchycore.module.title.manager;

import cn.nukkit.Player;
import lombok.Getter;
import lombok.Setter;
import ru.iwareq.anarchycore.module.title.TitleAPI;
import ru.iwareq.anarchycore.module.title.TitleDB;
import ru.iwareq.anarchycore.module.title.Titles;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

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

	public Set<Titles> getLockedTitles() {
		Set<Titles> all = new HashSet<>();
		Titles.getAll().values().forEach(all::addAll);

		all.removeIf(titles -> unlockedTitles.get(titles.getType()).contains(titles));

		return all;
	}

	public int getAllCount() {
		AtomicInteger result = new AtomicInteger();

		unlockedTitles.keySet().forEach(type -> {
			result.addAndGet(unlockedTitles.get(type).size());
		});

		return result.get();
	}
}
