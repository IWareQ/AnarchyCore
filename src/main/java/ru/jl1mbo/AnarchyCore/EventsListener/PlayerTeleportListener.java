package ru.jl1mbo.AnarchyCore.EventsListener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerTeleportEvent;
import cn.nukkit.level.Location;

public class PlayerTeleportListener implements Listener {
	private static Integer[] BORDER = new Integer[] {-2000, 2000, -2000, 2000};

	@EventHandler()
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		Player player = event.getPlayer();
		Location location = event.getTo();
		if (!player.hasPermission("Development")) {
			if (location.getFloorX() < BORDER[0] || location.getFloorX() > BORDER[1] || location.getFloorZ() < BORDER[2] || location.getFloorZ() > BORDER[3]) {
				player.sendTip("§r§fВы пытаетесь §6телепортироваться §fза границу мира§7!");
				event.setCancelled(true);
			}
		}
	}
}