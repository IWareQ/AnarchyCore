package ru.iwareq.anarchycore.manager.WorldSystem;

import cn.nukkit.Server;
import cn.nukkit.block.BlockID;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.level.generator.Nether;
import cn.nukkit.level.generator.Normal;
import cn.nukkit.math.Vector3;
import ru.iwareq.anarchycore.util.Utils;

import java.util.Random;
import java.util.function.Consumer;

public class WorldSystemAPI {

	private static final Integer[] RTP_RADIUS = new Integer[]{-1500, 1500, -1500, 1500};
	private static final String[] levels = new String[]{"map", "spawn", "test", "nether", "the_end"};
	public static Level Map;
	public static Level Spawn;
	public static Level Test;
	public static Level Nether;
	public static Level TheEnd;

	public static void register() {
		for (String levelName : levels) {
			if (levelName.equalsIgnoreCase("nether")) {
				Server.getInstance().generateLevel(levelName, new Random().nextLong(), Nether.class);
			} else {
				Server.getInstance().generateLevel(levelName, new Random().nextLong(), Normal.class);
			}
		}

		for (String levelName : levels) {
			Server.getInstance().loadLevel(levelName);
			if (levelName.startsWith("map")) {
				Map = Server.getInstance().getLevelByName(levelName);
					/*if (Map.getDimension() != Level.DIMENSION_OVERWORLD) {
						Map.setDimension(Level.DIMENSION_OVERWORLD);
					}*/
				Map.getGameRules().setGameRule(GameRule.SHOW_COORDINATES, true);
				Map.getGameRules().setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
			} else if (levelName.startsWith("spawn")) {
				Spawn = Server.getInstance().getLevelByName(levelName);
					/*if (Spawn.getDimension() != Level.DIMENSION_OVERWORLD) {
						Spawn.setDimension(Level.DIMENSION_OVERWORLD);
					}*/
				Spawn.getGameRules().setGameRule(GameRule.SPAWN_RADIUS, 0);
				Spawn.getGameRules().setGameRule(GameRule.SHOW_COORDINATES, false);
				Spawn.getGameRules().setGameRule(GameRule.DO_MOB_SPAWNING, false);

				Spawn.setTime(4200);
				Spawn.stopTime();

				Spawn.setRaining(false);
				Spawn.setThundering(false);
			} else if (levelName.startsWith("test")) {
				Test = Server.getInstance().getLevelByName(levelName);
					/*if (Test.getDimension() != Level.DIMENSION_NETHER) {
						Test.setDimension(Level.DIMENSION_OVERWORLD);
					}*/
			} else if (levelName.startsWith("nether")) {
				Nether = Server.getInstance().getLevelByName(levelName);
					/*if (Nether.getDimension() != Level.DIMENSION_NETHER) {
						Nether.setDimension(Level.DIMENSION_NETHER);
					}*/
				Nether.getGameRules().setGameRule(GameRule.SHOW_COORDINATES, true);
				Nether.getGameRules().setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
			} else if (levelName.startsWith("the_end")) {
				TheEnd = Server.getInstance().getLevelByName(levelName);
					/*if (TheEnd.getDimension() != Level.DIMENSION_THE_END) {
						TheEnd.setDimension(Level.DIMENSION_THE_END);
					}*/
				TheEnd.getGameRules().setGameRule(GameRule.SHOW_COORDINATES, true);
				TheEnd.getGameRules().setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);

				TheEnd.setSpawnLocation(new Vector3(100, 49, 0));
			}
		}
	}

	public static void generateTheEndPlatform() {
		Position pos = new Position(100, 48, 0, TheEnd);
		int x = pos.getFloorX();
		int y = pos.getFloorY();
		int z = pos.getFloorZ();
		for (int xx = x - 2; xx < x + 3; xx++) {
			for (int zz = z - 2; zz < z + 3; zz++) {
				TheEnd.setBlockAt(xx, y - 1, zz, BlockID.OBSIDIAN);
				for (int yy = y; yy < y + 4; yy++) {
					TheEnd.setBlockAt(xx, yy, zz, BlockID.AIR);
				}
			}
		}
	}

	public static void randomPosition(Level level, Consumer<Position> callback) {
		callback.accept(findRandomPosition(new Position(0, 60, 0, level)));
	}

	private static Position findRandomPosition(Position position) {
		int x = Utils.rand(RTP_RADIUS[0], RTP_RADIUS[1]);
		int z = Utils.rand(RTP_RADIUS[2], RTP_RADIUS[3]);

		int chunkX = position.getChunkX();
		int chunkZ = position.getChunkZ();

		Level level = position.getLevel();

		// generate and load chunk
		level.getChunk(chunkX, chunkZ, true);

		int y = level.getHeightMap(x, z);

		int blockId = level.getBlockIdAt(x, y, z);
		if (y == 0 || blockId == 0) {
			return findRandomPosition(position);
		}

		if (blockId == BlockID.WATER || blockId == BlockID.STILL_WATER ||
				blockId == BlockID.LAVA || blockId == BlockID.STILL_LAVA) {
			return findRandomPosition(position);
		}

		return position.setComponents(x + 0.5, y + 1, z + 0.5);
	}
}
