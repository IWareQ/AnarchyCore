package ru.jl1mbo.AnarchyCore.Manager.WorldSystem;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.plugin.PluginManager;
import ru.jl1mbo.AnarchyCore.Main;
import ru.jl1mbo.AnarchyCore.Manager.WorldSystem.EventsListener.BlockBreakListener;
import ru.jl1mbo.AnarchyCore.Manager.WorldSystem.EventsListener.BlockBurnListener;
import ru.jl1mbo.AnarchyCore.Manager.WorldSystem.EventsListener.BlockIgniteListener;
import ru.jl1mbo.AnarchyCore.Manager.WorldSystem.EventsListener.BlockPlaceListener;
import ru.jl1mbo.AnarchyCore.Manager.WorldSystem.EventsListener.EntityDamageListener;
import ru.jl1mbo.AnarchyCore.Manager.WorldSystem.EventsListener.ItemFrameDropItemListener;
import ru.jl1mbo.AnarchyCore.Manager.WorldSystem.EventsListener.LeavesDecayListener;
import ru.jl1mbo.AnarchyCore.Manager.WorldSystem.EventsListener.PlayerBucketEmptyListener;
import ru.jl1mbo.AnarchyCore.Manager.WorldSystem.EventsListener.PlayerDropItemListener;
import ru.jl1mbo.AnarchyCore.Manager.WorldSystem.EventsListener.PlayerFoodLevelChangeListener;
import ru.jl1mbo.AnarchyCore.Manager.WorldSystem.EventsListener.ProjectileLaunchListener;
import ru.jl1mbo.AnarchyCore.Utils.Utils;

public class WorldSystemAPI {
	private static final Integer[] RANDOM_TP = new Integer[] {-1000, 1000, -1000, 1000};
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
		pluginManager.registerEvents(new ProjectileLaunchListener(), Main.getInstance());
	}

	public static void findRandomSafePosition(Level level, Consumer<Position> callback) {
		CompletableFuture.runAsync(() -> callback.accept(randomPos(new Position(0, 0, 0, level))));
	}

	private static Position randomPos(Position base) {
		int x = Utils.rand(RANDOM_TP[0], RANDOM_TP[1]);
		int z = Utils.rand(RANDOM_TP[2], RANDOM_TP[3]);
		base.setComponents(x, 67, z);
		int cx = base.getChunkX();
		int cz = base.getChunkZ();
		while (!base.getLevel().isChunkGenerated(cx, cz) || !base.getLevel().isChunkLoaded(cx, cz)) {
			base.getLevel().generateChunk(cx, cz, true);
			base.getLevel().loadChunk(cx, cz, true);
		}
		for (int i = 60; i < 120; i++) {
			base.setComponents(x, i, z);
			Block ground = base.getLevel().getBlock(base);
			Block body = base.getLevel().getBlock(base.setComponents(x, i + 1, z));
			Block head = base.getLevel().getBlock(base.setComponents(x, i + 2, z));
			if (head.getId() == 0 && body.getId() == 0) {
				if (ground.getId() != Block.LAVA || ground.getId() != Block.STILL_LAVA && ground.isSolid()) {
					return base.setComponents(x + 0.5, i + 1, z + 0.5);
				}
			}
		}
		return randomPos(base);
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