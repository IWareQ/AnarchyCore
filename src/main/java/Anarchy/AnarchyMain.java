package Anarchy;

import java.io.File;
import java.util.Arrays;
import java.util.Map;

import Anarchy.Manager.Functions.FunctionsAPI;
import Anarchy.Manager.Sessions.AllSessionsManager;
import Anarchy.Module.Auction.AuctionAPI;
import Anarchy.Module.Auction.AuctionEventsHandler;
import Anarchy.Module.Auction.Commands.AuctionCommand;
import Anarchy.Module.Auth.AuthEventsHandler;
import Anarchy.Module.Boss.SilverfishBoss;
import Anarchy.Module.CombatLogger.CombatLoggerEventsHandler;
import Anarchy.Module.Commands.ACommand;
import Anarchy.Module.Commands.BarCommand;
import Anarchy.Module.Commands.ClearChatCommand;
import Anarchy.Module.Commands.CoordinateCommand;
import Anarchy.Module.Commands.DayCommand;
import Anarchy.Module.Commands.DonateCommand;
import Anarchy.Module.Commands.FoodCommand;
import Anarchy.Module.Commands.GamemodeCommand;
import Anarchy.Module.Commands.HealCommand;
import Anarchy.Module.Commands.KickCommand;
import Anarchy.Module.Commands.NearCommand;
import Anarchy.Module.Commands.NightCommand;
import Anarchy.Module.Commands.RepairCommand;
import Anarchy.Module.Commands.ResyncCommand;
import Anarchy.Module.Commands.TestCommand;
import Anarchy.Module.Commands.Defaults.ListCommand;
import Anarchy.Module.Commands.Defaults.StopCommand;
import Anarchy.Module.Commands.Defaults.TellCommand;
import Anarchy.Module.Commands.EnderChest.EnderChestCommand;
import Anarchy.Module.Commands.Home.DelHomeCommand;
import Anarchy.Module.Commands.Home.HomeCommand;
import Anarchy.Module.Commands.Home.SetHomeCommand;
import Anarchy.Module.Commands.Inventory.InventoryCommand;
import Anarchy.Module.Commands.Spectate.SpectateCommand;
import Anarchy.Module.Commands.Spectate.SpectateEventsHandler;
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
import Anarchy.Task.CombatLoggerTask;
import Anarchy.Task.HotbarTask;
import Anarchy.Task.MinuteTask;
import Anarchy.Task.Utils.Broadcast;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.entity.Entity;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.PluginManager;

public class AnarchyMain extends PluginBase {
	public static String PREFIX = "§l§7(§3Система§7) §r";
	public static AnarchyMain plugin;
	public static String folder;
	public static String datapath;
	public static int port;
	
	@Override()
	public void onEnable() {
		PluginManager pluginManager = Server.getInstance().getPluginManager();
		for (String pluginName : new String[]{"DbLib", "ScoreboardPlugin", "MobPlugin", "FormAPI"}) {
			if (pluginManager.getPlugin(pluginName) == null) {
				this.getLogger().alert("§l§fНе найден §3плагин §7- §6" + pluginName);
				pluginManager.disablePlugin(this);
				return;
			}
		}
		plugin = this;
		folder = "";
		port = Server.getInstance().getPort();
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
		AuctionAPI.unregister();
	}
	
	private void registerEvents() {
		PluginManager pluginManager = getServer().getPluginManager();
		pluginManager.registerEvents(new AuctionEventsHandler(), this);
		pluginManager.registerEvents(new AuthEventsHandler(), this);
		pluginManager.registerEvents(new CombatLoggerEventsHandler(), this);
		pluginManager.registerEvents(new SpectateEventsHandler(), this);
		pluginManager.registerEvents(new StorageHandler(), this);
		pluginManager.registerEvents(new EventsHandler(), this);
		pluginManager.registerEvents(new RegionsEventsHandler(), this);
	}
	
	private void unregisterCommands() {
		Map<String, Command> commandMap = getServer().getCommandMap().getCommands();
		for (String command : new String[]{"me", "ver", "say", "pl", "plugins", "mixer", "difficulty", "defaultgamemode", "help", "?", "pardon",  "particle", "tell", "gm", "gamemode", "list", "about", "title"}) {
			commandMap.remove(command);
		}
	}
	
	private void registerEntity() {
		Entity.registerEntity(SilverfishBoss.class.getSimpleName(), SilverfishBoss.class);
	}
	
	private void registerCommands() {
		Command[] commands = new Command[]{new ResyncCommand(),new ClearChatCommand(),new StopCommand(), new AuctionCommand(), new InventoryCommand(), new ListCommand(), new TellCommand(), new EnderChestCommand(), new SetHomeCommand(), new HomeCommand(), new DelHomeCommand(), new SpectateCommand(), new StorageHandler(), new TpaCommand(), new TpcCommand(), new TpdCommand(), new TprCommand(), new CoordinateCommand(), new HealCommand(), new NightCommand(), new DayCommand(), new FoodCommand(), new BarCommand(), new NearCommand(), new ACommand(), new TestCommand(), new DonateCommand(), new RepairCommand(), new KickCommand(), new GamemodeCommand(), new MoneyCommand(), new PayCommand(), new AddMoneyCommand(), new SetMoneyCommand(), new SeeMoneyCommand(), new GroupCommand(), new RegionCommand()};
		this.getServer().getCommandMap().registerAll("", Arrays.asList(commands));
	}
	
	private void registerTask() {
		this.getServer().getScheduler().scheduleRepeatingTask(new MinuteTask(), 60 * 20);
		this.getServer().getScheduler().scheduleRepeatingTask(new HotbarTask(), 30 * 20);
		this.getServer().getScheduler().scheduleRepeatingTask(new CombatLoggerTask(), 20);
		this.getServer().getScheduler().scheduleRepeatingTask(new ClearTask(), 20);
	}
}