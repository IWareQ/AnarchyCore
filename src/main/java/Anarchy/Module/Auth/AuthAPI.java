package Anarchy.Module.Auth;

import Anarchy.Utils.SQLiteUtils;
import cn.nukkit.Server;

public class AuthAPI {

	public static boolean isRegistered(String playerName) {
		if (Server.getInstance().getPlayerExact(playerName) != null) {
			return true;
		} else {
			return SQLiteUtils.selectString("Auth.db", "SELECT `Username` FROM `AUTH` WHERE UPPER(`Username`) = '" + playerName.toUpperCase() + "';") != null;
		}
	}
}