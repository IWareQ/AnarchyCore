package AnarchySystem.Components.EconomyAPI;

import AnarchySystem.Main;
import AnarchySystem.Components.EconomyAPI.EventsListener.PlayerJoinListener;
import AnarchySystem.Utils.ConfigUtils;
import cn.nukkit.Server;
import cn.nukkit.plugin.PluginManager;
import cn.nukkit.utils.Config;

public class EconomyAPI {
	public static String PREFIX = "§l§7(§3Экономика§7) §r";

	public static void registerEvents() {
		PluginManager pluginManager = Server.getInstance().getPluginManager();
		pluginManager.registerEvents(new PlayerJoinListener(), Main.getInstance());
	}

	public static boolean isRegister(String playerName) {
		return ConfigUtils.getEconomyConfig().exists(playerName.toLowerCase());
	}

	public static void registerPlayer(String playerName) {
		Config config = ConfigUtils.getEconomyConfig();
		config.set(playerName.toLowerCase(), 0.0);
		config.save();
		config.reload();
	}

	public static Double myMoney(String playerName) {
		return ConfigUtils.getEconomyConfig().getDouble(playerName.toLowerCase(), 0.0);
	}

	public static void setMoney(String playerName, Double count) {
		Config config = ConfigUtils.getEconomyConfig();
		config.set(playerName.toLowerCase(), count);
		config.save();
		config.reload();
	}

	public static void addMoney(String playerName, Double count) {
		setMoney(playerName, myMoney(playerName) + count);
	}

	public static void reduceMoney(String playerName, Double count) {
		setMoney(playerName, myMoney(playerName) - count);
	}
}