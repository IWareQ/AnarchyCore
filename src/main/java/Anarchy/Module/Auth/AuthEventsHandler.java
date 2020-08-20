package Anarchy.Module.Auth;

import Anarchy.Manager.Functions.FunctionsAPI;
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
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.level.Location;
import cn.nukkit.level.Position;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.SetLocalPlayerAsInitializedPacket;

public class AuthEventsHandler implements Listener {
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onDataPacketReceive(DataPacketReceiveEvent event) {
		Player player = event.getPlayer();
		DataPacket dataPacket = event.getPacket();
		if (!(dataPacket instanceof SetLocalPlayerAsInitializedPacket)) return;
		if (!(player.hasPlayedBefore())) {
			player.teleport(new Location(-7, 148, 93, FunctionsAPI.WORLD2));
			player.setSpawn(new Position(-7, 148, 93, FunctionsAPI.WORLD2));
		}
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		String playerName = player.getName();
		String upperCase = playerName.toUpperCase();
		String ip = player.getAddress();
		String date = StringUtils.getDate();
		Integer accountID = SQLiteUtils.selectInteger("Users.db", "SELECT `Account_ID` FROM `USERS` WHERE UPPER(`Username`) = \'" + upperCase + "\';");
		if (accountID == -1) {
			SQLiteUtils.query("Users.db", "INSERT INTO `USERS` (`Username`) VALUES (\'" + playerName + "\');");
			SQLiteUtils.query("Auth.db", "INSERT INTO `AUTH` (`Username`, `IP_Reg`, `Date_Reg`) VALUES (\'" + playerName + "\', \'" + ip + "\', \'" + date + "\');");
			Server.getInstance().getLogger().info("§l§7(§3Система§7) §fИгрок §e" + playerName + " §fне зарегистрирован§7! §fРегистрируем§7!");
			player.sendMessage("Новый игрок кек");
		}
		player.sendMessage("§l§e| §r§fДобро пожаловать на §3DEATH §fMC §7(§cАнархия§7)\n§l§e| §r§fМы в §9ВК §7- §fvk§7.§fcom§7/§3death§fanarchy §l§e| §r§fНаш сайт §7- §3deathmc§7.§3mcpetrade§7.§3ru");
		PlayerSessionManager.startPlayerSession(player);
		if (PlayerSessionManager.SCOREBOARD.contains(player.getName())) {
			HotbarTask.showScoreboard(player);
		}
		PermissionsAPI.updateTag(player);
		PermissionsAPI.updatePermissions(player);
		player.setCheckMovement(false);
		SQLiteUtils.query("Auth.db", "UPDATE `AUTH` SET `IP_Last` = \'" + ip + "\', `Date_Last` = \'" + date + "\' WHERE UPPER(`Username`) = \'" + upperCase + "\';");
		event.setJoinMessage("");
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if (PlayerSessionManager.hasPlayerSession(player)) {
			PlayerSessionManager.stopPlayerSession(player);
		}
		event.setQuitMessage("");
	}
}