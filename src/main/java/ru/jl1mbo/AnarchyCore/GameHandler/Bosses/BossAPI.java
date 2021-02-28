package ru.jl1mbo.AnarchyCore.GameHandler.Bosses;

import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import ru.jl1mbo.AnarchyCore.Entity.Bosses.EvokerBoss;
import ru.jl1mbo.AnarchyCore.Entity.Bosses.HuskBoss;
import ru.jl1mbo.AnarchyCore.Entity.Bosses.SpiderBoss;
import ru.jl1mbo.AnarchyCore.Entity.Bosses.WitherSkeletonBoss;
import ru.jl1mbo.AnarchyCore.GameHandler.Bosses.Commands.BossesCommand;
import ru.jl1mbo.AnarchyCore.GameHandler.Bosses.Entity.HelperCaveSpider;
import ru.jl1mbo.AnarchyCore.GameHandler.Bosses.Entity.HelperWitherSkeleton;
import ru.jl1mbo.AnarchyCore.GameHandler.Bosses.Task.BossesSpawnTask;

public class BossAPI {

	public static void register() {
		Server.getInstance().getCommandMap().register("", new BossesCommand());
		Server.getInstance().getScheduler().scheduleRepeatingTask(new BossesSpawnTask(), 60 * 20);
		Entity.registerEntity(WitherSkeletonBoss.class.getSimpleName(), WitherSkeletonBoss.class);
		Entity.registerEntity(HuskBoss.class.getSimpleName(), HuskBoss.class);
		Entity.registerEntity(SpiderBoss.class.getSimpleName(), SpiderBoss.class);
		Entity.registerEntity(EvokerBoss.class.getSimpleName(), EvokerBoss.class);
		Entity.registerEntity(HelperWitherSkeleton.class.getSimpleName(), HelperWitherSkeleton.class);
		Entity.registerEntity(HelperCaveSpider.class.getSimpleName(), HelperCaveSpider.class);
	}
}