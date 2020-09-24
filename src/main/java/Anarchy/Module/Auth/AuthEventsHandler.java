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
import cn.nukkit.level.Position;
import cn.nukkit.level.particle.FloatingTextParticle;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.SetLocalPlayerAsInitializedPacket;

public class AuthEventsHandler implements Listener {
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onDataPacketReceive(DataPacketReceiveEvent event) {
		Player player = event.getPlayer();
		DataPacket dataPacket = event.getPacket();
		if (dataPacket instanceof SetLocalPlayerAsInitializedPacket) {
			if (!player.hasPlayedBefore()) {
				player.teleport(new Position(-7, 148, 93, FunctionsAPI.SPAWN));
				player.setSpawn(new Position(-7, 148, 93, FunctionsAPI.SPAWN));
			} else {
				player.setSpawn(new Position(-7, 148, 93, FunctionsAPI.SPAWN));
			}
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
			Server.getInstance().getLogger().info("§l§7(§3Система§7) §fИгрок §6" + playerName + " §fне зарегистрирован§7! §fРегистрируем§7!");
		}
		FunctionsAPI.SPAWN.addParticle(new FloatingTextParticle(new Position(-6.50, 153, 62.50), "§l§6Прыгай в портал§7!", "§l§fПросто прыгай и начинай выживать"), player);
		FunctionsAPI.SPAWN.addParticle(new FloatingTextParticle(new Position(13.50, 150, 85.50), "§l§6Маленький приват", "\n§l§f3 §7× §f3"), player);
		FunctionsAPI.SPAWN.addParticle(new FloatingTextParticle(new Position(5.50, 150, 93.50), "§l§6Средний приват", "\n§l§f6 §7× §f6"), player);
		FunctionsAPI.SPAWN.addParticle(new FloatingTextParticle(new Position(11.50, 150, 91.50), "§l§6Большой приват", "\n§l§f10 §7× §f10"), player);
		FunctionsAPI.SPAWN.addParticle(new FloatingTextParticle(new Position(8.50, 150, 88.50), "§l§6Как приватить§7?", "\n§l§fЧтобы запривать регион§7,\n§l§fпросто установи один из блоков\n§l§fкоторые стоят рядом§7. §fКаждый блок имеет\n§l§fограниченный радиус привата§7,\n§l§fкоьорый создается вокруг блока§7!"), player);
		FunctionsAPI.SPAWN.addParticle(new FloatingTextParticle(new Position(0.50, 149, 81.50), "§l§6Самые опасные Игроки сервера", "\n§l§fСкоро§7..."), player);
		FunctionsAPI.SPAWN.addParticle(new FloatingTextParticle(new Position(-14.50, 149, 81.50), "§l§6Press F to pay respects", "\n§l§fСкоро§7..."), player);
		player.sendMessage("§l§6| §r§fДобро пожаловать на §3DEATH §fMC §7(§cАнархия§7)\n§l§6| §r§fМы в §9ВК §7- §fvk§7.§fcom§7/§3death§fanarchy §l§6| §r§fНаш сайт §7- §3death§7-§3mc§7.§3online");
		PlayerSessionManager.startPlayerSession(player);
		if (PlayerSessionManager.SCOREBOARD.contains(player.getName())) {
			HotbarTask.showScoreboard(player);
		}
		for (Player adminChat : Server.getInstance().getOnlinePlayers().values()) {
			if (adminChat.hasPermission("Command.A")) {
				adminChat.sendMessage("§l§7(§3Система§7) §r§fИгрок §6" + player.getName() + " §fзашел на сервер§7!");
			}
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
		for (Player adminChat : Server.getInstance().getOnlinePlayers().values()) {
			if (adminChat.hasPermission("Command.A")) {
				adminChat.sendMessage("§l§7(§3Система§7) §r§fИгрок §6" + player.getName() + " §fпокинул сервер§7!");
			}
		}
		event.setQuitMessage("");
	}
}