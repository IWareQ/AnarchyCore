package Anarchy.Module.Auth;

import Anarchy.Manager.Sessions.PlayerSessionManager;
import Anarchy.Module.Permissions.PermissionsAPI;
import Anarchy.Task.HotbarTask;
import Anarchy.Utils.SQLiteUtils;
import Anarchy.Utils.StringUtils;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;

public class AuthEventsHandler implements Listener {
	public static Level WORLD2;
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		String playerName = player.getName();
		String upperCase = playerName.toUpperCase();
		String ip = player.getAddress();
		String date = StringUtils.getDate();
		WORLD2 = Server.getInstance().getLevelByName("world2");
		Integer accountID = SQLiteUtils.selectInteger("Users.db", "SELECT `Account_ID` FROM `USERS` WHERE UPPER(`Username`) = \'" + upperCase + "\';");
		if (accountID == -1) {
			SQLiteUtils.query("Users.db", "INSERT INTO `USERS` (`Username`) VALUES (\'" + playerName + "\');");
			SQLiteUtils.query("Auth.db", "INSERT INTO `AUTH` (`Username`, `IP_Reg`, `Date_Reg`) VALUES (\'" + playerName + "\', \'" + ip + "\', \'" + date + "\');");
			player.setSpawn(new Position(-7, 146, 93, WORLD2));
		}
		player.sendMessage("§l§e| §r§fДобро пожаловать на §6Hall§fMine Project §7(§cАнархия§7)\n §fМы в §9ВК §7- §fvk§7.§fcom§7/§6hall§fmine §l§e| §r§fНаш сайт §7- §fshop§7.§6hall§fmine§7.§fml");
		PlayerSessionManager.startPlayerSession(player);
		player.setSpawn(new Position(-7, 146, 93, WORLD2));
		if (PlayerSessionManager.SCOREBOARD.contains(player.getName())) {
			HotbarTask.showScoreboard(player);
		}
		PermissionsAPI.updateTag(player);
		PermissionsAPI.updatePermissions(player);
		player.setCheckMovement(false);
		SQLiteUtils.query("Auth.db", "UPDATE `AUTH` SET `IP_Last` = \'" + ip + "\', `Date_Last` = \'" + date + "\' WHERE UPPER(`Username`) = \'" + upperCase + "\';");
		event.setJoinMessage("");
		player.setSpawn(new Position(-7, 146, 93, WORLD2));
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if (PlayerSessionManager.hasPlayerSession(player)) {
			PlayerSessionManager.stopPlayerSession(player);
		}
		player.setSpawn(new Position(-7, 146, 93, WORLD2));
		event.setQuitMessage("");
	}
}