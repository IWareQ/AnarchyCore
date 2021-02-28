package ru.jl1mbo.AnarchyCore.SystemProcessorHandler.BanSystem.EventsListener;

import cn.nukkit.Server;
import cn.nukkit.plugin.PluginManager;
import ru.jl1mbo.AnarchyCore.Main;

public class EventsListenerBanSystem {
	public static void register() {
		PluginManager pluginManager = Server.getInstance().getPluginManager();
		pluginManager.registerEvents(new BlockBreakListener(), Main.getInstance());
		pluginManager.registerEvents(new BlockIgniteListener(), Main.getInstance());
		pluginManager.registerEvents(new BlockPlaceListener(), Main.getInstance());
		pluginManager.registerEvents(new EntityDamageListener(), Main.getInstance());
		pluginManager.registerEvents(new PlayerBucketEmptyListener(), Main.getInstance());
		pluginManager.registerEvents(new PlayerChatListener(), Main.getInstance());
		pluginManager.registerEvents(new PlayerCommandPreprocessListener(), Main.getInstance());
		pluginManager.registerEvents(new PlayerDropItemListener(), Main.getInstance());
		pluginManager.registerEvents(new PlayerFoodLevelChangeListener(), Main.getInstance());
		pluginManager.registerEvents(new PlayerInteractListener(), Main.getInstance());
		pluginManager.registerEvents(new ProjectileLaunchListener(), Main.getInstance());
		pluginManager.registerEvents(new PlayerJoinListener(), Main.getInstance());
		pluginManager.registerEvents(new EntityDamageByEntityListener(), Main.getInstance());
	}
}