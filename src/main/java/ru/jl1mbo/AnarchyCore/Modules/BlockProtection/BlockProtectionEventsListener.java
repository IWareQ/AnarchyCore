package ru.jl1mbo.AnarchyCore.Modules.BlockProtection;

import java.util.List;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockPistonEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.block.ItemFrameDropItemEvent;
import cn.nukkit.event.entity.EntityExplodeEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import ru.jl1mbo.AnarchyCore.Utils.SQLiteUtils;

public class BlockProtectionEventsListener implements Listener {

	@EventHandler()
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();
		if (BlockProtectionAPI.canInteractHere(player, block.getLocation())) {
			player.sendTip("Территория §6не доступна §fдля взаимодействия");
			event.setCancelled(true);
		}
		int regionId = BlockProtectionAPI.getRegionIDByLocation(block.getLocation());
		if (regionId != -1) {
			if (block.getLocation().equals(BlockProtectionAPI.getRegionBlockLocation(regionId))) {
				if (BlockProtectionAPI.isRegionOwner(player.getName(), regionId)) {
					player.sendMessage(BlockProtectionAPI.PREFIX + "Регион §7#§6" + regionId + " §fуспешно удален§7!");
					SQLiteUtils.query("BlockProtection.db", "DELETE FROM `Areas` WHERE (`ID`) = '" + regionId + "'");
					SQLiteUtils.query("BlockProtection.db", "DELETE FROM `Members` WHERE (`ID`) = '" + regionId + "'");
				} else {
					player.sendMessage(BlockProtectionAPI.PREFIX + "Вы не можете удалить §6чужой §fрегион§7!");
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler()
	public void onBlockPiston(BlockPistonEvent event) {
		//TODO
	}

	@EventHandler()
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();
		if (BlockProtectionAPI.canInteractHere(player, block.getLocation())) {
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
			int regionId = BlockProtectionAPI.getRegionIDByLocation(block.getLocation());
			if (regionId != -1) {
				if (block.getLocation().equals(BlockProtectionAPI.getRegionBlockLocation(regionId))) {
					return true;
				}
			}
			return false;
		});
		event.setBlockList(blocks);
	}

	@EventHandler()
	public void onItemFrameDropItem(ItemFrameDropItemEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();
		if (BlockProtectionAPI.canInteractHere(player, block.getLocation())) {
			player.sendTip("Территория §6не доступна §fдля взаимодействия");
			event.setCancelled(true);
		}
	}

	@EventHandler()
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();
		if (BlockProtectionAPI.canInteractHere(player, block.getLocation())) {
			player.sendTip("Территория §6не доступна §fдля взаимодействия");
			event.setCancelled(true);
		}
	}
}