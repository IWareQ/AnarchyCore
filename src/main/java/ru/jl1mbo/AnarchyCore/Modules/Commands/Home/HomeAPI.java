package ru.jl1mbo.AnarchyCore.Modules.Commands.Home;

import java.util.Map;

import ru.jl1mbo.MySQLUtils.MySQLUtils;

public class HomeAPI {
	public static String PREFIX = "§l§7(§3Дом§7) §r";

	public static boolean isHome(String playerName) {
		return MySQLUtils.getInteger("SELECT `ID` FROM `Homes` WHERE UPPER (`Name`) = '" + playerName.toUpperCase() + "'") != -1;
	}

	public static Map<String, String> getHomeData(String playerName) {
		return MySQLUtils.getStringMap("SELECT * FROM `Homes` WHERE UPPER (`Name`) = '" + playerName.toUpperCase() + "'");
	}
}