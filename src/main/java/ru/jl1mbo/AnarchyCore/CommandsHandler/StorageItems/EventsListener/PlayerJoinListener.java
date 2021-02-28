package ru.jl1mbo.AnarchyCore.CommandsHandler.StorageItems.EventsListener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.utils.Config;
import ru.jl1mbo.AnarchyCore.Main;

public class PlayerJoinListener implements Listener {

	@EventHandler()
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		Config config = new Config(Main.getInstance().getDataFolder() + "/StorageItems/" + player.getName().toLowerCase() + ".yml", Config.YAML);
		if (config.getAll().size() != 0) {
			player.sendTitle("§l§6Активная покупка", "§7/§fstorage", 0, 60, 0);
		}
	}
}