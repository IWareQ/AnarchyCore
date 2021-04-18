package ru.jl1mbo.AnarchyCore;

import java.util.Arrays;
import java.util.Map;

import cn.nukkit.command.Command;
import cn.nukkit.entity.Entity;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.PluginManager;
import ru.jl1mbo.AnarchyCore.Entity.HelperCaveSpider;
import ru.jl1mbo.AnarchyCore.Entity.HelperWitherSkeleton;
import ru.jl1mbo.AnarchyCore.Entity.Bosses.EvokerBoss;
import ru.jl1mbo.AnarchyCore.Entity.Bosses.HuskBoss;
import ru.jl1mbo.AnarchyCore.Entity.Bosses.SpiderBoss;
import ru.jl1mbo.AnarchyCore.Entity.Bosses.WitherSkeletonBoss;
import ru.jl1mbo.AnarchyCore.Entity.NPC.PiglinNPC;
import ru.jl1mbo.AnarchyCore.Entity.NPC.VillagerNPC;
import ru.jl1mbo.AnarchyCore.Entity.NPC.WanderingTraderNPC;
import ru.jl1mbo.AnarchyCore.Manager.FakeInventory.FakeInventoryEventsListener;
import ru.jl1mbo.AnarchyCore.Manager.Forms.FormEventsListener;
import ru.jl1mbo.AnarchyCore.Manager.WorldSystem.WorldEventsListener;
import ru.jl1mbo.AnarchyCore.Manager.WorldSystem.WorldSystemAPI;
import ru.jl1mbo.AnarchyCore.Manager.WorldSystem.TheEnd.TheEndEventsListener;
import ru.jl1mbo.AnarchyCore.Modules.AdminSystem.AdminEventsListener;
import ru.jl1mbo.AnarchyCore.Modules.AdminSystem.Commands.AdminPanelCommand;
import ru.jl1mbo.AnarchyCore.Modules.AdminSystem.Commands.BanCommand;
import ru.jl1mbo.AnarchyCore.Modules.AdminSystem.Commands.MuteCommand;
import ru.jl1mbo.AnarchyCore.Modules.AdminSystem.Commands.SeeInventoryCommand;
import ru.jl1mbo.AnarchyCore.Modules.AdminSystem.Commands.SpectateCommand;
import ru.jl1mbo.AnarchyCore.Modules.AdminSystem.Commands.UnBanCommand;
import ru.jl1mbo.AnarchyCore.Modules.AdminSystem.Commands.UnMuteCommand;
import ru.jl1mbo.AnarchyCore.Modules.AdminSystem.Task.SpectateTask;
import ru.jl1mbo.AnarchyCore.Modules.AntiCheat.AntiCheatEventsListener;
import ru.jl1mbo.AnarchyCore.Modules.Auction.AuctionAPI;
import ru.jl1mbo.AnarchyCore.Modules.Auction.AuctionEventsListener;
import ru.jl1mbo.AnarchyCore.Modules.Auction.Commands.AuctionCommand;
import ru.jl1mbo.AnarchyCore.Modules.Auction.Task.AuctionUpdateTask;
import ru.jl1mbo.AnarchyCore.Modules.Auth.AuthAPI;
import ru.jl1mbo.AnarchyCore.Modules.Auth.AuthEventsListener;
import ru.jl1mbo.AnarchyCore.Modules.BlockProtection.BlockProtectionAPI;
import ru.jl1mbo.AnarchyCore.Modules.BlockProtection.BlockProtectionEventsListener;
import ru.jl1mbo.AnarchyCore.Modules.BlockProtection.Commands.RegionCommand;
import ru.jl1mbo.AnarchyCore.Modules.Clans.ClanEventsListener;
import ru.jl1mbo.AnarchyCore.Modules.Clans.Command.ClanCommand;
import ru.jl1mbo.AnarchyCore.Modules.CombatLogger.CombatEventsListener;
import ru.jl1mbo.AnarchyCore.Modules.CombatLogger.Task.CombatLoggerTask;
import ru.jl1mbo.AnarchyCore.Modules.Commands.BonusCommand;
import ru.jl1mbo.AnarchyCore.Modules.Commands.BorderBuildCommand;
import ru.jl1mbo.AnarchyCore.Modules.Commands.ClearChatCommand;
import ru.jl1mbo.AnarchyCore.Modules.Commands.CoordinateCommand;
import ru.jl1mbo.AnarchyCore.Modules.Commands.DayCommand;
import ru.jl1mbo.AnarchyCore.Modules.Commands.DonateCommand;
import ru.jl1mbo.AnarchyCore.Modules.Commands.FoodCommand;
import ru.jl1mbo.AnarchyCore.Modules.Commands.HealCommand;
import ru.jl1mbo.AnarchyCore.Modules.Commands.NearCommand;
import ru.jl1mbo.AnarchyCore.Modules.Commands.NightCommand;
import ru.jl1mbo.AnarchyCore.Modules.Commands.NightVisionCommand;
import ru.jl1mbo.AnarchyCore.Modules.Commands.RepairCommand;
import ru.jl1mbo.AnarchyCore.Modules.Commands.ReportCommand;
import ru.jl1mbo.AnarchyCore.Modules.Commands.SpawnCommand;
import ru.jl1mbo.AnarchyCore.Modules.Commands.TestCommand;
import ru.jl1mbo.AnarchyCore.Modules.Commands.CraftingTable.CraftingTableCommand;
import ru.jl1mbo.AnarchyCore.Modules.Commands.Defaults.ListCommand;
import ru.jl1mbo.AnarchyCore.Modules.Commands.Defaults.SayCommand;
import ru.jl1mbo.AnarchyCore.Modules.Commands.Defaults.StopCommand;
import ru.jl1mbo.AnarchyCore.Modules.Commands.Defaults.TellCommand;
import ru.jl1mbo.AnarchyCore.Modules.Commands.EnderChest.EnderChestCommand;
import ru.jl1mbo.AnarchyCore.Modules.Commands.Home.Commands.DelHomeCommand;
import ru.jl1mbo.AnarchyCore.Modules.Commands.Home.Commands.HomeCommand;
import ru.jl1mbo.AnarchyCore.Modules.Commands.Home.Commands.SetHomeCommand;
import ru.jl1mbo.AnarchyCore.Modules.Commands.NPC.NPCEventsListener;
import ru.jl1mbo.AnarchyCore.Modules.Commands.NPC.Command.NPCCommand;
import ru.jl1mbo.AnarchyCore.Modules.Commands.Storage.StorageEventsListener;
import ru.jl1mbo.AnarchyCore.Modules.Commands.Storage.Commands.StorageCommand;
import ru.jl1mbo.AnarchyCore.Modules.Commands.Teleport.Commands.RtpCommand;
import ru.jl1mbo.AnarchyCore.Modules.Commands.Teleport.Commands.TpaCommand;
import ru.jl1mbo.AnarchyCore.Modules.Commands.Teleport.Commands.TpcCommand;
import ru.jl1mbo.AnarchyCore.Modules.Commands.Teleport.Task.TpaRequestTask;
import ru.jl1mbo.AnarchyCore.Modules.Cooldown.CooldownEventsListener;
import ru.jl1mbo.AnarchyCore.Modules.Cooldown.Task.CooldownRemoveTask;
import ru.jl1mbo.AnarchyCore.Modules.Economy.Commands.AddMoneyCommand;
import ru.jl1mbo.AnarchyCore.Modules.Economy.Commands.MoneyCommand;
import ru.jl1mbo.AnarchyCore.Modules.Economy.Commands.PayCommand;
import ru.jl1mbo.AnarchyCore.Modules.Economy.Commands.SetMoneyCommand;
import ru.jl1mbo.AnarchyCore.Modules.EventsListener.EventsListener;
import ru.jl1mbo.AnarchyCore.Modules.Permissions.PermissionAPI;
import ru.jl1mbo.AnarchyCore.Modules.Permissions.Commands.GroupCommand;
import ru.jl1mbo.AnarchyCore.Task.BossSpawnTask;
import ru.jl1mbo.AnarchyCore.Task.BroadcastTask;
import ru.jl1mbo.AnarchyCore.Task.ClearTask;
import ru.jl1mbo.AnarchyCore.Task.RestartTask;
import ru.jl1mbo.AnarchyCore.Task.ScoreboardTask;

public class Main extends PluginBase {
	private static Main instance;

	public static Main getInstance() {
		return instance;
	}
	
	private void registerAll() {
		PermissionAPI.register();
		BlockProtectionAPI.register();
		AuctionAPI.register();
		WorldSystemAPI.register();
		BroadcastTask.register();
		this.registerTask();
		this.registerEntity();
		this.registerEvents();
		this.unregisterCommands();
		this.registerCommands();
	}

	private void registerTask() {
		this.getServer().getScheduler().scheduleRepeatingTask(new CombatLoggerTask(), 20);
		this.getServer().getScheduler().scheduleRepeatingTask(new BossSpawnTask(), 60 * 20);
		this.getServer().getScheduler().scheduleRepeatingTask(new SpectateTask(), 20);
		this.getServer().getScheduler().scheduleRepeatingTask(new AuctionUpdateTask(), 20);
		this.getServer().getScheduler().scheduleRepeatingTask(new ClearTask(), 20);
		this.getServer().getScheduler().scheduleRepeatingTask(new TpaRequestTask(), 20);
		this.getServer().getScheduler().scheduleRepeatingTask(new CooldownRemoveTask(), 20);
		this.getServer().getScheduler().scheduleRepeatingTask(new BroadcastTask(), 60 * 20);
		this.getServer().getScheduler().scheduleRepeatingTask(new RestartTask(), 20);
		this.getServer().getScheduler().scheduleRepeatingTask(new ScoreboardTask(), 10 * 20);
	}

	private void registerEntity() {
		Entity.registerEntity(WitherSkeletonBoss.class.getSimpleName(), WitherSkeletonBoss.class);
		Entity.registerEntity(HuskBoss.class.getSimpleName(), HuskBoss.class);
		Entity.registerEntity(SpiderBoss.class.getSimpleName(), SpiderBoss.class);
		Entity.registerEntity(EvokerBoss.class.getSimpleName(), EvokerBoss.class);
		Entity.registerEntity(HelperWitherSkeleton.class.getSimpleName(), HelperWitherSkeleton.class);
		Entity.registerEntity(HelperCaveSpider.class.getSimpleName(), HelperCaveSpider.class);

		Entity.registerEntity(PiglinNPC.class.getSimpleName(), PiglinNPC.class);
		Entity.registerEntity(VillagerNPC.class.getSimpleName(), VillagerNPC.class);
		Entity.registerEntity(WanderingTraderNPC.class.getSimpleName(), WanderingTraderNPC.class);
	}

	@Override()
	public void onEnable() {
		instance = this;
		this.registerAll();
		this.getLogger().info("§l§6AnarchyCore §aАктивирован§7!");
	}

	@Override()
	public void onDisable() {
		this.getLogger().info("§l§6AnarchyCore §cДеактивирован§7!");
		AuctionAPI.saveAuction();
	}

	private void registerEvents() {
		PluginManager pluginManager = this.getServer().getPluginManager();
		pluginManager.registerEvents(new EventsListener(), this);
		pluginManager.registerEvents(new NPCEventsListener(), this);
		pluginManager.registerEvents(new StorageEventsListener(), this);
		pluginManager.registerEvents(new CombatEventsListener(), this);
		pluginManager.registerEvents(new ClanEventsListener(), this);
		pluginManager.registerEvents(new BlockProtectionEventsListener(), this);
		pluginManager.registerEvents(new AuthEventsListener(), this);
		pluginManager.registerEvents(new AuctionEventsListener(), this);
		pluginManager.registerEvents(new AntiCheatEventsListener(), this);
		pluginManager.registerEvents(new AdminEventsListener(), this);
		pluginManager.registerEvents(new FormEventsListener(), this);
		pluginManager.registerEvents(new FakeInventoryEventsListener(), this);
		pluginManager.registerEvents(new WorldEventsListener(), this);
		pluginManager.registerEvents(new CooldownEventsListener(), this);
		
		pluginManager.registerEvents(new TheEndEventsListener(), this);
	}

	private void unregisterCommands() {
		Map<String, Command> commandMap = this.getServer().getCommandMap().getCommands();
		for (String command : new String[]{"say", "help", "me", "?", "tell", "stop", "ban", "unban", "list"}) {
			commandMap.remove(command);
		}
	}
	
	private void registerCommands() {
		this.getServer().getCommandMap().registerAll("", Arrays.asList(new BorderBuildCommand(), new TestCommand(), new BonusCommand(), new SeeInventoryCommand(), new SpectateCommand(), new AdminPanelCommand(), new UnMuteCommand(), new MuteCommand(), new UnBanCommand(), new BanCommand(), new AuctionCommand(), new RegionCommand(), new GroupCommand(), new AddMoneyCommand(), new MoneyCommand(), new PayCommand(), new SetMoneyCommand(), new ClearChatCommand(), new CoordinateCommand(), new CraftingTableCommand(), new DayCommand(), new ListCommand(), new SayCommand(), new TellCommand(), new StopCommand(), new DonateCommand(), new EnderChestCommand(), new FoodCommand(), new HealCommand(), new HomeCommand(), new SetHomeCommand(), new DelHomeCommand(), new NPCCommand(), new NearCommand(), new NightCommand(), new NightVisionCommand(), new RepairCommand(), new ReportCommand(), new SpawnCommand(), new StorageCommand(), new TpaCommand(), new TpcCommand(), new RtpCommand(), new ClanCommand()));
	}
}