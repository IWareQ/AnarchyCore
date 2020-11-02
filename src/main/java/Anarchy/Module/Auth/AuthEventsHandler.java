package Anarchy.Module.Auth;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import Anarchy.AnarchyMain;
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
import cn.nukkit.event.player.PlayerPreLoginEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.level.Position;
import cn.nukkit.level.particle.FloatingTextParticle;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.SetLocalPlayerAsInitializedPacket;
import cn.nukkit.utils.Config;

public class AuthEventsHandler implements Listener {
	File dataFileDeaths = new File(AnarchyMain.datapath + "/Deaths.yml");
	Config configDeaths = new Config(dataFileDeaths, Config.YAML);
	File dataFileKills = new File(AnarchyMain.datapath + "/Kills.yml");
	Config configKills = new Config(dataFileKills, Config.YAML);

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onDataPacketReceive(DataPacketReceiveEvent event) {
		Player player = event.getPlayer();
		DataPacket dataPacket = event.getPacket();
		if (dataPacket instanceof SetLocalPlayerAsInitializedPacket) {
			if (!player.hasPlayedBefore()) {
				player.teleport(new Position(-8.50, 51, -3.50, FunctionsAPI.SPAWN));
				player.setSpawn(new Position(-8.50, 51, -3.50, FunctionsAPI.SPAWN));
			} else {
				player.setSpawn(new Position(-8.50, 51, -3.50, FunctionsAPI.SPAWN));
			}
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerPreLogin(PlayerPreLoginEvent event) {
		Player player = event.getPlayer();
		String device = player.getLoginChainData().getDeviceModel();
		String brand = device.split("\\s+")[0];
		if (!brand.equals(brand.toUpperCase()) && !brand.equalsIgnoreCase("Iphone") && !brand.equalsIgnoreCase("playstation_4") && !brand.equalsIgnoreCase("iPhone8,4")) {
			player.close("", "§l§fНа нашем сервере запрещены §6Читы§7!\n§fВаша попытка входа с читами была отправленна Администраторам§7!");
			AnarchyMain.sendMessageToChat("Игрок " + player.getName() + " пытался зайти с ToolBox!\n\nУстройство: " + brand, 2000000004);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		String playerName = player.getName();
		String upperCase = playerName.toUpperCase();
		String ip = player.getAddress();
		String date = StringUtils.getDate();
		Integer accountID = SQLiteUtils.selectInteger("Users.db", "SELECT Account_ID FROM USERS WHERE UPPER(Username) = '" + upperCase + "';");
		if (accountID == -1) {
			SQLiteUtils.query("Users.db", "INSERT INTO USERS (Username) VALUES ('" + playerName + "');");
			SQLiteUtils.query("Auth.db", "INSERT INTO AUTH (Username, IP_Reg, Date_Reg) VALUES ('" + playerName + "', '" + ip + "', '" + date + "');");
			Server.getInstance().getLogger().info("§l§7(§6Система§7) §fИгрок §6" + playerName + " §fне зарегистрирован§7! §fРегистрируем§7!");
		}
		FunctionsAPI.SPAWN.addParticle(new FloatingTextParticle(new Position(8.50, 50, 0.50), "§l§6Заходи в портал§7!", "§l§fПросто заходи и начинай выживать"), player);
		FunctionsAPI.SPAWN.addParticle(new FloatingTextParticle(new Position(-4.50, 51, 12.50), "§l§6Маленький приват", "§l§f2 §7× §f2"), player);
		FunctionsAPI.SPAWN.addParticle(new FloatingTextParticle(new Position(-6.50, 51, 14.50), "§l§6Средний приват", "§l§f4 §7× §f4"), player);
		FunctionsAPI.SPAWN.addParticle(new FloatingTextParticle(new Position(-9.50, 51, 13.50), "§l§6Большой приват", "§l§f8 §7× §f8"), player);
		FunctionsAPI.SPAWN.addParticle(new FloatingTextParticle(new Position(-11.50, 51, 11.50), "§l§6Большой приват", "§l§f10 §7× §f10"), player);
		FunctionsAPI.SPAWN.addParticle(new FloatingTextParticle(new Position(8.50, 150, 88.50), "§l§6Как приватить§7?", "§l§fЧтобы запривать регион§7,\n§l§fпросто установи один из блоков\n§l§fкоторые стоят рядом§7. §fКаждый блок имеет\n§l§fограниченный радиус привата§7,\n§l§fкоторый создается вокруг блока§7!"), player);
		Map<String, Integer> counterKills = calculateScore(configKills);
		Map<String, Integer> counterDeaths = calculateScore(configDeaths);
		int placeKills = 1;
		for (Map.Entry<String, Integer> entry : counterKills.entrySet()) {
			if (placeKills <= 10) {
				String title = "§l§6" + placeKills + "§7. §f" + entry.getKey() + " §7- §6" + entry.getValue() + " §fkill§7(§fs§7)";
				FunctionsAPI.SPAWN.addParticle(new FloatingTextParticle(new Position(8.50, 52.50 - placeKills * 0.3, 6.50), title), player);
				placeKills++;
			}
		}
		int placeDeaths = 1;
		for (Map.Entry<String, Integer> entry : counterDeaths.entrySet()) {
			if (placeDeaths <= 10) {
				String title = "§l§6" + placeDeaths + "§7. §f" + entry.getKey() + " §7- §6" + entry.getValue() + " §fdeath§7(§fs§7)";
				FunctionsAPI.SPAWN.addParticle(new FloatingTextParticle(new Position(8.50, 52.50 - placeDeaths * 0.3, -5.50), title), player);
				placeDeaths++;
			}
		}
		FunctionsAPI.SPAWN.addParticle(new FloatingTextParticle(new Position(8.50, 52.50, 6.50), "§l§6Самые опасные Игроки сервера"), player);
		FunctionsAPI.SPAWN.addParticle(new FloatingTextParticle(new Position(8.50, 52.50, -5.50), "§l§6Press F to pay respects"), player);
		player.sendMessage("§l§6• §r§fДобро пожаловать на §3DEATH §fMC §7(§cАнархия§7)\n§l§6• §r§fМы в §9ВК §7- §fvk§7.§fcom§7/§6death§fanarchy §l§6| §r§fНаш сайт §7- §6death§7-§6mc§7.§6online");
		PlayerSessionManager.startPlayerSession(player);
		PermissionsAPI.updateTag(player);
		PermissionsAPI.updatePermissions(player);
		player.setCheckMovement(false);
		SQLiteUtils.query("Auth.db", "UPDATE AUTH SET IP_Last = '" + ip + "', Date_Last = '" + date + "' WHERE UPPER(Username) = '" + upperCase + "';");
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

	private Map<String, Integer> calculateScore(Config config) {
		Map<String, Integer> map = new HashMap<>();
		for (Map.Entry<String, Object> entry : config.getAll().entrySet()) {
			map.put(entry.getKey(), (Integer)entry.getValue());
		}
		Map<String, Integer> sorted = map;
		sorted = map.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2)->e2, LinkedHashMap::new));
		return sorted;
	}
}