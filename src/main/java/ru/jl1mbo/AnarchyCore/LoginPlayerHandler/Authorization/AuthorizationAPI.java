package ru.jl1mbo.AnarchyCore.LoginPlayerHandler.Authorization;

import java.util.HashMap;

import cn.nukkit.Server;
import cn.nukkit.plugin.PluginManager;
import cn.nukkit.utils.Config;
import ru.jl1mbo.AnarchyCore.Main;
import ru.jl1mbo.AnarchyCore.LoginPlayerHandler.Authorization.EventsListener.PlayerJoinListener;
import ru.jl1mbo.AnarchyCore.LoginPlayerHandler.Authorization.EventsListener.PlayerLocallyInitializedListener;
import ru.jl1mbo.AnarchyCore.LoginPlayerHandler.Authorization.EventsListener.PlayerQuitListener;
import ru.jl1mbo.AnarchyCore.Utils.ConfigUtils;

public class AuthorizationAPI {
	public static HashMap<String, Long> playerTime = new HashMap<>();
	private static Config config;

	public static void register() {
		config = ConfigUtils.getAuthorizationConfig();
		PluginManager pluginManager = Server.getInstance().getPluginManager();
		pluginManager.registerEvents(new PlayerJoinListener(), Main.getInstance());
		pluginManager.registerEvents(new PlayerLocallyInitializedListener(), Main.getInstance());
		pluginManager.registerEvents(new PlayerQuitListener(), Main.getInstance());
	}

	public static boolean isRegister(String playerName) {
		return config.exists(playerName.toLowerCase());
	}

	public static void registerPlayer(String playerName, String ip, String date) {
		config.set(playerName.toLowerCase() + ".reg_IP", ip);
		config.set(playerName.toLowerCase() + ".reg_DATE", date);
		config.set(playerName.toLowerCase() + ".last_IP", ip);
		config.set(playerName.toLowerCase() + ".last_DATE", date);
		config.set(playerName.toLowerCase() + ".gameTime", 0L);
		config.save();
		config.reload();
	}

	public static void updatePlayerInfo(String playerName, Long gameTime, String ip, String date) {
		config.set(playerName.toLowerCase() + ".last_IP", ip);
		config.set(playerName.toLowerCase() + ".last_DATE", date);
		config.set(playerName.toLowerCase() + ".gameTime", getGameTime(playerName) + gameTime);
		config.save();
		config.reload();
	}

	public static long getGameTime(String playerName) {
		return config.getLong(playerName.toLowerCase() + ".gameTime", 0L);
	}
}