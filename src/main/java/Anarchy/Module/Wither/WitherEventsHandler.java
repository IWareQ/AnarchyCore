package Anarchy.Module.Wither;

import Anarchy.AnarchyMain;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockID;
import cn.nukkit.block.BlockSoulSand;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.math.BlockFace;

public class WitherEventsHandler implements Listener {
	
	public static void register() {
		Server.getInstance().getPluginManager().registerEvents(new WitherEventsHandler(), AnarchyMain.plugin);
		Entity.registerEntity(EntityWither.class.getSimpleName(), EntityWither.class);
		Entity.registerEntity("BlueWitherSkull", BlueWitherSkull.class);
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		Item item = event.getItem();
		if (item.getId() == Item.SKULL && item.getDamage() == 1) {
			Block block = event.getBlock();
			Block block1 = block.getSide(BlockFace.DOWN, 1);
			Block block2 = block.getSide(BlockFace.DOWN, 2);
			if (block1.getId() == BlockID.SOUL_SAND && block2.getId() == BlockID.SOUL_SAND) {
				Block westBlock = block1.getSide(BlockFace.WEST, 1);
				Block eastBlock = block1.getSide(BlockFace.EAST, 1);
				Block northBlock = block1.getSide(BlockFace.NORTH, 1);
				Block southBlock = block1.getSide(BlockFace.SOUTH, 1);
				if (westBlock.getId() == BlockID.SOUL_SAND && eastBlock.getId() == BlockID.SOUL_SAND) {
					Block westHead = block.getSide(BlockFace.WEST, 1);
					Block eastHead = block.getSide(BlockFace.EAST, 1);
					if (westHead.getId() == BlockID.SKULL_BLOCK && eastHead.getId() == BlockID.SKULL_BLOCK) {
						Level level = player.level;
						Block air = Block.get(0);
						level.setBlock(block, air);
						level.setBlock(block1, air);
						level.setBlock(block2, air);
						level.setBlock(westBlock, air);
						level.setBlock(eastBlock, air);
						level.setBlock(westHead, air);
						level.setBlock(eastHead, air);
						new EntityWither(block2.getChunk(), Entity.getDefaultNBT(player)).spawnToAll();
						return;
					}
				} else if (northBlock.getId() == BlockID.SOUL_SAND && southBlock.getId() == BlockID.SOUL_SAND) {
					Block northHead = block.getSide(BlockFace.NORTH, 1);
					Block southHead = block.getSide(BlockFace.SOUTH, 1);
					if (northHead.getId() == BlockID.SKULL_BLOCK && southHead.getId() == BlockID.SKULL_BLOCK) {
						Level level = player.level;
						Block air = Block.get(0);
						level.setBlock(block, air);
						level.setBlock(block1, air);
						level.setBlock(block2, air);
						level.setBlock(northBlock, air);
						level.setBlock(southBlock, air);
						level.setBlock(northHead, air);
						level.setBlock(southHead, air);
						new EntityWither(block2.getChunk(), Entity.getDefaultNBT(player)).spawnToAll();
						return;
					}
				}
			}
			if (block.getSide(BlockFace.DOWN, 1) instanceof BlockSoulSand) {
				player.sendTip("§l§e| §fЕсли Вы строите §6Визера§7, §fто последняя голова должна быть поставлена по середине§7! §l§e|");
			}
		}
	}
}