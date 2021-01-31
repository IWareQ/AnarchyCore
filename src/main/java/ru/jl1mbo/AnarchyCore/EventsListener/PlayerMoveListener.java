package ru.jl1mbo.AnarchyCore.EventsListener;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerMoveEvent;
import cn.nukkit.level.Position;
import ru.jl1mbo.AnarchyCore.CommandsHandler.Home.HomeAPI;
import ru.jl1mbo.AnarchyCore.Manager.WorldSystem.WorldSystemAPI;

public class PlayerMoveListener implements Listener {
	private static Integer[] BORDER = new Integer[] {-2000, 2000, -2000, 2000};

	@EventHandler()
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		Block block = player.getLevel().getBlock(new Position(player.getFloorX() + 0.5, player.getFloorY() - 1, player.getFloorZ() + 0.5));
		if (!player.hasPermission("Development")) {
			if (player.getFloorX() < BORDER[0] || player.getFloorX() > BORDER[1] || player.getFloorZ() < BORDER[2] || player.getFloorZ() > BORDER[3]) {
				player.sendTip("§fВы пытаетесь §6выйти §fза границу мира§7!");
				event.setCancelled(true);
			}
		}
		if (player.getFloorY() <= -15 && player.getLevel() != WorldSystemAPI.getTheEnd()) {
			player.teleport(WorldSystemAPI.getSpawn().getSafeSpawn());
			player.sendMessage("§l§6• §r§fВы упали за границу мира§7! §fЧтобы Вы не потеряли свои вещи§7, §fмы решили телепортировать Вас на спавн§7!\n§l§6• §r§fЗапомните§7, §fесли вы упадете в бездну в мире §6TheEnd§7, §fто Вас не спасут§7!");
		}
		if (player.getLevel().equals(WorldSystemAPI.getSpawn()) && block.getId() == Block.END_PORTAL) {
			if (HomeAPI.isHome(player.getName())) {
				player.teleport(HomeAPI.getHomePosition(player.getName()));
			} else {
				WorldSystemAPI.findRandomSafePosition(WorldSystemAPI.getMap(), position -> {
					player.teleport(position.setLevel(WorldSystemAPI.getMap()));
					player.sendTip("§r§fВы успешно телепортировались в рандомное место§7!");
				});
			}
		}
	}
}