package Anarchy.Manager.Functions;

import cn.nukkit.Server;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.Level;

public class FunctionsAPI {
	public static Integer[] BORDER = new Integer[] {-1023, 1023, -1023, 1023};
	public static Integer[] RANDOM_TP = new Integer[] {-350, 350, -350, 350};
	public static Level WORLD;
	public static Level NETHER;
	public static Level WORLD2;

	public static void register() {
		Server.getInstance().loadLevel("world");
		Server.getInstance().loadLevel("nether");
		Server.getInstance().loadLevel("world2");
		WORLD = Server.getInstance().getLevelByName("world");
		NETHER = Server.getInstance().getLevelByName("nether");
		WORLD2 = Server.getInstance().getLevelByName("world2");
		WORLD.getGameRules().setGameRule(GameRule.SHOW_COORDINATES, true);
		NETHER.getGameRules().setGameRule(GameRule.SHOW_COORDINATES, true);
		WORLD2.getGameRules().setGameRule(GameRule.SHOW_COORDINATES, false);
	}
}