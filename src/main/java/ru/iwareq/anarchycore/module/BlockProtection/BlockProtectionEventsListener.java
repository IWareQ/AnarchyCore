package ru.iwareq.anarchycore.module.BlockProtection;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.block.ItemFrameDropItemEvent;
import cn.nukkit.event.entity.EntityExplodeEvent;
import cn.nukkit.event.player.PlayerInteractEvent;

import java.util.List;

public class BlockProtectionEventsListener implements Listener {

	@EventHandler()
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();
		if (!BlockProtectionAPI.canInteractHere(player, block.getLocation())) {
			player.sendTip("Территория §6не доступна §fдля взаимодействия");
			event.setCancelled(true);
		}
		int regionId = BlockProtectionAPI.getRegionIDByPosition(block.getLocation());
		if (regionId != -1) {
			if (block.getLocation().equals(BlockProtectionAPI.getRegionBlockLocation(regionId))) {
				if (BlockProtectionAPI.isRegionOwner(player.getName(), regionId) || player.isOp()) {
					player.sendMessage(BlockProtectionAPI.PREFIX + "Регион §7#§6" + regionId + " §fуспешно удален§7!");
					BlockProtectionAPI.deleteRegion(regionId);
				} else {
					player.sendMessage(BlockProtectionAPI.PREFIX + "Вы не можете удалить §6чужой §fрегион§7!");
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler()
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();
		if (!BlockProtectionAPI.canInteractHere(player, block.getLocation())) {
			player.sendTip("Территория §6не доступна §fдля взаимодействия");
			event.setCancelled(true);
		}
		if (BlockProtectionAPI.getAllBlocks().containsKey(block.getId())) {
			BlockProtectionAPI.placeRegion(player, block);
		}
	}

	@EventHandler()
	public void onEntityExplode(EntityExplodeEvent event) {
		List<Block> blocks = event.getBlockList();
		blocks.removeIf(block -> {
			int regionId = BlockProtectionAPI.getRegionIDByPosition(block.getLocation());
			if (regionId != -1) {
				return block.getLocation().equals(BlockProtectionAPI.getRegionBlockLocation(regionId));
			}
			return false;
		});
		event.setBlockList(blocks);
	}

	@EventHandler()
	public void onItemFrameDropItem(ItemFrameDropItemEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();
		if (!BlockProtectionAPI.canInteractHere(player, block.getLocation())) {
			player.sendTip("Территория §6не доступна §fдля взаимодействия");
			event.setCancelled(true);
		}
	}

	@EventHandler()
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();
		if (!BlockProtectionAPI.canInteractHere(player, block.getLocation())) {
			player.sendTip("Территория §6не доступна §fдля взаимодействия");
			event.setCancelled(true);
		}
	}
}