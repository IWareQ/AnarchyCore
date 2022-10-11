package ru.iwareq.anarchycore;

import cn.nukkit.command.Command;
import cn.nukkit.entity.Entity;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.PluginManager;
import cn.nukkit.scheduler.ServerScheduler;
import lombok.Getter;
import ru.iwareq.anarchycore.entity.Bosses.EvokerBoss;
import ru.iwareq.anarchycore.entity.Bosses.HuskBoss;
import ru.iwareq.anarchycore.entity.Bosses.SpiderBoss;
import ru.iwareq.anarchycore.entity.Bosses.WitherSkeletonBoss;
import ru.iwareq.anarchycore.entity.HelperCaveSpider;
import ru.iwareq.anarchycore.entity.HelperWitherSkeleton;
import ru.iwareq.anarchycore.entity.NPC.PiglinNPC;
import ru.iwareq.anarchycore.entity.NPC.VillagerNPC;
import ru.iwareq.anarchycore.entity.NPC.WanderingTraderNPC;
import ru.iwareq.anarchycore.manager.FakeInventory.FakeInventoryEventsListener;
import ru.iwareq.anarchycore.manager.Forms.FormEventsListener;
import ru.iwareq.anarchycore.manager.WorldSystem.TheEnd.TheEndEventsListener;
import ru.iwareq.anarchycore.manager.WorldSystem.WorldEventsListener;
import ru.iwareq.anarchycore.manager.WorldSystem.WorldSystemAPI;
import ru.iwareq.anarchycore.module.AdminSystem.AdminEventsListener;
import ru.iwareq.anarchycore.module.AdminSystem.Commands.AdminPanelCommand;
import ru.iwareq.anarchycore.module.AdminSystem.Commands.BanCommand;
import ru.iwareq.anarchycore.module.AdminSystem.Commands.CheatCheckCommand;
import ru.iwareq.anarchycore.module.AdminSystem.Commands.CheatCheckTutorialCommand;
import ru.iwareq.anarchycore.module.AdminSystem.Commands.MuteCommand;
import ru.iwareq.anarchycore.module.AdminSystem.Commands.SeeInventoryCommand;
import ru.iwareq.anarchycore.module.AdminSystem.Commands.SpectateCommand;
import ru.iwareq.anarchycore.module.AdminSystem.Commands.UnBanCommand;
import ru.iwareq.anarchycore.module.AdminSystem.Commands.UnMuteCommand;
import ru.iwareq.anarchycore.module.AdminSystem.Task.SpectateTask;
import ru.iwareq.anarchycore.module.AntiCheat.AntiCheatEventsListener;
import ru.iwareq.anarchycore.module.Auction.Auction;
import ru.iwareq.anarchycore.module.Auction.AuctionEventsListener;
import ru.iwareq.anarchycore.module.Auction.Commands.AuctionCommand;
import ru.iwareq.anarchycore.module.Auction.Task.AuctionUpdateTask;
import ru.iwareq.anarchycore.module.Auth.AuthAPI;
import ru.iwareq.anarchycore.module.Auth.AuthEventsListener;
import ru.iwareq.anarchycore.module.BlockProtection.BlockProtectionAPI;
import ru.iwareq.anarchycore.module.BlockProtection.BlockProtectionEventsListener;
import ru.iwareq.anarchycore.module.BlockProtection.Commands.RegionCommand;
import ru.iwareq.anarchycore.module.Clans.ClanEventsListener;
import ru.iwareq.anarchycore.module.Clans.Command.ClanCommand;
import ru.iwareq.anarchycore.module.CombatLogger.CombatEventsListener;
import ru.iwareq.anarchycore.module.CombatLogger.Task.CombatLoggerTask;
import ru.iwareq.anarchycore.module.Commands.BarCommand;
import ru.iwareq.anarchycore.module.Commands.BonusCommand;
import ru.iwareq.anarchycore.module.Commands.BorderBuildCommand;
import ru.iwareq.anarchycore.module.Commands.ClearChatCommand;
import ru.iwareq.anarchycore.module.Commands.CoordinateCommand;
import ru.iwareq.anarchycore.module.Commands.CraftingTable.CraftingTableCommand;
import ru.iwareq.anarchycore.module.Commands.DayCommand;
import ru.iwareq.anarchycore.module.Commands.Defaults.ListCommand;
import ru.iwareq.anarchycore.module.Commands.Defaults.SayCommand;
import ru.iwareq.anarchycore.module.Commands.Defaults.StopCommand;
import ru.iwareq.anarchycore.module.Commands.Defaults.TellCommand;
import ru.iwareq.anarchycore.module.Commands.DisenchantCommand;
import ru.iwareq.anarchycore.module.Commands.DonateCommand;
import ru.iwareq.anarchycore.module.Commands.EnderChest.EnderChestCommand;
import ru.iwareq.anarchycore.module.Commands.FoodCommand;
import ru.iwareq.anarchycore.module.Commands.GarbageCommand;
import ru.iwareq.anarchycore.module.Commands.HealCommand;
import ru.iwareq.anarchycore.module.Commands.HelpCommand;
import ru.iwareq.anarchycore.module.Commands.HideGlobalChatCommand;
import ru.iwareq.anarchycore.module.Commands.Home.Commands.DelHomeCommand;
import ru.iwareq.anarchycore.module.Commands.Home.Commands.HomeCommand;
import ru.iwareq.anarchycore.module.Commands.Home.Commands.SetHomeCommand;
import ru.iwareq.anarchycore.module.Commands.Home.HomeAPI;
import ru.iwareq.anarchycore.module.Commands.NPC.Command.NPCCommand;
import ru.iwareq.anarchycore.module.Commands.NPC.NPCEventsListener;
import ru.iwareq.anarchycore.module.Commands.NearCommand;
import ru.iwareq.anarchycore.module.Commands.NightCommand;
import ru.iwareq.anarchycore.module.Commands.NightVisionCommand;
import ru.iwareq.anarchycore.module.Commands.RepairCommand;
import ru.iwareq.anarchycore.module.Commands.ReportCommand;
import ru.iwareq.anarchycore.module.Commands.RulesCommand;
import ru.iwareq.anarchycore.module.Commands.SpawnCommand;
import ru.iwareq.anarchycore.module.Commands.Storage.Commands.StorageCommand;
import ru.iwareq.anarchycore.module.Commands.Storage.StorageEventsListener;
import ru.iwareq.anarchycore.module.Commands.SunnyCommand;
import ru.iwareq.anarchycore.module.Commands.Teleport.Commands.RtpCommand;
import ru.iwareq.anarchycore.module.Commands.Teleport.Commands.TpaCommand;
import ru.iwareq.anarchycore.module.Commands.Teleport.Commands.TpcCommand;
import ru.iwareq.anarchycore.module.Commands.Teleport.Commands.TpdCommand;
import ru.iwareq.anarchycore.module.Commands.Teleport.Commands.TpdeclineCommand;
import ru.iwareq.anarchycore.module.Commands.Teleport.Task.TpaRequestTask;
import ru.iwareq.anarchycore.module.Commands.TestCommand;
import ru.iwareq.anarchycore.module.Cooldown.CooldownEventsListener;
import ru.iwareq.anarchycore.module.Cooldown.Task.CooldownRemoveTask;
import ru.iwareq.anarchycore.module.Economy.Commands.AddMoneyCommand;
import ru.iwareq.anarchycore.module.Economy.Commands.MoneyCommand;
import ru.iwareq.anarchycore.module.Economy.Commands.PayCommand;
import ru.iwareq.anarchycore.module.Economy.Commands.SetMoneyCommand;
import ru.iwareq.anarchycore.module.EventsListener.EventsListener;
import ru.iwareq.anarchycore.module.Permissions.Commands.GroupCommand;
import ru.iwareq.anarchycore.module.Permissions.PermissionAPI;
import ru.iwareq.anarchycore.task.BossSpawnTask;
import ru.iwareq.anarchycore.task.BroadcastTask;
import ru.iwareq.anarchycore.task.ClearTask;
import ru.iwareq.anarchycore.task.RestartTask;
import ru.iwareq.anarchycore.task.ScoreboardTask;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Stream;

@Getter
public class AnarchyCore extends PluginBase {

	@Getter
	private static AnarchyCore instance;

	@Getter
	private ClearTask clearTask;

	@Override()
	public void onEnable() {
		AnarchyCore.instance = this;

		this.registerAll();
	}

	@SuppressWarnings("InstantiationOfUtilityClass")
	private void registerAll() {
		new AuthAPI();
		new HomeAPI();

		Auction.load();

		PermissionAPI.register();
		BlockProtectionAPI.register();
		WorldSystemAPI.register();
		BroadcastTask.register();
		this.registerTask();
		this.registerEntity();
		this.registerEvents();
		this.unregisterCommands();
		this.registerCommands();
	}

	private void registerTask() {
		ServerScheduler scheduler = this.getServer().getScheduler();
		scheduler.scheduleRepeatingTask(new CombatLoggerTask(), 20);
		scheduler.scheduleRepeatingTask(new BossSpawnTask(), 60 * 20);
		scheduler.scheduleRepeatingTask(new SpectateTask(), 20);
		scheduler.scheduleRepeatingTask(new AuctionUpdateTask(), 20);
		scheduler.scheduleRepeatingTask(this.clearTask = new ClearTask(), 20);
		scheduler.scheduleRepeatingTask(new TpaRequestTask(), 20);
		scheduler.scheduleRepeatingTask(new CooldownRemoveTask(), 20);
		scheduler.scheduleRepeatingTask(new BroadcastTask(), 60 * 20);
		scheduler.scheduleRepeatingTask(new RestartTask(), 20);
		scheduler.scheduleRepeatingTask(new ScoreboardTask(this), 20);
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
		Stream.of("say", "help", "me", "?", "tell", "stop", "ban", "unban", "list").forEach(commandMap::remove);
	}

	private void registerCommands() {
		this.getServer().getCommandMap().registerAll("", Arrays.asList(
				new SunnyCommand(),
				new HelpCommand(),
				new RulesCommand(),
				new GarbageCommand(),
				new HideGlobalChatCommand(),
				new TpdeclineCommand(),
				new DisenchantCommand(),
				new BarCommand(), new CheatCheckCommand(),
				new BorderBuildCommand(), new TestCommand(), new BonusCommand(), new SeeInventoryCommand(),
				new SpectateCommand(), new AdminPanelCommand(), new UnMuteCommand(), new MuteCommand(),
				new UnBanCommand(), new BanCommand(), new AuctionCommand(), new RegionCommand(), new GroupCommand(),
				new AddMoneyCommand(), new MoneyCommand(), new PayCommand(), new SetMoneyCommand(), new ClearChatCommand(), new CoordinateCommand(), new CraftingTableCommand(), new DayCommand(), new ListCommand(), new SayCommand(), new TellCommand(), new StopCommand(), new DonateCommand(), new EnderChestCommand(), new FoodCommand(), new HealCommand(), new HomeCommand(), new SetHomeCommand(), new DelHomeCommand(), new NPCCommand(), new NearCommand(), new NightCommand(), new NightVisionCommand(), new RepairCommand(), new ReportCommand(), new SpawnCommand(), new StorageCommand(), new TpaCommand(), new TpdCommand(), new TpcCommand(), new RtpCommand(), new CheatCheckTutorialCommand(), new ClanCommand()));
	}
}
