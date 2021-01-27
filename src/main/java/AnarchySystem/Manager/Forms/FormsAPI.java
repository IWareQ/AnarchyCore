package AnarchySystem.Manager.Forms;

import AnarchySystem.Main;
import AnarchySystem.Manager.Forms.EventsListener.PlayerFormRespondedListener;
import AnarchySystem.Manager.Forms.EventsListener.PlayerQuitListener;
import cn.nukkit.Server;
import cn.nukkit.plugin.PluginManager;

public class FormsAPI {
	
	public static void registerEvents() {
		PluginManager pluginManager = Server.getInstance().getPluginManager();
		pluginManager.registerEvents(new PlayerFormRespondedListener(), Main.getInstance());
		pluginManager.registerEvents(new PlayerQuitListener(), Main.getInstance());
	}
}