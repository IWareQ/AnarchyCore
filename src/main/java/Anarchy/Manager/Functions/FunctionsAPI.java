package Anarchy.Manager.Functions;

import cn.nukkit.Server;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;

public class FunctionsAPI {
	public static Integer[] BORDER = new Integer[]{-2000, 2000, -2000, 2000};
	public static Integer[] RANDOM_TP = new Integer[]{-1000, 1000, -1000, 1000};
	public static Level WORLD;
	public static Level NETHER;
	public static Level WORLD2;
	
	public static void register() {
		Server.getInstance().loadLevel("nether");
		Server.getInstance().loadLevel("world");
		Server.getInstance().loadLevel("world2");
		NETHER = Server.getInstance().getLevelByName("nether");
		WORLD = Server.getInstance().getLevelByName("world");
		WORLD2 = Server.getInstance().getLevelByName("world2");
		WORLD2.setSpawnLocation(new Location(-7, 146, 93, WORLD2));
		WORLD.getGameRules().setGameRule(GameRule.SHOW_COORDINATES, true);
		NETHER.getGameRules().setGameRule(GameRule.SHOW_COORDINATES, true);
		WORLD2.getGameRules().setGameRule(GameRule.SHOW_COORDINATES, false);
	}
}