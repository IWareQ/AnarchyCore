package ru.jl1mbo.AnarchyCore.GameHandler.BlockProtection.EventsListener;

import cn.nukkit.Server;
import cn.nukkit.plugin.PluginManager;
import ru.jl1mbo.AnarchyCore.Main;

public class EventsListenerBlockProtection {
	public static void register() {
		PluginManager pluginManager = Server.getInstance().getPluginManager();
		pluginManager.registerEvents(new BlockBreakListener(), Main.getInstance());
		pluginManager.registerEvents(new BlockPistonListener(), Main.getInstance());
		pluginManager.registerEvents(new BlockPlaceListener(), Main.getInstance());
		pluginManager.registerEvents(new EntityExplodeListener(), Main.getInstance());
		pluginManager.registerEvents(new ItemFrameDropItemListener(), Main.getInstance());
		pluginManager.registerEvents(new PlayerInteractListener(), Main.getInstance());
	}
}