package ru.jl1mbo.AnarchyCore.Manager.WorldSystem;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockID;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.math.Vector3;
import ru.jl1mbo.AnarchyCore.Modules.Commands.NPC.NPC;
import ru.jl1mbo.AnarchyCore.Utils.Utils;

public class WorldSystemAPI {
	private static final Integer[] RTP_RADIUS = new Integer[] {-1500, 1500, -1500, 1500};
	private static final String[] levels = new String[] {"map", "spawn", "test", "nether", "the_end"};
	public static Level Map;
	public static Level Spawn;
	public static Level Test;
	public static Level Nether;
	public static Level TheEnd;

	public static void register() {
		for (String levelName : levels) {
			if (!Server.getInstance().loadLevel(levelName)) {
				Server.getInstance().getLogger().error("Мир §6" + levelName + " §fне найден§7!");
			}
			if (Server.getInstance().isLevelLoaded(levelName)) {
				if (levelName.startsWith("map")) {
					Map = Server.getInstance().getLevelByName(levelName);
					if (Map.getDimension() != Level.DIMENSION_OVERWORLD) {
						Map.setDimension(Level.DIMENSION_OVERWORLD);
					}
					Map.getGameRules().setGameRule(GameRule.SHOW_COORDINATES, true);
					Map.getGameRules().setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
				} else if (levelName.startsWith("spawn")) {
					Spawn = Server.getInstance().getLevelByName(levelName);
					if (Spawn.getDimension() != Level.DIMENSION_OVERWORLD) {
						Spawn.setDimension(Level.DIMENSION_OVERWORLD);
					}
					Spawn.getGameRules().setGameRule(GameRule.SPAWN_RADIUS, 0);
					Spawn.getGameRules().setGameRule(GameRule.SHOW_COORDINATES, false);

					Spawn.setTime(4200);
					Spawn.stopTime();

					Spawn.setRaining(false);
					Spawn.setThundering(false);
				} else if (levelName.startsWith("test")) {
					Test = Server.getInstance().getLevelByName(levelName);
					if (Test.getDimension() != Level.DIMENSION_NETHER) {
						Test.setDimension(Level.DIMENSION_OVERWORLD);
					}
				} else if (levelName.startsWith("nether")) {
					Nether = Server.getInstance().getLevelByName(levelName);
					if (Nether.getDimension() != Level.DIMENSION_NETHER) {
						Nether.setDimension(Level.DIMENSION_NETHER);
					}
					Nether.getGameRules().setGameRule(GameRule.SHOW_COORDINATES, true);
					Nether.getGameRules().setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
				} else if (levelName.startsWith("the_end")) {
					TheEnd = Server.getInstance().getLevelByName(levelName);
					if (TheEnd.getDimension() != Level.DIMENSION_THE_END) {
						TheEnd.setDimension(Level.DIMENSION_THE_END);
					}
					TheEnd.getGameRules().setGameRule(GameRule.SHOW_COORDINATES, true);
					TheEnd.getGameRules().setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);

					TheEnd.setSpawnLocation(new Vector3(100, 49, 0));
				}
			}
		}
	}

	public static void generateTheEndPlatform() {
		Position pos = new Position(100, 48, 0, TheEnd);
		int x = pos.getFloorX();
		int y = pos.getFloorY();
		int z = pos.getFloorZ();
		for (int xx = x - 2; xx < x + 3; xx++) {
			for (int zz = z - 2; zz < z + 3; zz++)  {
				TheEnd.setBlockAt(xx, y - 1, zz, BlockID.OBSIDIAN);
				for (int yy = y; yy < y + 4; yy++) {
					TheEnd.setBlockAt(xx, yy, zz, BlockID.AIR);
				}
			}
		}
	}

	public static void generateTheEndPortal() {
		Position pos = new Position(0, 64, 0, TheEnd);
		for (int x = -4; x <= 4; x ++) {
			for (int z = -4; z <= 4; z++) {
				TheEnd.setBlockAt(x, pos.getFloorY(), z, BlockID.BEDROCK);
			}
		}
		/*for (int i = -1; i <= 100; i++) {
			for (int x = -4; x <= 4; x++) {
				for (int z = -4; z <= 4; z++) {
					int dy = pos.getFloorY() + i;
					double distance = new Vector3(0, pos.y).distance(new Vector3(x, dy, z));
					if (distance <= 3.5) {
						if (dy < pos.y) {
							if (distance <= 2.5) {
								TheEnd.setBlockAt(x, dy, z, BlockID.BEDROCK);
							} else if (dy < pos.y) {
								TheEnd.setBlockAt(x, dy, z, BlockID.END_STONE);
							}
						} else if (dy > pos.y) {
							TheEnd.setBlockAt(x, dy, z, BlockID.AIR);
						} else if (distance > 2.5) {
							TheEnd.setBlockAt(x, dy, z, BlockID.BEDROCK);
						} else {
							TheEnd.setBlockAt(x, dy, z, BlockID.AIR);
							TheEnd.setBlockAt(x, dy, z, BlockID.END_PORTAL);
						}
					}
				}
			}
		}*/

		for (int i = 0; i < 4; ++i) {
			TheEnd.setBlockAt(0, pos.getFloorY() + i, 0, BlockID.BEDROCK);
		}
		int torch = pos.getFloorY() + 2;
		TheEnd.setBlockAt(1, torch, 0, BlockID.TORCH, 1);
		TheEnd.setBlockAt(-1, torch, 0, BlockID.TORCH, 2);
		TheEnd.setBlockAt(0, torch, 1, BlockID.TORCH, 3);
		TheEnd.setBlockAt(0, torch, -1, BlockID.TORCH, 4);
	}

	public static void randomPosition(Level level, Consumer<Position> callback) {
		CompletableFuture.runAsync(() -> callback.accept(findRandomPosition(new Position(0, 0, 0, level))));
	}

	private static Position findRandomPosition(Position position) {
		int x = Utils.rand(RTP_RADIUS[0], RTP_RADIUS[1]);
		int z = Utils.rand(RTP_RADIUS[2], RTP_RADIUS[3]);
		position.setComponents(x, 67, z);
		int chunkX = position.getChunkX();
		int chunkZ = position.getChunkZ();
		while (!position.getLevel().isChunkLoaded(chunkX, chunkZ)) {
			position.getLevel().loadChunk(chunkX, chunkZ, true);
		}
		for (int i = 60; i < 120; i++) {
			position.setComponents(x, i, z);
			Block ground = position.getLevel().getBlock(position.add(0, -1, 0));
			Block body = position.getLevel().getBlock(position.setComponents(x, i + 1, z));
			Block head = position.getLevel().getBlock(position.setComponents(x, i + 2, z));
			if (head.getId() == 0 && body.getId() == 0) {
				if (ground.getId() == BlockID.WATER || ground.getId() == BlockID.STILL_WATER && !ground.isSolid()) {
					return findRandomPosition(position);
				}
				return position.setComponents(x + 0.5, i + 1, z + 0.5);
			}
		}
		return findRandomPosition(position);
	}
}