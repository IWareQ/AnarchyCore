package ru.jl1mbo.AnarchyCore.Manager.WorldSystem.EventsListener;

import cn.nukkit.Server;
import cn.nukkit.plugin.PluginManager;
import ru.jl1mbo.AnarchyCore.Main;

public class EventsListenerWorldSystem {

	public static void register() {
		PluginManager pluginManager = Server.getInstance().getPluginManager();
		pluginManager.registerEvents(new BlockBreakListener(), Main.getInstance());
		pluginManager.registerEvents(new BlockBurnListener(), Main.getInstance());
		pluginManager.registerEvents(new BlockIgniteListener(), Main.getInstance());
		pluginManager.registerEvents(new BlockPlaceListener(), Main.getInstance());
		pluginManager.registerEvents(new EntityDamageListener(), Main.getInstance());
		pluginManager.registerEvents(new ItemFrameDropItemListener(), Main.getInstance());
		pluginManager.registerEvents(new LeavesDecayListener(), Main.getInstance());
		pluginManager.registerEvents(new PlayerBucketEmptyListener(), Main.getInstance());
		pluginManager.registerEvents(new PlayerDropItemListener(), Main.getInstance());
		pluginManager.registerEvents(new PlayerFoodLevelChangeListener(), Main.getInstance());
		pluginManager.registerEvents(new ProjectileLaunchListener(), Main.getInstance());
	}
}