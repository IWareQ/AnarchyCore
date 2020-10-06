package Anarchy.Module.Commands.Spectate;

import Anarchy.Module.Regions.RegionsAPI;
import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerItemHeldEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Sound;

public class SpectateEventsHandler implements Listener {

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerItemHeld(PlayerItemHeldEvent event) {
		Player player = event.getPlayer();
		Item item = event.getItem();
		if (SpectateAPI.SPECTATE_PLAYERS.containsKey(player.getName()) && item.getCustomName().equalsIgnoreCase("§r§l§fЗавершить Наблюдение") && player.getGamemode() == 3) {
			SpectateAPI.removeSpectate(player);
		}
		if (SpectateAPI.SPECTATE_PLAYERS.containsKey(player.getName()) && item.getCustomName().equalsIgnoreCase("§r§l§fПроверка регионов") && player.getGamemode() == 3) {
			int regionID = RegionsAPI.getRegionIDByLocation(player.getLocation());
			if (regionID != -1) {
				player.sendTip(RegionsAPI.BUSY_BY.replace("{PLAYER}", RegionsAPI.getRegionOwner(regionID)));
				player.getLevel().addSound(player, Sound.RANDOM_FIZZ, 1, 1, player);
			} else {
				player.sendTip(RegionsAPI.FREE);
				player.getLevel().addSound(player, Sound.RANDOM_LEVELUP, 1, 1, player);
			}
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (SpectateAPI.SPECTATE_PLAYERS.containsKey(player.getName())) {
			SpectateAPI.removeSpectate(player);
		}
	}
}