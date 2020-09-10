package Anarchy.Task.Utils;

import Anarchy.Utils.RandomUtils;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.level.Position;

public class Boss {
	public static void createBoss() {
		int x = RandomUtils.rand(-100, 100);
		int z = RandomUtils.rand(-100, 100);
		Entity entity = Entity.createEntity("SilverfishBoss", new Position(100, 70, 100));
		entity.setScale((float)5.0);
		entity.spawnToAll();
		Server.getInstance().broadcastMessage("Босс появился на " + entity.getPosition());
	}
}