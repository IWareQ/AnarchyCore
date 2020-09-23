package Anarchy.Manager.Functions;

import cn.nukkit.Server;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.Level;

public class FunctionsAPI {
	public static Integer[] BORDER = new Integer[]{-1000, 1000, -1000, 1000};
	public static Integer[] RANDOM_TP = new Integer[]{-700, 700, -700, 700};
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
}