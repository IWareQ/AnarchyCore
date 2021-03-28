package ru.jl1mbo.AnarchyCore.Modules.Commands.Home;

import cn.nukkit.level.Position;
import ru.jl1mbo.AnarchyCore.Utils.SQLiteUtils;

public class HomeAPI {
	public static String PREFIX = "§l§7(§3Дом§7) §r";

	public static boolean isHome(String playerName) {
		return SQLiteUtils.getInteger("Homes.db", "SELECT `ID` FROM `Homes` WHERE UPPER (`Name`) = '" + playerName.toUpperCase() + "'") != -1;
	}

	public static Position getHomePosition(String playerName) {
		int x = SQLiteUtils.getInteger("Homes.db", "SELECT `X` FROM `Homes` WHERE UPPER (`Name`) = '" + playerName.toUpperCase() + "'");
		int y = SQLiteUtils.getInteger("Homes.db", "SELECT `Y` FROM `Homes` WHERE UPPER (`Name`) = '" + playerName.toUpperCase() + "'");
		int z = SQLiteUtils.getInteger("Homes.db", "SELECT `Z` FROM `Homes` WHERE UPPER (`Name`) = '" + playerName.toUpperCase() + "'");
		return new Position(x + 0.5, y, z + 0.5);
	}
}