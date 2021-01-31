package ru.jl1mbo.AnarchyCore.GameHandler.Bosses;

import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.item.Item;
import ru.dragonestia.ironlib.IronLib;
import ru.dragonestia.ironlib.item.PrefabManager;
import ru.jl1mbo.AnarchyCore.Entity.Bosses.EvokerBoss;
import ru.jl1mbo.AnarchyCore.Entity.Bosses.HuskBoss;
import ru.jl1mbo.AnarchyCore.Entity.Bosses.SpiderBoss;
import ru.jl1mbo.AnarchyCore.Entity.Bosses.WitherSkeletonBoss;
import ru.jl1mbo.AnarchyCore.GameHandler.Bosses.Commands.BossesCommand;
import ru.jl1mbo.AnarchyCore.GameHandler.Bosses.Entity.HelperCaveSpider;
import ru.jl1mbo.AnarchyCore.GameHandler.Bosses.Entity.HelperWitherSkeleton;
import ru.jl1mbo.AnarchyCore.GameHandler.Bosses.Items.BlazeRodArtifact;
import ru.jl1mbo.AnarchyCore.GameHandler.Bosses.Items.CobwebArtifact;
import ru.jl1mbo.AnarchyCore.GameHandler.Bosses.Items.IceFrostedArtifact;
import ru.jl1mbo.AnarchyCore.GameHandler.Bosses.Items.SkullArtifact;

public class BossAPI {

	public static void register() {
		PrefabManager prefabManager = IronLib.getInstance().getPrefabManager();
		prefabManager.registerItem(new CobwebArtifact(Item.get(Item.COBWEB)));
		prefabManager.registerItem(new BlazeRodArtifact(Item.get(Item.BLAZE_ROD)));
		prefabManager.registerItem(new IceFrostedArtifact(Item.get(Item.ICE_FROSTED)));
		prefabManager.registerItem(new SkullArtifact(Item.get(Item.SKULL)));
		Server.getInstance().getCommandMap().register("", new BossesCommand());
		Entity.registerEntity(WitherSkeletonBoss.class.getSimpleName(), WitherSkeletonBoss.class);
		Entity.registerEntity(HuskBoss.class.getSimpleName(), HuskBoss.class);
		Entity.registerEntity(SpiderBoss.class.getSimpleName(), SpiderBoss.class);
		Entity.registerEntity(EvokerBoss.class.getSimpleName(), EvokerBoss.class);
		Entity.registerEntity(HelperWitherSkeleton.class.getSimpleName(), HelperWitherSkeleton.class);
		Entity.registerEntity(HelperCaveSpider.class.getSimpleName(), HelperCaveSpider.class);
	}
}