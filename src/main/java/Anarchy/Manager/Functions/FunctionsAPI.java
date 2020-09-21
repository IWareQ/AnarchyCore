package Anarchy.Manager.Functions;

import cn.nukkit.Server;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.Level;

public class FunctionsAPI {
	public static Integer[] BORDER = new Integer[]{-1000, 1000, -1000, 1000};
	public static Integer[] RANDOM_TP = new Integer[]{-300, 300, -300, 300};
	public static Level MAP;
	public static Level NETHER;
	public static Level SPAWN;
	public static Level WORLD3;
	
	public static void register() {
		Server.getInstance().loadLevel("nether");
		Server.getInstance().loadLevel("map");
		Server.getInstance().loadLevel("spawn");
		Server.getInstance().loadLevel("world3");
		NETHER = Server.getInstance().getLevelByName("nether");
		MAP = Server.getInstance().getLevelByName("map");
		SPAWN = Server.getInstance().getLevelByName("spawn");
		WORLD3 = Server.getInstance().getLevelByName("world3");
		MAP.getGameRules().setGameRule(GameRule.SHOW_COORDINATES, true);
		NETHER.getGameRules().setGameRule(GameRule.SHOW_COORDINATES, true);
		SPAWN.getGameRules().setGameRule(GameRule.SHOW_COORDINATES, false);
		MAP.getGameRules().setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
		NETHER.getGameRules().setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
	}
}