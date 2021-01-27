package AnarchySystem.Components.CombatLogger;

import cn.nukkit.Player;
import cn.nukkit.utils.DummyBossBar;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CombatLoggerAPI {
    private static final Map<String, Long> bossBarList = new HashMap<>();
    public static ConcurrentHashMap<Player, Long> inCombat = new ConcurrentHashMap<>();

    public static void createBossBar(Player player, String text, Integer length) {
        DummyBossBar dummyBossBar = new DummyBossBar.Builder(player).text(text).length(length).build();
        player.createBossBar(dummyBossBar);
        getMap().put(player.getName(), dummyBossBar.getBossBarId());
    }

    public static void updateBossBar(Player player, String text, Integer length) {
        DummyBossBar dummyBossBar = getDummyBossBar(player);
        if (dummyBossBar != null) {
            dummyBossBar.setText(text);
            dummyBossBar.setLength(length);
        }
    }

    public static DummyBossBar getDummyBossBar(Player player) {
        Long barId = getBarId(player);
        if (barId != null) {
            return player.getDummyBossBar(barId);
        }
        return null;
    }

    public static void addCombat(Player player) {
        if (!inCombat(player)) {
            createBossBar(player, "        §l§fВы вошли в §6PvP §fрежим§7!\n\n§l§fНе выходите из игры еще §630 §fсек§7.!", 100);
        }
        inCombat.put(player, System.currentTimeMillis() / 1000 + 30);
    }

    public static boolean inCombat(Player player) {
        return inCombat.containsKey(player);
    }

    public static void removeCombat(Player player) {
        inCombat.keySet().removeIf((next) -> next.equals(player));
        removeBossBar(player);
    }

    public static Map<Player, Long> getPlayers() {
        return inCombat;
    }

    private static Long getBarId(Player player) {
        return getMap().get(player.getName());
    }

    private static Map<String, Long> getMap() {
        return bossBarList;
    }

    private static void removeBossBar(Player player) {
        Long barId = getBarId(player);
        if (barId != null) {
            player.removeBossBar(barId);
            getMap().remove(player.getName());
        }
    }
}