package Anarchy.Module.Auth;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import Anarchy.AnarchyMain;
import Anarchy.Functions.FunctionsAPI;
import Anarchy.Module.BanSystem.BanSystemAPI;
import Anarchy.Module.BanSystem.Utils.BanUtils;
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
	Config deathsConfig = new Config(AnarchyMain.folder + "/Deaths.yml", Config.YAML);
	Config killsConfig = new Config(AnarchyMain.folder + "/Kills.yml", Config.YAML);
	private Long startPlayerTime;

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onDataPacketReceive(DataPacketReceiveEvent event) {
		Player player = event.getPlayer();
		DataPacket dataPacket = event.getPacket();
		if (dataPacket instanceof SetLocalPlayerAsInitializedPacket) {
			if (!player.hasPlayedBefore()) {
				player.teleport(new Position(-8.5, 51, -3.5, FunctionsAPI.SPAWN));
				player.setSpawn(new Position(-8.5, 51, -3.5, FunctionsAPI.SPAWN));
			} else {
				player.setSpawn(new Position(-8.5, 51, -3.5, FunctionsAPI.SPAWN));
			}
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerPreLogin(PlayerPreLoginEvent event) {
		Player player = event.getPlayer();
		String device = player.getLoginChainData().getDeviceModel();
		String brand = device.split("\\s+")[0];
		if (!brand.equals(brand.toUpperCase()) && !brand.equalsIgnoreCase("Iphone") && !brand.equalsIgnoreCase("playstation_4") && !brand.equalsIgnoreCase("iPhone8,4") && !brand.equalsIgnoreCase("iPad7,3") && !brand.equalsIgnoreCase("iPhone8,1") && !brand.equalsIgnoreCase("iPhone9,4") && !brand.equalsIgnoreCase("iPhone6,2") && !brand.equalsIgnoreCase("iPad7,4") && !brand.equalsIgnoreCase("iPad11,1") && !brand.equalsIgnoreCase("iPhone7,2") && !brand.equalsIgnoreCase("iPad4,1")) {
			player.close("", "§l§fНа нашем сервере запрещены §6Читы§7!\n§fЕсли Вы заходите без читов§7, §fно видите это окно§7, §fсообщите это в ВК - §7@§6extranons§7!");
			AnarchyMain.sendMessageToChat("Игрок " + player.getName() + " пытался зайти с ToolBox!\n\nУстройство: " + brand, 2000000001);
		}
		BanUtils banUtils = BanSystemAPI.getBan(player.getName());
		if (BanSystemAPI.playerIsBanned(player.getName())) {
			if (banUtils.getTime() != -1) {
				if (banUtils.getTime() < System.currentTimeMillis() / 1000L) {
					BanSystemAPI.unBanPlayer(player.getName());
					return;
				} else {
					player.close("", "§l§fУвы§7, §fно Вас §6временно §fзаблокировали§7!\n§fВас заблокировал§7: §6" + banUtils.getBanner() + "\n§fПричина блокировки§7: §6" + banUtils.getReason() + "\n§fРазбан через§7: §6" + ((banUtils.getTime() - System.currentTimeMillis() / 1000L) / 86400) + " §fд§7. §6" + ((banUtils.getTime() - System.currentTimeMillis() / 1000L) / 3600 % 24) + " §fч§7. §6" + ((banUtils.getTime() - System.currentTimeMillis() / 1000L) / 60 % 60) + " §fмин§7.");
				}
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
		Integer accountID = SQLiteUtils.selectInteger("Users.db", "SELECT Account_ID FROM USERS WHERE UPPER(Username) = \'" + upperCase + "\';");
		if (accountID == -1) {
			SQLiteUtils.query("Users.db", "INSERT INTO USERS (Username) VALUES (\'" + playerName + "\');");
			SQLiteUtils.query("Auth.db", "INSERT INTO AUTH (Username, IP_Reg, Date_Reg) VALUES (\'" + playerName + "\', \'" + ip + "\', \'" + date + "\');");
			Server.getInstance().getLogger().info("§l§7(§6Система§7) §fИгрок §6" + playerName + " §fне зарегистрирован§7! §fРегистрируем§7!");
		}
		FunctionsAPI.SPAWN.addParticle(new FloatingTextParticle(new Position(8.5, 50, 0.5), "§l§6Заходи в портал§7!", "§l§fПросто заходи и начинай выживать"), player);
		FunctionsAPI.SPAWN.addParticle(new FloatingTextParticle(new Position(-4.5, 51, 12.5), "§l§6Маленький приват", "§l§f2 §7× §f2"), player);
		FunctionsAPI.SPAWN.addParticle(new FloatingTextParticle(new Position(-6.5, 51, 14.5), "§l§6Средний приват", "§l§f4 §7× §f4"), player);
		FunctionsAPI.SPAWN.addParticle(new FloatingTextParticle(new Position(-9.5, 51, 13.5), "§l§6Большой приват", "§l§f8 §7× §f8"), player);
		FunctionsAPI.SPAWN.addParticle(new FloatingTextParticle(new Position(-11.5, 51, 11.5), "§l§6Большой приват", "§l§f10 §7× §f10"), player);
		FunctionsAPI.SPAWN.addParticle(new FloatingTextParticle(new Position(8.5, 150, 88.5), "§l§6Как приватить§7?", "§l§fЧтобы запривать регион§7,\n§l§fпросто установи один из блоков\n§l§fкоторые стоят рядом§7. §fКаждый блок имеет\n§l§fограниченный радиус привата§7,\n§l§fкоторый создается вокруг блока§7!"), player);
		Map<String, Integer> counterKills = calculateScore(killsConfig);
		Map<String, Integer> counterDeaths = calculateScore(deathsConfig);
		int placeKills = 1;
		for (Map.Entry<String, Integer> entry : counterKills.entrySet()) {
			if (placeKills <= 10) {
				String title = "§l§6" + placeKills + "§7. §f" + entry.getKey() + " §7- §6" + entry.getValue() + " §fkill§7(§fs§7)";
				FunctionsAPI.SPAWN.addParticle(new FloatingTextParticle(new Position(8.5, 52.5 - placeKills * 0.3, 6.5), title), player);
				placeKills++;
			}
		}
		int placeDeaths = 1;
		for (Map.Entry<String, Integer> entry : counterDeaths.entrySet()) {
			if (placeDeaths <= 10) {
				String title = "§l§6" + placeDeaths + "§7. §f" + entry.getKey() + " §7- §6" + entry.getValue() + " §fdeath§7(§fs§7)";
				FunctionsAPI.SPAWN.addParticle(new FloatingTextParticle(new Position(8.5, 52.5 - placeDeaths * 0.3, -5.5), title), player);
				placeDeaths++;
			}
		}
		FunctionsAPI.SPAWN.addParticle(new FloatingTextParticle(new Position(8.5, 52.5, 6.5), "§l§6Самые опасные Игроки сервера"), player);
		FunctionsAPI.SPAWN.addParticle(new FloatingTextParticle(new Position(8.5, 52.5, -5.5), "§l§6Press F to pay respects"), player);
		player.sendMessage("§l§6• §r§fДобро пожаловать на §3DEATH §fMC §7(§cАнархия§7)\n§l§6• §r§fМы в §9ВК §7- §fvk§7.§fcom§7/§6death§fanarchy §l§6| §r§fНаш сайт §7- §6death§7-§6mc§7.§6online");
		this.startPlayerTime = System.currentTimeMillis();
		PermissionsAPI.updateTag(player);
		PermissionsAPI.updatePermissions(player);
		player.setCheckMovement(false);
		SQLiteUtils.query("Auth.db", "UPDATE AUTH SET IP_Last = \'" + ip + "\', Date_Last = \'" + date + "\' WHERE UPPER(Username) = \'" + upperCase + "\';");
		event.setJoinMessage("");
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		long test = System.currentTimeMillis() / 1000L - startPlayerTime / 1000L;
		Integer gameTime = SQLiteUtils.selectInteger("Users.db", "SELECT `Gametime` FROM USERS WHERE UPPER(`Username`) = '" + player.getName().toUpperCase() + "'");
		SQLiteUtils.query("Users.db", "UPDATE `USERS` SET `Gametime` = '" + (gameTime + test) + "' WHERE UPPER(`Username`) = '" + player.getName().toUpperCase() + "';");
		if (HotbarTask.SCOREBOARD.contains(player.getName())) {
			HotbarTask.SCOREBOARDS.get(player.getName()).hideFor(player);
			HotbarTask.SCOREBOARD.remove(player.getName());
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