package Anarchy;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;

import Anarchy.Manager.Functions.FunctionsAPI;
import Anarchy.Manager.Sessions.AllSessionsManager;
import Anarchy.Module.Auction.AuctionAPI;
import Anarchy.Module.Auction.AuctionEventsHandler;
import Anarchy.Module.Auction.Commands.AuctionCommand;
import Anarchy.Module.Auth.AuthEventsHandler;
import Anarchy.Module.BanSystem.Commands.BanCommand;
import Anarchy.Module.BanSystem.Commands.MuteCommand;
import Anarchy.Module.BanSystem.Commands.UnBanCommand;
import Anarchy.Module.BanSystem.Commands.UnMuteCommand;
import Anarchy.Module.Boss.EvokerBoss;
import Anarchy.Module.Boss.RavagerBoss;
import Anarchy.Module.Boss.SilverfishBoss;
import Anarchy.Module.Boss.SlimeBoss;
import Anarchy.Module.Boss.WitchBoss;
import Anarchy.Module.CombatLogger.CombatLoggerEventsHandler;
import Anarchy.Module.Commands.BarCommand;
import Anarchy.Module.Commands.BonusCommand;
import Anarchy.Module.Commands.ClearChatCommand;
import Anarchy.Module.Commands.CoordinateCommand;
import Anarchy.Module.Commands.DayCommand;
import Anarchy.Module.Commands.DonateCommand;
import Anarchy.Module.Commands.FoodCommand;
import Anarchy.Module.Commands.GamemodeCommand;
import Anarchy.Module.Commands.HealCommand;
import Anarchy.Module.Commands.NearCommand;
import Anarchy.Module.Commands.NightCommand;
import Anarchy.Module.Commands.NightVisionCommand;
import Anarchy.Module.Commands.RepairCommand;
import Anarchy.Module.Commands.ReportCommand;
import Anarchy.Module.Commands.ResyncCommand;
import Anarchy.Module.Commands.SpawnCommand;
import Anarchy.Module.Commands.TestCommand;
import Anarchy.Module.Commands.CraftingTable.CraftingTableCommand;
import Anarchy.Module.Commands.Defaults.ListCommand;
import Anarchy.Module.Commands.Defaults.StopCommand;
import Anarchy.Module.Commands.Defaults.TellCommand;
import Anarchy.Module.Commands.DonateShop.DonateShopHandler;
import Anarchy.Module.Commands.EnderChest.EnderChestCommand;
import Anarchy.Module.Commands.Home.DelHomeCommand;
import Anarchy.Module.Commands.Home.HomeCommand;
import Anarchy.Module.Commands.Home.SetHomeCommand;
import Anarchy.Module.Commands.Inventory.InventoryHandler;
import Anarchy.Module.Commands.NPC.NPCCommand;
import Anarchy.Module.Commands.NPC.Utils.PiglinBruteNPC;
import Anarchy.Module.Commands.NPC.Utils.VillagerNPC;
import Anarchy.Module.Commands.Spectate.SpectateEventsHandler;
import Anarchy.Module.Commands.Spectate.Commands.SpectateCommand;
import Anarchy.Module.Commands.Storage.StorageHandler;
import Anarchy.Module.Commands.Teleport.TpaCommand;
import Anarchy.Module.Commands.Teleport.TpcCommand;
import Anarchy.Module.Commands.Teleport.TpdCommand;
import Anarchy.Module.Commands.Teleport.TprCommand;
import Anarchy.Module.Economy.Commands.AddMoneyCommand;
import Anarchy.Module.Economy.Commands.MoneyCommand;
import Anarchy.Module.Economy.Commands.PayCommand;
import Anarchy.Module.Economy.Commands.SeeMoneyCommand;
import Anarchy.Module.Economy.Commands.SetMoneyCommand;
import Anarchy.Module.EventsHandler.EventsHandler;
import Anarchy.Module.Permissions.PermissionsAPI;
import Anarchy.Module.Permissions.Commands.GroupCommand;
import Anarchy.Module.Regions.RegionsAPI;
import Anarchy.Module.Regions.RegionsEventsHandler;
import Anarchy.Module.Regions.Commands.RegionCommand;
import Anarchy.Task.ClearTask;
import Anarchy.Task.HotbarTask;
import Anarchy.Task.MinuteTask;
import Anarchy.Task.SecondTask;
import Anarchy.Task.Utils.Broadcast;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.entity.Entity;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.PluginManager;

public class AnarchyMain extends PluginBase {
	public static String PREFIX = "§l§7(§3Система§7) §r";
	public static String accessToken = "ac9b77006227ff5fd44036db53fa328fb99809eb5d4fb42a458bfd3bcc278065ba5ad0abcc1a3fac1a2ae";
	public static AnarchyMain plugin;
	public static String folder = "";
	public static String datapath;
	public static int port;

	@Override()
	public void onEnable() {
		PluginManager pluginManager = this.getServer().getPluginManager();
		for (String pluginName : new String[] {"DbLib", "ScoreboardPlugin", "MobPlugin", "FormAPI"}) {
			if (pluginManager.getPlugin(pluginName) == null) {
				this.getLogger().alert("§l§fНе найден §3плагин §7- §6" + pluginName);
				pluginManager.disablePlugin(this);
				return;
			}
		}
		plugin = this;
		port = this.getServer().getPort();
		datapath = folder + port;
		(new File(datapath)).mkdirs();
		this.registerAll();
		this.registerEvents();
		this.registerEntity();
		this.unregisterCommands();
		this.registerCommands();
		this.registerTask();
		this.getLogger().info("§l§fПлагин §aАктивирован§7! (§fАвтор §7- @§3extranons§7)");
	}

	@Override()
	public void onDisable() {
		this.unregisterAll();
		this.getLogger().info("§l§fПлагин §cДеактивирован§7! (§fАвтор §7- @§3extranons§7)");
	}

	private void registerAll() {
		FunctionsAPI.register();
		AuctionAPI.register();
		PermissionsAPI.register();
		RegionsAPI.register();
		SQLProvider.register();
		Broadcast.register();
	}

	private void unregisterAll() {
		AllSessionsManager.saveAllSessions();
		AuctionAPI.saveAuction();
	}

	private void registerEvents() {
		PluginManager pluginManager = this.getServer().getPluginManager();
		pluginManager.registerEvents(new AuctionEventsHandler(), this);
		pluginManager.registerEvents(new AuthEventsHandler(), this);
		pluginManager.registerEvents(new CombatLoggerEventsHandler(), this);
		pluginManager.registerEvents(new SpectateEventsHandler(), this);
		pluginManager.registerEvents(new StorageHandler(), this);
		pluginManager.registerEvents(new EventsHandler(), this);
		pluginManager.registerEvents(new RegionsEventsHandler(), this);
		pluginManager.registerEvents(new InventoryHandler(), this);
		pluginManager.registerEvents(new DonateShopHandler(), this);
	}

	private void unregisterCommands() {
		Map<String, Command> commandMap = this.getServer().getCommandMap().getCommands();
		for (String command : new String[] {"me", "ver", "say", "pl", "plugins", "mixer", "difficulty", "defaultgamemode", "help", "?", "pardon", "particle", "tell", "gm", "gamemode", "list", "about", "title", "unban", "ban"}) {
			commandMap.remove(command);
		}
	}

	private void registerEntity() {
		Entity.registerEntity(SilverfishBoss.class.getSimpleName(), SilverfishBoss.class);
		Entity.registerEntity(RavagerBoss.class.getSimpleName(), RavagerBoss.class);
		Entity.registerEntity(WitchBoss.class .getSimpleName(), WitchBoss.class);
		Entity.registerEntity(SlimeBoss.class .getSimpleName(), SlimeBoss.class);
		Entity.registerEntity(EvokerBoss.class .getSimpleName(), EvokerBoss.class);
		Entity.registerEntity(PiglinBruteNPC.class .getSimpleName(), PiglinBruteNPC.class);
		Entity.registerEntity(VillagerNPC.class.getSimpleName(), VillagerNPC.class);
	}

	private void registerCommands() {
		Command[] commands = new Command[] {new UnMuteCommand(), new UnBanCommand(), new DonateShopHandler(), new SpawnCommand(), new BonusCommand(), new MuteCommand(), new NPCCommand(), new BanCommand(), new NightVisionCommand(), new CraftingTableCommand(), new ReportCommand(), new ResyncCommand(), new ClearChatCommand(), new StopCommand(), new AuctionCommand(), new InventoryHandler(), new ListCommand(), new TellCommand(), new EnderChestCommand(), new SetHomeCommand(), new HomeCommand(), new DelHomeCommand(), new SpectateCommand(), new StorageHandler(), new TpaCommand(), new TpcCommand(), new TpdCommand(), new TprCommand(), new CoordinateCommand(), new HealCommand(), new NightCommand(), new DayCommand(), new FoodCommand(), new BarCommand(), new NearCommand(), new TestCommand(), new DonateCommand(), new RepairCommand(), new Anarchy.Module.BanSystem.Commands.KickCommand(), new GamemodeCommand(), new MoneyCommand(), new PayCommand(), new AddMoneyCommand(), new SetMoneyCommand(), new SeeMoneyCommand(), new GroupCommand(), new RegionCommand()};
		this.getServer().getCommandMap().registerAll("", Arrays.asList(commands));
	}

	private void registerTask() {
		this.getServer().getScheduler().scheduleRepeatingTask(new MinuteTask(), 60 * 20);
		this.getServer().getScheduler().scheduleRepeatingTask(new HotbarTask(), 20);
		this.getServer().getScheduler().scheduleRepeatingTask(new SecondTask(), 20);
		this.getServer().getScheduler().scheduleRepeatingTask(new ClearTask(), 20);
	}

	public static void sendMessageToChat(String message, int peerId) {
		try {
			String url = "https://api.vk.com/method/messages.send?peer_id=" + peerId + "&random_id=" + new Random().nextInt(Integer.MAX_VALUE) + "&access_token=" + accessToken + "&message=" + URLEncoder.encode(message, "UTF-8") + "&v=5.124";
			URL(url);
		} catch (Exception e) {
			Server.getInstance().getLogger().alert("§l§fОшибка в §6sendMessageToChat§7: §6" + e);
		}
	}
	
	public static void sendMessage(String message, int peerId) {
		try {
			String url = "https://api.vk.com/method/messages.send?user_id=" + peerId + "&random_id=" + new Random().nextInt(Integer.MAX_VALUE) + "&access_token=" + accessToken + "&message=" + URLEncoder.encode(message, "UTF-8") + "&v=5.124";
			URL(url);
		} catch (Exception e) {
			Server.getInstance().getLogger().alert("§l§fОшибка в §6sendMessage§7: §6" + e);
		}
	}

	private static void URL(String url) {
		try {
			URL request = new URL(url);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(request.openStream()));
			StringBuilder stringBuilder = new StringBuilder();
			String inputLine;
			while ((inputLine = bufferedReader.readLine()) != null) stringBuilder.append(inputLine);
			bufferedReader.close();
		} catch (Exception e) {
			Server.getInstance().getLogger().alert("§l§fОшибка в §6URL§7: §6" + e);
		}
	}
}