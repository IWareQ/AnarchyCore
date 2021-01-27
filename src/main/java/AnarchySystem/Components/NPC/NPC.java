package AnarchySystem.Components.NPC;

import AnarchySystem.Main;
import AnarchySystem.Components.NPC.EventsHandler.EntityDamageListener;
import AnarchySystem.Components.NPC.EventsHandler.InventoryTransactionListener;
import AnarchySystem.Components.NPC.EventsHandler.PlayerMoveListener;
import cn.nukkit.Server;
import cn.nukkit.plugin.PluginManager;

public class NPC {

	private void registerEvents() {
		PluginManager pluginManager = Server.getInstance().getPluginManager();
		pluginManager.registerEvents(new PlayerMoveListener(), Main.getInstance());
		pluginManager.registerEvents(new EntityDamageListener(), Main.getInstance());
		pluginManager.registerEvents(new InventoryTransactionListener(), Main.getInstance());
	}
}