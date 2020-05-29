package Anarchy;

import Anarchy.Manager.Functions.FunctionsAPI;
import Anarchy.Manager.Sessions.AllSessionsManager;
import Anarchy.Module.Auction.AuctionAPI;
import Anarchy.Module.Auction.AuctionEventsHandler;
import Anarchy.Module.Auction.Commands.AuctionCommand;
import Anarchy.Module.Auction.Commands.BuyCommand;
import Anarchy.Module.Auth.AuthEventsHandler;
import Anarchy.Module.Commands.*;
import Anarchy.Module.Commands.Check.CheckCommand;
import Anarchy.Module.Commands.Defaults.ListCommand;
import Anarchy.Module.Commands.Defaults.TellCommand;
import Anarchy.Module.Commands.Donate.DonateHandler;
import Anarchy.Module.Commands.EnderChest.EnderChestCommand;
import Anarchy.Module.Commands.Home.DelHomeCommand;
import Anarchy.Module.Commands.Home.HomeCommand;
import Anarchy.Module.Commands.Home.HomesCommand;
import Anarchy.Module.Commands.Home.SetHomeCommand;
import Anarchy.Module.Commands.Spectate.SpectateCommand;
import Anarchy.Module.Commands.Teleport.TpaCommand;
import Anarchy.Module.Commands.Teleport.TpnCommand;
// import Anarchy.Module.Commands.Teleport.TprCommand;
import Anarchy.Module.Commands.Teleport.TpyCommand;
import Anarchy.Module.Economy.Commands.*;
import Anarchy.Module.EventsHandler.EventsHandler;
import Anarchy.Module.Permissions.Commands.GroupCommand;
import Anarchy.Module.Permissions.PermissionsAPI;
import Anarchy.Module.Regions.Commands.RegionCommand;
import Anarchy.Module.Regions.RegionsAPI;
import Anarchy.Module.Regions.RegionsEventsHandler;
import Anarchy.Module.Wither.WitherEventsHandler;
import Anarchy.Task.HotbarTask;
import Anarchy.Task.MinuteTask;
import Anarchy.Task.Utils.Broadcast;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.PluginManager;

import java.io.File;
import java.util.Arrays;
import java.util.Map;

public class AnarchyMain extends PluginBase {
	public static AnarchyMain plugin;
	public static String folder;
	public static String datapath;
	public static int port;

	@Override
	public void onEnable() {
		PluginManager pluginManager = Server.getInstance().getPluginManager();
		for (String pluginName: new String[] {
				"DbLib",
				"ScoreboardPlugin",
				"MobPlugin",
				"FormAPI"
			}) {
			if (pluginManager.getPlugin(pluginName) == null) {
				getLogger().alert("§l§aНе хватает плагина §7- §b\"" + pluginName + "\"");
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
		unregisterCommands();
		registerCommands();
		registerTask();
		getLogger().info("§fПлагин §aАктивирован");
	}

	@Override
	public void onDisable() {
		unregisterAll();
		getLogger().info("§fПлагин §cДезактивирован");
	}

	private void registerAll() {
		FunctionsAPI.register();
		RegionsAPI.register();
		PermissionsAPI.register();
		AuctionAPI.register();
		Broadcast.register();
		AuthEventsHandler.register();
		WitherEventsHandler.register();
	}

	private void unregisterAll() {
		AllSessionsManager.saveAllSessions();
		AuctionAPI.unregister();
	}

	private void registerEvents() {
		PluginManager pluginManager = getServer().getPluginManager();
		pluginManager.registerEvents(new RegionsEventsHandler(), this);
		pluginManager.registerEvents(new AuthEventsHandler(), this);
		pluginManager.registerEvents(new AuctionEventsHandler(), this);
		pluginManager.registerEvents(new EventsHandler(), this);
		pluginManager.registerEvents(new DonateHandler(), this);
	}

	private void unregisterCommands() {
		Map<String, Command> commandMap = getServer().getCommandMap().getCommands();
		for (String command: new String[] {
				"me",
				"ver",
				"say",
				"pl",
				"plugins",
				"mixer",
				"difficulty",
				"defaultgamemode",
				"help",
				"?",
				"pardon",
				"unban",
				"particle",
				"tell",
				"list",
				"about",
				"title"
			}) {
			commandMap.remove(command);
		}
	}

	private void registerCommands() {
		Command[] commands = new Command[] {
			new DonateHandler(),
			new EnderChestCommand(),
			new SpectateCommand(),
			new DayCommand(),
			new NightCommand(),
			new FoodCommand(),
			new NearCommand(),
			new RepairCommand(),
			new HealCommand(),
			new RegionCommand(),
			new TellCommand(),
			new ListCommand(),
			new TpaCommand(),
			new TpyCommand(),
			new TpnCommand(),
			//new TprCommand(),
			new ProfileCommand(),
			new MoneyCommand(),
			new PayCommand(),
			new CheckCommand(),
			new AddMoneyCommand(),
			new SetMoneyCommand(),
			new SeeMoneyCommand(),
			new GroupCommand(),
			new BuyCommand(),
			new AuctionCommand(),
			new BarCommand(),
			new HomeCommand(),
			new HomesCommand(),
			new SetHomeCommand(),
			new DelHomeCommand()
		};
		getServer().getCommandMap().registerAll("", Arrays.asList(commands));
	}

	private void registerTask() {
		this.getServer().getScheduler().scheduleRepeatingTask(new MinuteTask(), 60 * 20);
		this.getServer().getScheduler().scheduleRepeatingTask(new HotbarTask(), 30 * 20);
	}
}