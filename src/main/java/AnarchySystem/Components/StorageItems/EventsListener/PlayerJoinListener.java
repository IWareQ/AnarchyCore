package AnarchySystem.Components.StorageItems.EventsListener;

import AnarchySystem.Main;
import AnarchySystem.Utils.ConfigUtils;
import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.utils.Config;

public class PlayerJoinListener implements Listener {

	@EventHandler()
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		Config config = ConfigUtils.getStorageItemsConfig(player.getName());
		if (config.getAll().size() != 0) {
			player.sendTitle("§l§6Активная покупка", "§7/§fstorage", 0, 60, 0);
		}
	}
}