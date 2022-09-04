package ru.iwareq.anarchycore.module.Commands.NPC;

import cn.nukkit.entity.Entity;
import ru.iwareq.anarchycore.entity.NPC.VillagerNPC;
import ru.iwareq.anarchycore.entity.NPC.WanderingTraderNPC;

public class NPC {

	public static void register() {
		Entity.registerEntity(VillagerNPC.class.getSimpleName(), VillagerNPC.class);
		Entity.registerEntity(WanderingTraderNPC.class.getSimpleName(), WanderingTraderNPC.class);
	}
}