package Anarchy.Module.Commands.Spectate;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerItemHeldEvent;

public class SpectateEventsHandler implements Listener {
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerItemHeld(PlayerItemHeldEvent event) {
		Player player = event.getPlayer();
		if (SpectateAPI.SPECTATE_PLAYERS.containsKey(player.getName())) {
			if (event.getItem().getCustomName().equals("§r§l§fЗавершить Наблюдение") && player.getGamemode() == 3) {
				SpectateAPI.removeSpectate(player);
			}
		}
	}
}