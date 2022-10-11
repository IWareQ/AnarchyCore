package ru.iwareq.anarchycore.manager.WorldSystem.TheEnd;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockEndPortal;
import cn.nukkit.block.BlockID;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityPortalEnterEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerInteractEvent.Action;
import cn.nukkit.event.player.PlayerMoveEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemID;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.math.BlockFace;
import cn.nukkit.network.protocol.LevelSoundEventPacket;
import cn.nukkit.scheduler.Task;
import ru.iwareq.anarchycore.manager.WorldSystem.WorldSystemAPI;

public class TheEndEventsListener implements Listener {

	private static boolean isCompletedPortal(Block center) {
		for (int i = 0; i < 4; i++) {
			for (int j = -1; j <= 1; j++) {
				Block block = center.getSide(BlockFace.fromHorizontalIndex(i), 2).getSide(BlockFace.fromHorizontalIndex((i + 1) % 4), j);
				if (block.getId() != Block.END_PORTAL_FRAME || (block.getDamage() & 0x4) == 0) {
					return false;
				}
			}
		}
		return true;
	}

	private static Position moveToTheEnd(Position current) {
		Server server = Server.getInstance();
		if (WorldSystemAPI.TheEnd != null) {
			if (current.getLevel() == WorldSystemAPI.TheEnd) {
				return server.getDefaultLevel().getSpawnLocation();
			} else {
				return new Position(100.5, 49, 0.5, WorldSystemAPI.TheEnd);
			}
		}

		return null;
	}

	/*@EventHandler()
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		Block block = player.getLevel().getBlock(new Position(player.getX(), player.getY() - 0.5, player.getZ()));
		if (player.getLevel().equals(WorldSystemAPI.Map) && block.getId() == Block.END_PORTAL) {
			Server.getInstance().getScheduler().scheduleDelayedTask(new Task() {

				@Override
				public void onRun(int currentTick) {
					WorldSystemAPI.generateTheEndPlatform();
				}
			}, 20);
			player.teleport(WorldSystemAPI.TheEnd.getSafeSpawn().add(0, 2, 0));
		}
	}*/

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Block block = event.getBlock();
			if (block.getId() == BlockID.END_PORTAL_FRAME && (block.getDamage() & 0x4) == 0) {
				Player player = event.getPlayer();
				Item item = player.getInventory().getItemInHand();
				if (item.getId() == ItemID.ENDER_EYE) {
					event.setCancelled(true);
					if (!player.isSneaking()) {
						block.onActivate(item, player);
					}
					for (int i = 0; i < 4; i++) {
						for (int j = -1; j <= 1; j++) {
							Block test = block.getSide(BlockFace.fromHorizontalIndex(i), 2).getSide(BlockFace.fromHorizontalIndex((i + 1) % 4), j);
							if (isCompletedPortal(test)) {
								for (int x = -1; x <= 1; x++) {
									for (int z = -1; z <= 1; z++) {
										block.getLevel().setBlock(test.add(x, 0, z), Block.get(Block.END_PORTAL), true);
									}
								}

								block.getLevel().addLevelSoundEvent(block, LevelSoundEventPacket.SOUND_BLOCK_END_PORTAL_SPAWN);
								return;
							}
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if ((player.getServer().getTick() & 0b1111) != 0) {
			return;
		}
		if (player.getLevel().getBlock(player.getFloorX(), player.getFloorY(), player.getFloorZ()) instanceof BlockEndPortal && player.getY() - player.getFloorY() < 0.75) {
			Position newPos = moveToTheEnd(player);
			if (newPos != null) {
				if (newPos.getLevel().getDimension() == Level.DIMENSION_THE_END) {
					player.teleport(newPos);
					Server.getInstance().getScheduler().scheduleDelayedTask(new Task() {

						@Override
						public void onRun(int currentTick) {
							WorldSystemAPI.generateTheEndPlatform();
							player.teleport(newPos);
						}
					}, 20);
				} else {
					WorldSystemAPI.findRandomPositionAndTp(WorldSystemAPI.Map, player::teleport);
				}
			}
		}
	}

	@EventHandler
	public void onEntityPortalEnter(EntityPortalEnterEvent event) {
		if (event.getPortalType() == EntityPortalEnterEvent.PortalType.NETHER) {
			Entity entity = event.getEntity();
			if (entity.getLevel().equals(WorldSystemAPI.TheEnd) || entity.getLevel().equals(WorldSystemAPI.Spawn)) {
				event.setCancelled(true);
			}
		}
	}
}