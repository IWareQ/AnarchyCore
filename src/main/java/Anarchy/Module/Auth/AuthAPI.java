package Anarchy.Module.Auth;

import Anarchy.Utils.SQLiteUtils;
import cn.nukkit.Server;

public class AuthAPI {

	public static boolean isRegistered(String playerName) {
		if (Server.getInstance().getPlayerExact(playerName) != null) {
			return true;
		} else {
			return SQLiteUtils.selectString("SELECT `Username` FROM `Auth` WHERE UPPER(`Username`) = '" + playerName.toUpperCase() + "';") != null;
		}
	}
}