package ru.jl1mbo.AnarchyCore.CommandsHandler.NPC;

import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.item.Item;
import cn.nukkit.plugin.PluginManager;
import ru.dragonestia.ironlib.IronLib;
import ru.dragonestia.ironlib.item.PrefabManager;
import ru.jl1mbo.AnarchyCore.Main;
import ru.jl1mbo.AnarchyCore.CommandsHandler.NPC.Command.NPCCommand;
import ru.jl1mbo.AnarchyCore.CommandsHandler.NPC.EventsListener.EntityDamageListener;
import ru.jl1mbo.AnarchyCore.CommandsHandler.NPC.EventsListener.InventoryTransactionListener;
import ru.jl1mbo.AnarchyCore.CommandsHandler.NPC.EventsListener.PlayerMoveListener;
import ru.jl1mbo.AnarchyCore.CommandsHandler.NPC.Item.GoldenMoneyItem;
import ru.jl1mbo.AnarchyCore.Entity.NPC.PiglinNPC;
import ru.jl1mbo.AnarchyCore.Entity.NPC.VillagerNPC;
import ru.jl1mbo.AnarchyCore.Entity.NPC.WanderingTraderNPC;

public class NPC {

	public static void register() {
		PrefabManager prefabManager = IronLib.getInstance().getPrefabManager();
		prefabManager.registerItem(new GoldenMoneyItem(Item.get(Item.GOLD_NUGGET)));
		PluginManager pluginManager = Server.getInstance().getPluginManager();
		pluginManager.registerEvents(new PlayerMoveListener(), Main.getInstance());
		pluginManager.registerEvents(new EntityDamageListener(), Main.getInstance());
		pluginManager.registerEvents(new InventoryTransactionListener(), Main.getInstance());
		Entity.registerEntity(PiglinNPC.class.getSimpleName(), PiglinNPC.class);
		Entity.registerEntity(VillagerNPC.class.getSimpleName(), VillagerNPC.class);
		Entity.registerEntity(WanderingTraderNPC.class.getSimpleName(), WanderingTraderNPC.class);
		Server.getInstance().getCommandMap().register("", new NPCCommand());
	}
}