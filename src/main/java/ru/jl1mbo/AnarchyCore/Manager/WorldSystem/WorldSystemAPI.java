package ru.jl1mbo.AnarchyCore.Manager.WorldSystem;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import ru.jl1mbo.AnarchyCore.Manager.WorldSystem.EventsListener.EventsListenerWorldSystem;
import ru.jl1mbo.AnarchyCore.Manager.WorldSystem.Task.BorderBuildTask;
import ru.jl1mbo.AnarchyCore.Utils.Utils;

public class WorldSystemAPI {
	private static final Integer[] BORDER = new Integer[] {-2000, 2000, -2000, 2000};
	private static Level MAP;
	private static Level SPAWN;
	private static Level TEST;
	private static Level NETHER;
	private static Level THE_END;

	public static void register() {
		Server.getInstance().getScheduler().scheduleRepeatingTask(new BorderBuildTask(), 20);
		EventsListenerWorldSystem.register();
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

	public static void randomPosition(Entity entity) {
		new Thread(() -> {entity.teleport(findRandomPosition(new Position(0, 0, 0, MAP)));}).start();
	}

	public static void randomPosition(Player player) {
		new Thread(() -> {player.teleport(findRandomPosition(new Position(0, 0, 0, MAP)));}).start();
	}

	private static Position findRandomPosition(Position position) {
		int x = Utils.rand(BORDER[0], BORDER[1]);
		int z = Utils.rand(BORDER[2], BORDER[3]);
		position.setComponents(x, 67, z);
		int chunkX = position.getChunkX();
		int chunkZ = position.getChunkZ();
		while (!position.getLevel().isChunkGenerated(chunkX, chunkZ) || !position.getLevel().isChunkLoaded(chunkX, chunkZ)) {
			position.getLevel().generateChunk(chunkX, chunkZ, true);
			position.getLevel().loadChunk(chunkX, chunkZ, true);
		}
		for (int i = 60; i < 120; i++) {
			position.setComponents(x, i, z);
			Block ground = position.getLevel().getBlock(position.add(0, -1, 0));
			Block body = position.getLevel().getBlock(position.setComponents(x, i + 1, z));
			Block head = position.getLevel().getBlock(position.setComponents(x, i + 2, z));
			if (head.getId() == 0 && body.getId() == 0) {
				if (ground.getId() == Block.WATER || ground.getId() == Block.STILL_WATER && !ground.isSolid()) {
					return findRandomPosition(position);
				}
				return position.setComponents(x + 0.5, i + 1, z + 0.5);
			}
		}
		return findRandomPosition(position);
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