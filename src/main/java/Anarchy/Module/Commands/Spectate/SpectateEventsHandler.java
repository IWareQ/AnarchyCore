package Anarchy.Module.Commands.Spectate;

import Anarchy.Module.Commands.Inventory.InventoryHandler;
import Anarchy.Module.Commands.Spectate.Utils.SpectatePlayer;
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
		if (SpectateAPI.SPECTATE_PLAYERS.containsKey(player.getName()) && item.getCustomName().equalsIgnoreCase("\u00a7r\u00a7f\u0417\u0430\u0432\u0435\u0440\u0448\u0438\u0442\u044c \u041d\u0430\u0431\u043b\u044e\u0434\u0435\u043d\u0438\u0435") && player.getGamemode() == 3) {
			SpectateAPI.removeSpectate(player);
		}
		if (SpectateAPI.SPECTATE_PLAYERS.containsKey(player.getName()) && item.getCustomName().equalsIgnoreCase("\u00a7r\u00a7f\u041f\u0440\u043e\u0432\u0435\u0440\u043a\u0430 \u0440\u0435\u0433\u0438\u043e\u043d\u043e\u0432") && player.getGamemode() == 3) {
			int regionID = RegionsAPI.getRegionIDByLocation(player.getLocation());
			if (regionID != -1) {
				player.sendTip(RegionsAPI.BUSY_BY.replace("{PLAYER}", RegionsAPI.getRegionOwner(regionID)));
				player.getLevel().addSound(player, Sound.RANDOM_FIZZ, 1, 1, player);
			} else {
				player.sendTip(RegionsAPI.FREE);
				player.getLevel().addSound(player, Sound.RANDOM_LEVELUP, 1, 1, player);
			}
		}
		if (SpectateAPI.SPECTATE_PLAYERS.containsKey(player.getName()) && item.getCustomName().equalsIgnoreCase("\u00a7r\u00a7f\u041f\u0440\u043e\u0441\u043c\u043e\u0442\u0440\u0435\u0442\u044c \u0418\u043d\u0432\u0435\u043d\u0442\u0430\u0440\u044c") && player.getGamemode() == 3) {
			SpectatePlayer spectatePlayer = SpectateAPI.SPECTATE_PLAYERS.get(player.getName());
			InventoryHandler.checkInventory(spectatePlayer.getSpectateName(), player);
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