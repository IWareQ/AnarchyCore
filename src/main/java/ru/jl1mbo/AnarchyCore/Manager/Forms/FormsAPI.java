package ru.jl1mbo.AnarchyCore.Manager.Forms;

import cn.nukkit.Server;
import cn.nukkit.plugin.PluginManager;
import ru.jl1mbo.AnarchyCore.Main;
import ru.jl1mbo.AnarchyCore.Manager.Forms.EventsListener.PlayerFormRespondedListener;
import ru.jl1mbo.AnarchyCore.Manager.Forms.EventsListener.PlayerQuitListener;

public class FormsAPI {
	
	public static void register() {
		PluginManager pluginManager = Server.getInstance().getPluginManager();
		pluginManager.registerEvents(new PlayerFormRespondedListener(), Main.getInstance());
		pluginManager.registerEvents(new PlayerQuitListener(), Main.getInstance());
	}
}