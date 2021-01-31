package ru.jl1mbo.AnarchyCore.LoginPlayerHandler.Authorization;

import cn.nukkit.Server;
import cn.nukkit.plugin.PluginManager;
import cn.nukkit.utils.Config;
import ru.jl1mbo.AnarchyCore.Main;
import ru.jl1mbo.AnarchyCore.LoginPlayerHandler.Authorization.EventsListener.PlayerJoinListener;
import ru.jl1mbo.AnarchyCore.LoginPlayerHandler.Authorization.EventsListener.PlayerLocallyInitializedListener;
import ru.jl1mbo.AnarchyCore.LoginPlayerHandler.Authorization.EventsListener.PlayerQuitListener;
import ru.jl1mbo.AnarchyCore.Utils.ConfigUtils;

public class AuthorizationAPI {

	public static void register() {
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