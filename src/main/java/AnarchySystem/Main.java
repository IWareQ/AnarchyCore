package AnarchySystem;

import java.util.Arrays;
import java.util.Map;

import AnarchySystem.Components.Scoreboard.ScoreboardTask;
import AnarchySystem.Components.StorageItems.Commands.StorageCommand;
import AnarchySystem.Components.Bosses.Bosses.EvokerBoss;
import AnarchySystem.Components.Bosses.Bosses.HuskBoss;
import AnarchySystem.Components.Bosses.Bosses.SpiderBoss;
import AnarchySystem.Components.Bosses.Bosses.WitherSkeletonBoss;
import AnarchySystem.Components.Bosses.Entity.HelperCaveSpider;
import AnarchySystem.Components.Bosses.Entity.HelperWitherSkeleton;
import AnarchySystem.Components.Bosses.Items.BlazeRodArtifact;
import AnarchySystem.Components.Bosses.Items.CobwebArtifact;
import AnarchySystem.Components.Bosses.Items.IceFrostedArtifact;
import AnarchySystem.Components.Bosses.Items.SkullArtifact;
import AnarchySystem.Components.Commands.BonusCommand;
import AnarchySystem.Components.Commands.ClearChatCommand;
import AnarchySystem.Components.Commands.CoordinateCommand;
import AnarchySystem.Components.Commands.DayCommand;
import AnarchySystem.Components.Commands.DonateCommand;
import AnarchySystem.Components.Commands.FoodCommand;
import AnarchySystem.Components.Commands.GamemodeCommand;
import AnarchySystem.Components.Commands.HealCommand;
import AnarchySystem.Components.Commands.NearCommand;
import AnarchySystem.Components.Commands.NightCommand;
import AnarchySystem.Components.Commands.NightVisionCommand;
import AnarchySystem.Components.Commands.RepairCommand;
import AnarchySystem.Components.Commands.ReportCommand;
import AnarchySystem.Components.Commands.SpawnCommand;
import AnarchySystem.Components.Commands.CraftingTable.CraftingTableCommand;
import AnarchySystem.Components.Commands.Defaults.ListCommand;
import AnarchySystem.Components.Commands.Defaults.SayCommand;
import AnarchySystem.Components.Commands.Defaults.TellCommand;
import AnarchySystem.Components.Commands.EnderChest.EnderChestCommand;
import AnarchySystem.Components.Commands.Shop.ShopHandler;
import AnarchySystem.Components.Commands.Test.TestCommand;
import AnarchySystem.Components.Commands.Test.TestEntity;
import AnarchySystem.Components.Commands.WorldBorder.BorderBuildCommand;
import AnarchySystem.Components.NPC.Entity.PiglinNPC;
import AnarchySystem.Components.NPC.Entity.VillagerNPC;
import AnarchySystem.Components.NPC.Entity.WanderingTraderNPC;
import AnarchySystem.Components.NPC.Item.GoldenMoneyItem;
import AnarchySystem.Components.Permissions.PermissionAPI;
import AnarchySystem.Manager.FakeInventory.FakeInventoryAPI;
import AnarchySystem.Manager.Forms.FormsAPI;
import AnarchySystem.Manager.WorldSystem.WorldSystemAPI;
import cn.nukkit.command.Command;
import cn.nukkit.entity.Entity;
import cn.nukkit.item.Item;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.PluginManager;
import ru.dragonestia.ironlib.IronLib;
import ru.dragonestia.ironlib.item.PrefabManager;

public class Main extends PluginBase {
	private static Main instance;
	
	public static Main getInstance() {
		return instance;
	}
	
	private void registerItems() {
        PrefabManager prefabManager = IronLib.getInstance().getPrefabManager();
        prefabManager.registerItem(new CobwebArtifact(Item.get(Item.COBWEB)));
        prefabManager.registerItem(new BlazeRodArtifact(Item.get(Item.BLAZE_ROD)));
        prefabManager.registerItem(new IceFrostedArtifact(Item.get(Item.ICE_FROSTED)));
        prefabManager.registerItem(new SkullArtifact(Item.get(Item.SKULL)));
        prefabManager.registerItem(new GoldenMoneyItem(Item.get(Item.GOLD_NUGGET)));
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
		this.registerEvents();
		this.unregisterCommands();
		this.registerCommands();
		WorldSystemAPI.register();
		PermissionAPI.register();
		Entity.registerEntity(TestEntity.class.getSimpleName(), TestEntity.class);
		this.getServer().getScheduler().scheduleRepeatingTask(new ScoreboardTask(), 60 * 20);
		this.getLogger().info("§l§fПлагин §aАктивирован§7!");
	}

	@Override()
	public void onDisable() {
		this.getLogger().info("§l§fПлагин §cДеактивирован§7!");
	}

	private void registerEvents() {
		FakeInventoryAPI.registerEvents();
		FormsAPI.registerEvents();
		PluginManager pluginManager = this.getServer().getPluginManager();
	}

	private void unregisterCommands() {
		Map<String, Command> commandMap = this.getServer().getCommandMap().getCommands();
		for (String command : new String[] {"me", "ver", "say", "pl", "plugins", "mixer", "difficulty", "defaultgamemode", "help", "?", "pardon", "particle", "tell", "gm", "gamemode", "list", "about", "title"}) {
			commandMap.remove(command);
		}
	}

	private void registerCommands() {
		Command[] commands = new Command[] {new BorderBuildCommand(), new TestCommand(), new BonusCommand(), new SayCommand(), new StorageCommand(), new ShopHandler(), new SpawnCommand(), new NightVisionCommand(), new CraftingTableCommand(), new ReportCommand(), new ClearChatCommand(), new ListCommand(), new TellCommand(), new EnderChestCommand(), new CoordinateCommand(), new HealCommand(), new NightCommand(), new DayCommand(), new FoodCommand(), new NearCommand(), new DonateCommand(), new RepairCommand(), new GamemodeCommand()};
		this.getServer().getCommandMap().registerAll("", Arrays.asList(commands));
	}
}