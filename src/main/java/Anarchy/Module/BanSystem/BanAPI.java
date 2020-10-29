package Anarchy.Module.BanSystem;

import Anarchy.Utils.SQLiteUtils;
import cn.nukkit.Player;

public class BanAPI {
	
	public static void banPlayer(Player player, Player target, String reason, Long banTime) {
		SQLiteUtils.query("BanList.db", "INSERT INTO BANPLAYERS (Username, Banned, Reason, banTime) VALUES ('" + target.getName() + "', '" + player.getName() + "', '" + reason + "', '" + banTime +"');");
	}
	public static void unbanPlayer(Player player, Player target, String reason, Long banTime) {
		SQLiteUtils.query("BanList.db", "INSERT INTO BANPLAYERS (Username, Banned, Reason, banTime) VALUES ('" + target.getName() + "', '" + player.getName() + "', '" + reason + "', '" + banTime +"');");
	}
}