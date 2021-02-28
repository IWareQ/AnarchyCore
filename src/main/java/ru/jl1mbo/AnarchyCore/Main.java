package ru.jl1mbo.AnarchyCore;

import java.util.Arrays;
import java.util.Map;

import cn.nukkit.command.Command;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.PluginManager;
import ru.jl1mbo.AnarchyCore.CommandsHandler.AdminPanelCommand;
import ru.jl1mbo.AnarchyCore.CommandsHandler.BonusCommand;
import ru.jl1mbo.AnarchyCore.CommandsHandler.ClearChatCommand;
import ru.jl1mbo.AnarchyCore.CommandsHandler.CoordinateCommand;
import ru.jl1mbo.AnarchyCore.CommandsHandler.DayCommand;
import ru.jl1mbo.AnarchyCore.CommandsHandler.DonateCommand;
import ru.jl1mbo.AnarchyCore.CommandsHandler.FoodCommand;
import ru.jl1mbo.AnarchyCore.CommandsHandler.GamemodeCommand;
import ru.jl1mbo.AnarchyCore.CommandsHandler.HealCommand;
import ru.jl1mbo.AnarchyCore.CommandsHandler.NearCommand;
import ru.jl1mbo.AnarchyCore.CommandsHandler.NightCommand;
import ru.jl1mbo.AnarchyCore.CommandsHandler.NightVisionCommand;
import ru.jl1mbo.AnarchyCore.CommandsHandler.RepairCommand;
import ru.jl1mbo.AnarchyCore.CommandsHandler.ReportCommand;
import ru.jl1mbo.AnarchyCore.CommandsHandler.SpawnCommand;
import ru.jl1mbo.AnarchyCore.CommandsHandler.CraftingTable.CraftingTableCommand;
import ru.jl1mbo.AnarchyCore.CommandsHandler.Defaults.ListCommand;
import ru.jl1mbo.AnarchyCore.CommandsHandler.Defaults.SayCommand;
import ru.jl1mbo.AnarchyCore.CommandsHandler.Defaults.StopCommand;
import ru.jl1mbo.AnarchyCore.CommandsHandler.Defaults.TellCommand;
import ru.jl1mbo.AnarchyCore.CommandsHandler.EnderChest.EnderChestCommand;
import ru.jl1mbo.AnarchyCore.CommandsHandler.Home.HomeAPI;
import ru.jl1mbo.AnarchyCore.CommandsHandler.NPC.NPC;
import ru.jl1mbo.AnarchyCore.CommandsHandler.Shop.ShopHandler;
import ru.jl1mbo.AnarchyCore.CommandsHandler.StorageItems.StorageItemsAPI;
import ru.jl1mbo.AnarchyCore.EventsListener.EntityDamageByEntityListener;
import ru.jl1mbo.AnarchyCore.EventsListener.EntityDeathListener;
import ru.jl1mbo.AnarchyCore.EventsListener.PlayerDeathListener;
import ru.jl1mbo.AnarchyCore.EventsListener.PlayerInteractListener;
import ru.jl1mbo.AnarchyCore.EventsListener.PlayerMoveListener;
import ru.jl1mbo.AnarchyCore.EventsListener.PlayerTeleportListener;
import ru.jl1mbo.AnarchyCore.GameHandler.Achievements.AchievementsAPI;
import ru.jl1mbo.AnarchyCore.GameHandler.Auction.AuctionAPI;
import ru.jl1mbo.AnarchyCore.GameHandler.BlockProtection.BlockProtectionAPI;
import ru.jl1mbo.AnarchyCore.GameHandler.Bosses.BossAPI;
import ru.jl1mbo.AnarchyCore.GameHandler.ClanManager.ClanManager;
import ru.jl1mbo.AnarchyCore.GameHandler.CombatLogger.CombatLoggerAPI;
import ru.jl1mbo.AnarchyCore.GameHandler.ExperienceBottle.ExperienceBottleAPI;
import ru.jl1mbo.AnarchyCore.LoginPlayerHandler.AntiCheat.AntiCheatAPI;
import ru.jl1mbo.AnarchyCore.LoginPlayerHandler.Authorization.AuthorizationAPI;
import ru.jl1mbo.AnarchyCore.LoginPlayerHandler.FloatingTexts.FloatingTextsAPI;
import ru.jl1mbo.AnarchyCore.LoginPlayerHandler.Scoreboard.ScoreboardTask;
import ru.jl1mbo.AnarchyCore.Manager.FakeInventory.FakeInventoryAPI;
import ru.jl1mbo.AnarchyCore.Manager.Forms.FormsAPI;
import ru.jl1mbo.AnarchyCore.Manager.WorldSystem.WorldSystemAPI;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.BanSystem.BanSystemAPI;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.CheatCheacker.CheatCheackerAPI;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.EconomyAPI.EconomyAPI;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.Permissions.PermissionAPI;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.SeeInventory.SeeInventoryAPI;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.Spectate.SpectateAPI;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.TeleportSystem.TeleportAPI;
import ru.jl1mbo.AnarchyCore.Task.BroadcastTask;
import ru.jl1mbo.AnarchyCore.Task.ClearTask;
import ru.jl1mbo.AnarchyCore.Task.RestartTask;

public class Main extends PluginBase {
	private static Main instance;

	public static Main getInstance() {
		return instance;
	}

	private static void registerAll() {
		BroadcastTask.register();
		SpectateAPI.register();
		SeeInventoryAPI.register();
		PermissionAPI.register();
		EconomyAPI.register();
		BanSystemAPI.register();
		FormsAPI.register();
		FakeInventoryAPI.register();
		WorldSystemAPI.register();
		ScoreboardTask.register();
		FloatingTextsAPI.register();
		AuthorizationAPI.register();
		ExperienceBottleAPI.register();
		ClanManager.registe();
		CombatLoggerAPI.register();
		BlockProtectionAPI.register();
		AuctionAPI.register();
		AchievementsAPI.register();
		HomeAPI.register();
		BossAPI.register();
		NPC.register();
		StorageItemsAPI.register();
		AntiCheatAPI.register();
		CheatCheackerAPI.register();
		TeleportAPI.register();
	}

	private void registerTask() {
		this.getServer().getScheduler().scheduleRepeatingTask(new ClearTask(), 20);
		this.getServer().getScheduler().scheduleRepeatingTask(new BroadcastTask(), 60 * 20);
		this.getServer().getScheduler().scheduleRepeatingTask(new RestartTask(), 20);
		this.getServer().getScheduler().scheduleRepeatingTask(new ScoreboardTask(), 60 * 20);
	}

	@Override()
	public void onEnable() {
		instance = this;
		this.registerEvents();
		this.unregisterCommands();
		this.registerCommands();
		registerAll();
		registerTask();
		this.getLogger().info("§lПлагин §6AnarchyCore §aАктивирован§7!");
	}

	@Override()
	public void onDisable() {
		this.getLogger().info("§lПлагин §6AnarchyCore §cДеактивирован§7!");
		AuctionAPI.saveAuction();
	}

	private void registerEvents() {
		PluginManager pluginManager = this.getServer().getPluginManager();
		pluginManager.registerEvents(new EntityDamageByEntityListener(), this);
		pluginManager.registerEvents(new EntityDeathListener(), this);
		pluginManager.registerEvents(new PlayerDeathListener(), this);
		pluginManager.registerEvents(new PlayerInteractListener(), this);
		pluginManager.registerEvents(new PlayerMoveListener(), this);
		pluginManager.registerEvents(new PlayerTeleportListener(), this);
		pluginManager.registerEvents(new ShopHandler(), this);
	}

	private void unregisterCommands() {
		Map<String, Command> commandMap = this.getServer().getCommandMap().getCommands();
		for (String commands : new String[] {"help", "mixer", "gamemode", "gm", "?", "list", "tell", "stop", "me"}) {
			commandMap.remove(commands);
		}
	}

	private void registerCommands() {
		this.getServer().getCommandMap().registerAll("", Arrays.asList(new AdminPanelCommand(), new StopCommand(), new ShopHandler(), new BonusCommand(), new SayCommand(), new SpawnCommand(), new NightVisionCommand(), new CraftingTableCommand(), new ReportCommand(), new ClearChatCommand(), new ListCommand(), new TellCommand(), new EnderChestCommand(), new CoordinateCommand(), new HealCommand(), new NightCommand(), new DayCommand(), new FoodCommand(), new NearCommand(), new DonateCommand(), new RepairCommand(), new GamemodeCommand()));
	}
}