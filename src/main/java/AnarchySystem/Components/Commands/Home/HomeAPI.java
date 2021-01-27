package AnarchySystem.Components.Commands.Home;

import AnarchySystem.Utils.ConfigUtils;
import cn.nukkit.utils.Config;

public class HomeAPI {
    public static String PREFIX = "§l§7(§3Дом§7) §r";

    public static boolean isHome(String playerName) {
        return ConfigUtils.getHomeConfig().exists(playerName.toLowerCase());
    }

    public static void addHome(String playerName, Integer x, Integer y, Integer z) {
        Config config = ConfigUtils.getHomeConfig();
        config.set(playerName.toLowerCase() + ".X", x);
        config.set(playerName.toLowerCase() + ".Y", y);
        config.set(playerName.toLowerCase() + ".Z", z);
        config.save();
        config.reload();
    }

    public static void removeHome(String playerName) {
        Config config = ConfigUtils.getHomeConfig();
        config.remove(playerName.toLowerCase());
        config.save();
        config.reload();
    }
}