package AnarchySystem.Components.Authorization;

import AnarchySystem.Main;
import AnarchySystem.Components.Authorization.EventsListener.PlayerJoinListener;
import AnarchySystem.Components.Authorization.EventsListener.PlayerLocallyInitializedListener;
import AnarchySystem.Components.Authorization.EventsListener.PlayerQuitListener;
import AnarchySystem.Utils.ConfigUtils;
import cn.nukkit.Server;
import cn.nukkit.plugin.PluginManager;
import cn.nukkit.utils.Config;

public class AuthorizationAPI {
	
	public static void registerEvents() {
		PluginManager pluginManager = Server.getInstance().getPluginManager();
		pluginManager.registerEvents(new PlayerJoinListener(), Main.getInstance());
		pluginManager.registerEvents(new PlayerLocallyInitializedListener(), Main.getInstance());
		pluginManager.registerEvents(new PlayerQuitListener(), Main.getInstance());
	}

	public static boolean isRegister(String playerName) {
		return ConfigUtils.getAuthorizationConfig().exists(playerName.toLowerCase());
	}

	public static void registerPlayer(String playerName, String ip, String date) {
		Config config = ConfigUtils.getAuthorizationConfig();
		config.set(playerName.toLowerCase() + ".registerIP", ip);
		config.set(playerName.toLowerCase() + ".registerDATE", date);
		config.save();
		config.reload();
	}

	public static void updateGameTime(String playerName, Long gameTime) {
		Config config = ConfigUtils.getAuthorizationConfig();
		config.set(playerName.toLowerCase() + ".gameTime", (ConfigUtils.getAuthorizationConfig().getLong(playerName) + gameTime));
		config.save();
		config.reload();
	}

	public static long getGameTime(String playerName) {
		return ConfigUtils.getAuthorizationConfig().getLong(playerName.toLowerCase() + ".gameTime", 0L);
	}
}