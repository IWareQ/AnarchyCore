package Anarchy;

import java.io.File;
import java.util.Arrays;
import java.util.Map;

import Anarchy.Functions.FunctionsAPI;
import Anarchy.Module.Commands.BarCommand;
import Anarchy.Module.Commands.BonusCommand;
import Anarchy.Module.Commands.ClearChatCommand;
import Anarchy.Module.Commands.CoordinateCommand;
import Anarchy.Module.Commands.DayCommand;
import Anarchy.Module.Commands.DonateCommand;
import Anarchy.Module.Commands.FoodCommand;
import Anarchy.Module.Commands.GamemodeCommand;
import Anarchy.Module.Commands.GarbageCommand;
import Anarchy.Module.Commands.HealCommand;
import Anarchy.Module.Commands.NearCommand;
import Anarchy.Module.Commands.NightCommand;
import Anarchy.Module.Commands.NightVisionCommand;
import Anarchy.Module.Commands.RepairCommand;
import Anarchy.Module.Commands.ReportCommand;
import Anarchy.Module.Commands.ResyncCommand;
import Anarchy.Module.Commands.SpawnCommand;
import Anarchy.Module.Commands.CraftingTable.CraftingTableCommand;
import Anarchy.Module.Commands.Defaults.ListCommand;
import Anarchy.Module.Commands.Defaults.StopCommand;
import Anarchy.Module.Commands.Defaults.TellCommand;
import Anarchy.Module.Commands.DonateShop.DonateShopHandler;
import Anarchy.Module.Commands.EnderChest.EnderChestCommand;
import Anarchy.Module.Commands.ExperienceBottle.ExperienceBottleHandler;
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
import Anarchy.Module.EventsHandler.EventsHandler;
import Anarchy.Task.ClearTask;
import Anarchy.Task.HotbarTask;
import Anarchy.Task.MinuteTask;
import Anarchy.Task.SecondTask;
import Anarchy.Task.Utils.Broadcast;
import cn.nukkit.command.Command;
import cn.nukkit.entity.Entity;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.PluginManager;

public class AnarchyMain extends PluginBase {
	public static String PREFIX = "§l§7(§3Система§7) §r";
	public static AnarchyMain plugin;
	public static File folder;

	@Override()
	public void onEnable() {
		PluginManager pluginManager = this.getServer().getPluginManager();
		for (String pluginName : new String[] {"ScoreboardPlugin", "FormAPI"}) {
			if (pluginManager.getPlugin(pluginName) == null) {
				this.getLogger().alert("§l§fНе найден §3плагин §7- §6" + pluginName);
				pluginManager.disablePlugin(this);
				return;
			}
		}
		plugin = this;
		folder = this.getDataFolder();
		new File(String.valueOf(folder)).mkdirs();
		this.registerAll();
		this.registerEvents();
		this.registerEntity();
		this.unregisterCommands();
		this.registerCommands();
		this.registerTask();
		new File(this.getDataFolder() + "/StorageItems/").mkdirs();
		this.getLogger().info("§l§fПлагин §aАктивирован§7! (§fАвтор §7- @§6extranons§7)");
	}

	@Override()
	public void onDisable() {
		this.getLogger().info("§l§fПлагин §cДеактивирован§7! (§fАвтор §7- @§6extranons§7)");
	}
	
	private void registerAll() {
		FunctionsAPI.register();
		Broadcast.register();
	}

	private void registerEvents() {
		PluginManager pluginManager = this.getServer().getPluginManager();
		pluginManager.registerEvents(new SpectateEventsHandler(), this);
		pluginManager.registerEvents(new StorageHandler(), this);
		pluginManager.registerEvents(new EventsHandler(), this);
		pluginManager.registerEvents(new InventoryHandler(), this);
		pluginManager.registerEvents(new DonateShopHandler(), this);
		pluginManager.registerEvents(new ExperienceBottleHandler(), this);
	}

	private void unregisterCommands() {
		Map<String, Command> commandMap = this.getServer().getCommandMap().getCommands();
		for (String command : new String[] {"me", "ver", "say", "pl", "plugins", "mixer", "difficulty", "defaultgamemode", "help", "?", "pardon", "particle", "tell", "gm", "gamemode", "list", "about", "title", "unban", "ban"}) {
			commandMap.remove(command);
		}
	}

	private void registerEntity() {
		Entity.registerEntity(PiglinBruteNPC.class.getSimpleName(), PiglinBruteNPC.class);
		Entity.registerEntity(VillagerNPC.class.getSimpleName(), VillagerNPC.class);
	}

	private void registerCommands() {
		Command[] commands = new Command[] {new ExperienceBottleHandler(), new GarbageCommand(), new DonateShopHandler(), new SpawnCommand(), new BonusCommand(), new NPCCommand(), new NightVisionCommand(), new CraftingTableCommand(), new ReportCommand(), new ResyncCommand(), new ClearChatCommand(), new StopCommand(), new InventoryHandler(), new ListCommand(), new TellCommand(), new EnderChestCommand(), new SetHomeCommand(), new HomeCommand(), new DelHomeCommand(), new SpectateCommand(), new StorageHandler(), new TpaCommand(), new TpcCommand(), new TpdCommand(), new TprCommand(), new CoordinateCommand(), new HealCommand(), new NightCommand(), new DayCommand(), new FoodCommand(), new BarCommand(), new NearCommand(), new DonateCommand(), new RepairCommand(), new GamemodeCommand()};
		this.getServer().getCommandMap().registerAll("", Arrays.asList(commands));
	}

	private void registerTask() {
		this.getServer().getScheduler().scheduleRepeatingTask(new MinuteTask(), 60 * 20);
		this.getServer().getScheduler().scheduleRepeatingTask(new HotbarTask(), 20);
		this.getServer().getScheduler().scheduleRepeatingTask(new SecondTask(), 20);
		this.getServer().getScheduler().scheduleRepeatingTask(new ClearTask(), 20);
	}
}