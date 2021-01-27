package AnarchySystem.Components.EventsListener;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import AnarchySystem.Components.Commands.Home.HomeAPI;
import AnarchySystem.Manager.WorldSystem.WorldSystemAPI;
import AnarchySystem.Utils.Utils;
import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerMoveEvent;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;

public class PlayerMoveListener implements Listener {
	private static Integer[] BORDER = new Integer[] {-1500, 1500, -1500, 1500};

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
				
			}
			findRandomSafePosition(WorldSystemAPI.getMap(), position -> {
				player.teleport(position.setLevel(WorldSystemAPI.getMap()));
				player.sendTip("§r§fВы успешно телепортировались в рандомное место§7!");
			});
		}
	}

	private static void findRandomSafePosition(Level level, Consumer<Position> callback) {
		CompletableFuture.runAsync(() -> callback.accept(randomPos(new Position(0, 0, 0, level))));
	}

	private static Position randomPos(Position base) {
		int x = Utils.rand(BORDER[0], BORDER[1]);
		int z = Utils.rand(BORDER[2], BORDER[3]);
		base.setComponents(x, 65, z);
		int cx = base.getChunkX();
		int cz = base.getChunkZ();
		while (!base.getLevel().isChunkGenerated(cx, cz) || !base.getLevel().isChunkLoaded(cx, cz)) {
			base.getLevel().generateChunk(cx, cz, true);
			base.getLevel().loadChunk(cx, cz, true);
		}
		for (int y = 65; y < 120; y++) {
			base.setComponents(x, y, z);
			Block ground = base.getLevel().getBlock(base);
			Block body = base.getLevel().getBlock(base.setComponents(x, y + 1, z));
			Block head = base.getLevel().getBlock(base.setComponents(x, y + 2, z));
			if (head.getId() == 0 && body.getId() == 0) {
				if (ground.getId() != Block.LAVA || ground.getId() != Block.STILL_LAVA && ground.isSolid()) {
					return base.setComponents(x + 0.5, y + 1, z + 0.5);
				}
			}
		}
		return randomPos(base);
	}
}