package ru.jl1mbo.AnarchyCore.Modules.Commands.NPC;

import cn.nukkit.entity.Entity;
import ru.jl1mbo.AnarchyCore.Entity.NPC.VillagerNPC;
import ru.jl1mbo.AnarchyCore.Entity.NPC.WanderingTraderNPC;

public class NPC {

	public static void register() {
		Entity.registerEntity(VillagerNPC.class.getSimpleName(), VillagerNPC.class);
		Entity.registerEntity(WanderingTraderNPC.class.getSimpleName(), WanderingTraderNPC.class);
	}
}