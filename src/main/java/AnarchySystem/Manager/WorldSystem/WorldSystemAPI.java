package AnarchySystem.Manager.WorldSystem;

import AnarchySystem.Main;
import AnarchySystem.Manager.WorldSystem.EventsListener.BlockBreakListener;
import AnarchySystem.Manager.WorldSystem.EventsListener.BlockBurnListener;
import AnarchySystem.Manager.WorldSystem.EventsListener.BlockIgniteListener;
import AnarchySystem.Manager.WorldSystem.EventsListener.BlockPlaceListener;
import AnarchySystem.Manager.WorldSystem.EventsListener.EntityDamageListener;
import AnarchySystem.Manager.WorldSystem.EventsListener.ItemFrameDropItemListener;
import AnarchySystem.Manager.WorldSystem.EventsListener.LeavesDecayListener;
import AnarchySystem.Manager.WorldSystem.EventsListener.PlayerBucketEmptyListener;
import AnarchySystem.Manager.WorldSystem.EventsListener.PlayerDropItemListener;
import AnarchySystem.Manager.WorldSystem.EventsListener.PlayerFoodLevelChangeListener;
import AnarchySystem.Manager.WorldSystem.EventsListener.PlayerInteractListener;
import AnarchySystem.Manager.WorldSystem.EventsListener.ProjectileLaunchListener;
import cn.nukkit.Server;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.Level;
import cn.nukkit.plugin.PluginManager;

public class WorldSystemAPI {
	private static Level MAP;
	private static Level SPAWN;
	private static Level TEST;
	private static Level NETHER;
	private static Level THE_END;

	public static void register() {
		registerEvents();
		Server.getInstance().loadLevel("map");
		Server.getInstance().loadLevel("spawn");
		Server.getInstance().loadLevel("test");
		Server.getInstance().loadLevel("nether");
		Server.getInstance().loadLevel("the_end");
		MAP = Server.getInstance().getLevelByName("map");
		SPAWN = Server.getInstance().getLevelByName("spawn");
		TEST = Server.getInstance().getLevelByName("test");
		NETHER = Server.getInstance().getLevelByName("nether");
		THE_END = Server.getInstance().getLevelByName("the_end");
		MAP.getGameRules().setGameRule(GameRule.SHOW_COORDINATES, true);
		MAP.getGameRules().setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
		SPAWN.getGameRules().setGameRule(GameRule.SHOW_COORDINATES, false);
		NETHER.getGameRules().setGameRule(GameRule.SHOW_COORDINATES, true);
		NETHER.getGameRules().setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
		THE_END.getGameRules().setGameRule(GameRule.SHOW_COORDINATES, true);
		THE_END.getGameRules().setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
	}

	private static void registerEvents() {
		PluginManager pluginManager = Server.getInstance().getPluginManager();
		pluginManager.registerEvents(new BlockBreakListener(), Main.getInstance());
		pluginManager.registerEvents(new BlockBurnListener(), Main.getInstance());
		pluginManager.registerEvents(new BlockIgniteListener(), Main.getInstance());
		pluginManager.registerEvents(new BlockPlaceListener(), Main.getInstance());
		pluginManager.registerEvents(new EntityDamageListener(), Main.getInstance());
		pluginManager.registerEvents(new ItemFrameDropItemListener(), Main.getInstance());
		pluginManager.registerEvents(new LeavesDecayListener(), Main.getInstance());
		pluginManager.registerEvents(new PlayerBucketEmptyListener(), Main.getInstance());
		pluginManager.registerEvents(new PlayerDropItemListener(), Main.getInstance());
		pluginManager.registerEvents(new PlayerFoodLevelChangeListener(), Main.getInstance());
		pluginManager.registerEvents(new PlayerInteractListener(), Main.getInstance());
		pluginManager.registerEvents(new ProjectileLaunchListener(), Main.getInstance());
	}

	public static Level getTest() {
		return TEST;
	}

	public static Level getMap() {
		return MAP;
	}

	public static Level getNether() {
		return NETHER;
	}

	public static Level getSpawn() {
		return SPAWN;
	}

	public static Level getTheEnd() {
		return THE_END;
	}
}