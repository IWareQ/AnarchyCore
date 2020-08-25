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
import Anarchy.Module.Commands.ACommand;
import Anarchy.Module.Commands.BarCommand;
import Anarchy.Module.Commands.DayCommand;
import Anarchy.Module.Commands.DonateCommand;
import Anarchy.Module.Commands.FoodCommand;
import Anarchy.Module.Commands.GamemodeCommand;
import Anarchy.Module.Commands.HealCommand;
import Anarchy.Module.Commands.KickCommand;
import Anarchy.Module.Commands.NearCommand;
import Anarchy.Module.Commands.NightCommand;
import Anarchy.Module.Commands.RepairCommand;
import Anarchy.Module.Commands.TestCommand;
import Anarchy.Module.Commands.Check.CheckCommand;
import Anarchy.Module.Commands.Defaults.ListCommand;
import Anarchy.Module.Commands.Defaults.TellCommand;
import Anarchy.Module.Commands.EnderChest.EnderChestCommand;
import Anarchy.Module.Commands.Home.DelHomeCommand;
import Anarchy.Module.Commands.Home.HomeCommand;
import Anarchy.Module.Commands.Home.SetHomeCommand;
import Anarchy.Module.Commands.Spectate.SpectateCommand;
import Anarchy.Module.Commands.Storage.StorageHandler;
import Anarchy.Module.Commands.Teleport.TpaCommand;
import Anarchy.Module.Commands.Teleport.TpnCommand;
import Anarchy.Module.Commands.Teleport.TprCommand;
import Anarchy.Module.Commands.Teleport.TpyCommand;
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
import Anarchy.Task.HotbarTask;
import Anarchy.Task.MinuteTask;
import Anarchy.Task.Utils.Broadcast;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.entity.Entity;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.PluginManager;

public class AnarchyMain extends PluginBase {
	public static AnarchyMain plugin;
	public static String folder;
	public static String datapath;
	public static int port;
	
	@Override()
	public void onEnable() {
		PluginManager pluginManager = Server.getInstance().getPluginManager();
		for (String pluginName : new String[]{"DbLib", "ScoreboardPlugin", "MobPlugin", "FormAPI"}) {
			if (pluginManager.getPlugin(pluginName) == null) {
				getLogger().alert("§l§fНе найден §3плагин §7- §6" + pluginName);
				pluginManager.disablePlugin(this);
				return;
			}
		}
		plugin = this;
		folder = "";
		port = Server.getInstance().getPort();
		datapath = folder + port;
		new File(datapath).mkdirs();
		SQLProvider.register();
		registerAll();
		registerEvents();
		registerEntity();
		unregisterCommands();
		registerCommands();
		registerTask();
		getLogger().info("§l§fПлагин §aАктивирован§7! (§fАвтор §7- @§3extranons§7)");
	}
	
	@Override()
	public void onDisable() {
		unregisterAll();
		getLogger().info("§l§fПлагин §cДеактивирован§7! (§fАвтор §7- @§3extranons§7)");
	}
	
	private void registerAll() {
		FunctionsAPI.register();
		RegionsAPI.register();
		PermissionsAPI.register();
		AuctionAPI.register();
		Broadcast.register();
	}
	
	private void unregisterAll() {
		AllSessionsManager.saveAllSessions();
		AuctionAPI.unregister();
	}
	
	private void registerEvents() {
		PluginManager pluginManager = getServer().getPluginManager();
		pluginManager.registerEvents(new RegionsEventsHandler(), this);
		pluginManager.registerEvents(new AuctionEventsHandler(), this);
		pluginManager.registerEvents(new EventsHandler(), this);
		pluginManager.registerEvents(new AuthEventsHandler(), this);
		pluginManager.registerEvents(new StorageHandler(), this);
	}
	
	private void unregisterCommands() {
		Map<String, Command> commandMap = getServer().getCommandMap().getCommands();
		for (String command : new String[]{"me", "ver", "say", "pl", "plugins", "mixer", "difficulty", "defaultgamemode", "help", "?", "pardon", "unban", "particle", "tell", "gm", "gamemode", "list", "about", "title"}) {
			commandMap.remove(command);
		}
	}
	
	public static void registerEntity() {
		Entity.registerEntity(SilverfishBoss.class.getSimpleName(), SilverfishBoss.class);
	}
	
	private void registerCommands() {
		Command[] commands = new Command[]{new TestCommand(), new TprCommand(), new StorageHandler(), new DonateCommand(), new EnderChestCommand(), new SpectateCommand(), new DayCommand(), new NightCommand(), new FoodCommand(), new NearCommand(), new RepairCommand(), new HealCommand(), new RegionCommand(), new TellCommand(), new ListCommand(), new TpaCommand(), new TpyCommand(), new TpnCommand(), new KickCommand(), new MoneyCommand(), new PayCommand(), new ACommand(), new CheckCommand(), new AddMoneyCommand(), new SetMoneyCommand(), new SeeMoneyCommand(), new GroupCommand(), new AuctionCommand(), new BarCommand(), new HomeCommand(), new SetHomeCommand(), new GamemodeCommand(), new DelHomeCommand()};
		getServer().getCommandMap().registerAll("", Arrays.asList(commands));
	}
	
	private void registerTask() {
		this.getServer().getScheduler().scheduleRepeatingTask(new MinuteTask(), 60 * 20);
		this.getServer().getScheduler().scheduleRepeatingTask(new HotbarTask(), 30 * 20);
	}
}