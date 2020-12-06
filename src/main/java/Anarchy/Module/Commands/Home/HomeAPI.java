package Anarchy.Module.Commands.Home;

import Anarchy.AnarchyMain;
import Anarchy.Module.Commands.Home.Utils.HomeUtils;
import cn.nukkit.utils.Config;

public class HomeAPI {
	public static String PREFIX = "§l§7(§3Дом§7) §r";

	public static boolean playerIsHome(String playerName) {
		Config config = new Config(AnarchyMain.folder + "/Homes/homes.yml", Config.YAML);
		return config.exists(playerName);
	}

	public static void setHome(String playerName, Integer X, Integer Y, Integer Z) {
		Config config = new Config(AnarchyMain.folder + "/Homes/homes.yml", Config.YAML);
		config.set(playerName + ".X", X);
		config.set(playerName + ".Y", Y);
		config.set(playerName + ".Z", Z);
		config.save();
		config.reload();
	}

	public static void delHome(String playerName) {
		Config config = new Config(AnarchyMain.folder + "/Homes/homes.yml", Config.YAML);
		config.remove(playerName);
		config.save();
		config.reload();
	}

	public static HomeUtils getHomeUtils(String playerName) {
		Config config = new Config(AnarchyMain.folder + "/Homes/homes.yml", Config.YAML);
		int X = config.getInt(playerName + ".X");
		int Y = config.getInt(playerName + ".Y");
		int Z = config.getInt(playerName + ".Z");
		return new HomeUtils(X, Y, Z);
	}
}