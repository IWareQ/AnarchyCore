package ru.jl1mbo.AnarchyCore.Modules.Commands.NPC;

import cn.nukkit.entity.Entity;
import cn.nukkit.level.Position;
import cn.nukkit.math.SimpleAxisAlignedBB;
import ru.jl1mbo.AnarchyCore.Entity.NPC.PiglinNPC;
import ru.jl1mbo.AnarchyCore.Entity.NPC.VillagerNPC;
import ru.jl1mbo.AnarchyCore.Entity.NPC.WanderingTraderNPC;

public class NPC {

	public static void register() {
		Entity.registerEntity(PiglinNPC.class.getSimpleName(), PiglinNPC.class);
		Entity.registerEntity(VillagerNPC.class.getSimpleName(), VillagerNPC.class);
		Entity.registerEntity(WanderingTraderNPC.class.getSimpleName(), WanderingTraderNPC.class);
	}

	public static void spawnNPC(Position position, Position position2) {
		Entity[] entities = position.getLevel().getNearbyEntities(new SimpleAxisAlignedBB(position.getFloorX(), position.getFloorY(), position.getFloorZ(), position.getFloorX() + 1, position.getFloorY() + 2,
							position.getFloorZ() + 1));
		Entity[] entities2 = position.getLevel().getNearbyEntities(new SimpleAxisAlignedBB(position.getFloorX(), position.getFloorY(), position.getFloorZ(), position.getFloorX() + 1, position.getFloorY() + 2,
							 position.getFloorZ() + 1));
		Entity entity = Entity.createEntity("VillagerNPC", position);
		Entity entity2 = Entity.createEntity("WanderingTraderNPC", position2);
		if (entities.length == 0 && entity != null) {
			entity.spawnToAll();
		}
		if (entities2.length == 0 && entity2 != null) {
			entity2.spawnToAll();
		}
	}
}