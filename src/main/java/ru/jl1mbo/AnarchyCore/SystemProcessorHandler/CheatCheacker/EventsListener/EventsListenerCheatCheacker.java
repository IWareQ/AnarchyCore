package ru.jl1mbo.AnarchyCore.SystemProcessorHandler.CheatCheacker.EventsListener;

import cn.nukkit.Server;
import cn.nukkit.plugin.PluginManager;
import ru.jl1mbo.AnarchyCore.Main;

public class EventsListenerCheatCheacker {

	public static void register() {
		PluginManager pluginManager = Server.getInstance().getPluginManager();
		pluginManager.registerEvents(new EntityDamageListener(), Main.getInstance());
		pluginManager.registerEvents(new PlayerDropItemListener(), Main.getInstance());
		pluginManager.registerEvents(new PlayerQuitListener(), Main.getInstance());
		pluginManager.registerEvents(new BlockBreakListener(), Main.getInstance());
		pluginManager.registerEvents(new BlockPlaceListener(), Main.getInstance());
		pluginManager.registerEvents(new PlayerCommandPreprocessListener(), Main.getInstance());
		pluginManager.registerEvents(new EntityDamageByEntityListener(), Main.getInstance());
	}
}