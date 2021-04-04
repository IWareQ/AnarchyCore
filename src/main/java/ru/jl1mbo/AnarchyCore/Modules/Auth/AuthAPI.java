package ru.jl1mbo.AnarchyCore.Modules.Auth;

import ru.jl1mbo.MySQLUtils.MySQLUtils;

public class AuthAPI {

	public static boolean isRegister(String playerName) {
		return MySQLUtils.getInteger("SELECT `ID` FROM `Auth` WHERE UPPER (`Name`) = '" + playerName.toUpperCase() + "'") != -1;
	}

	public static long getGameTime(String playerName) {
		long gameTime = MySQLUtils.getLong("SELECT `GameTime` FROM `Users` WHERE UPPER (`Name`) = '" + playerName.toUpperCase() + "'");
		if (gameTime != -1L) {
			return gameTime;
		} else {
			return 0L;
		}
	}
}