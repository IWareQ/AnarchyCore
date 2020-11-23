package Anarchy.Functions;

import Anarchy.Utils.RandomUtils;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;

public class FunctionsAPI {
	public static Integer[] BORDER = new Integer[] {-1500, 1500, -1500, 1500};
	public static Integer[] RANDOM_TP = new Integer[] {-1000, 1000, -1000, 1000};
	public static Level MAP;
	public static Level SPAWN;
	public static Level COORDINATE;
	public static Level NETHER;

	public static void register() {
		Server.getInstance().loadLevel("world");
		Server.getInstance().loadLevel("world2");
		Server.getInstance().loadLevel("world3");
		Server.getInstance().loadLevel("nether");
		MAP = Server.getInstance().getLevelByName("world");
		SPAWN = Server.getInstance().getLevelByName("world2");
		COORDINATE = Server.getInstance().getLevelByName("world3");
		NETHER = Server.getInstance().getLevelByName("nether");
		MAP.getGameRules().setGameRule(GameRule.SHOW_COORDINATES, true);
		MAP.getGameRules().setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
		SPAWN.getGameRules().setGameRule(GameRule.SHOW_COORDINATES, false);
		NETHER.getGameRules().setGameRule(GameRule.SHOW_COORDINATES, true);
		NETHER.getGameRules().setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
	}

	public static Position randomPos(Position pos) {
		int x = RandomUtils.rand(RANDOM_TP[0], RANDOM_TP[1]);
		int z = RandomUtils.rand(RANDOM_TP[2], RANDOM_TP[3]);
		pos.setComponents(x, 70, z);
		for (int i = 70; i < 120; i++) {
			pos.setComponents(x, i, z);
			Block ground = pos.getLevel().getBlock(pos);
			Block body = pos.getLevel().getBlock(pos.setComponents(x, i + 1, z));
			Block head = pos.getLevel().getBlock(pos.setComponents(x, i + 2, z));
			if (head.getId() == 0 && body.getId() == 0) {
				if (ground.getId() != Block.LAVA || ground.getId() != Block.STILL_LAVA && ground.isSolid()) {
					return pos.setComponents(x + 0.5, i + 1, z + 0.5);
				}
			}
		}
		return randomPos(pos);
	}
}