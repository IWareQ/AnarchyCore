package Anarchy.Module.CombatLogger;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cn.nukkit.Player;
import cn.nukkit.utils.DummyBossBar;

public class CombatLoggerAPI {
	public static ConcurrentHashMap<Player, Long> inCombat = new ConcurrentHashMap<>();
	public static Map<String, Long> bossBarList = new HashMap<>();
	
	public static void createBossBar(Player player, String text) {
		DummyBossBar dummyBossBar = new DummyBossBar.Builder(player).text(text).length(100).build();
		player.createBossBar(dummyBossBar);
		getMap().put(player.getName(), dummyBossBar.getBossBarId());
	}
	
	public static void addCombat(Player player) {
		if (inCombat(player)) {
			if (System.currentTimeMillis() * 1000 - inCombat.get(player) < 60) {
				createBossBar(player, "        §l§fВы вошли в §6PvP §fрежим§7!\n\n  §l§fНе выходите с сервера §630 §fсек§7.!");
			}
		} else {
			createBossBar(player, "        §l§fВы вошли в §6PvP §fрежим§7!\n\n  §l§fНе выходите с сервера §630 §fсек§7.!");
		}
		inCombat.put(player, System.currentTimeMillis());
	}
	
	public static boolean inCombat(Player player) {
		return inCombat.containsKey(player);
	}
	
	public static void removeCombat(Player player) {
		Iterator iterator = inCombat.keySet().iterator();
		while (iterator.hasNext()) {
			Player next = (Player)iterator.next();
			if (next.equals(player)) {
				iterator.remove();
			}
		}
	}
	
	public static Map<Player, Long> getPlayers() {
		return inCombat;
	}
	
	public static Long getBarId(Player player) {
		Long barId = getMap().get(player.getName());
		if (barId != null) {
			return barId;
		}
		return null;
	}
	
	public static Map<String, Long> getMap() {
		return bossBarList;
	}
	
	public static void removeBossBar(Player player) {
		Long barId = getBarId(player);
		if (barId != null) {
			player.removeBossBar(barId);
			getMap().remove(player.getName());
		}
	}
}