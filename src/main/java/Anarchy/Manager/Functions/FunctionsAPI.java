package Anarchy.Manager.Functions;

import cn.nukkit.Server;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.Level;

public class FunctionsAPI {
	public static Integer[] BORDER = new Integer[]{-1000, 1000, -1000, 1000};
	public static Integer[] RANDOM_TP = new Integer[]{-300, 300, -300, 300};
	public static Level WORLD;
	public static Level NETHER;
	public static Level WORLD2;
	
	public static void register() {
		Server.getInstance().loadLevel("nether");
		Server.getInstance().loadLevel("world");
		Server.getInstance().loadLevel("world2");
		Server.getInstance().loadLevel("world3");
		NETHER = Server.getInstance().getLevelByName("nether");
		WORLD = Server.getInstance().getLevelByName("world");
		WORLD2 = Server.getInstance().getLevelByName("world2");
		WORLD.getGameRules().setGameRule(GameRule.SHOW_COORDINATES, true);
		NETHER.getGameRules().setGameRule(GameRule.SHOW_COORDINATES, true);
		WORLD2.getGameRules().setGameRule(GameRule.SHOW_COORDINATES, false);
		WORLD.getGameRules().setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
		NETHER.getGameRules().setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
	}
}