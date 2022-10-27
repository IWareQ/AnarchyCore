package ru.iwareq.anarchycore.module.title;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.scheduler.Task;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.iwareq.anarchycore.module.title.manager.TitleManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TitleAPI {

	private static final Map<Player, TitleManager> MANAGERS = new ConcurrentHashMap<>();

	private static final TitleDB DB = new TitleDB();

	public static void register() {
		Server.getInstance().getScheduler().scheduleRepeatingTask(new Task() {

			@Override
			public void onRun(int i) {
				MANAGERS.values().forEach(manager -> manager.save(DB));
			}
		}, 20 * 60, true);
	}

	public static Map<Player, TitleManager> getManagers() {
		return MANAGERS;
	}

	public static TitleManager getManager(Player player) {
		if (!MANAGERS.containsKey(player)) {
			MANAGERS.put(player, new TitleManager(player).load(DB));
		}

		return MANAGERS.get(player);
	}

	public static void saveAndRemove(Player player) {
		getManager(player).save(DB);

		MANAGERS.remove(player);
	}

	@Getter
	@AllArgsConstructor
	public enum Type {

		DISNEY("Дисней"),
		ANIME("Аниме");

		private final String name;
	}
}
