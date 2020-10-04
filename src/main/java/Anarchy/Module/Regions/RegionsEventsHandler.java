package Anarchy.Module.Regions;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import Anarchy.Manager.Functions.FunctionsAPI;
import Anarchy.Utils.SQLiteUtils;
import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.block.ItemFrameDropItemEvent;
import cn.nukkit.event.entity.EntityExplodeEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Sound;

public class RegionsEventsHandler implements Listener {

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();
		if (!RegionsAPI.canInteractHere(player, block.getLocation())) {
			player.sendTip(RegionsAPI.BUSY);
			event.setCancelled();
			return;
		}
		if (RegionsAPI.REGIONS.containsKey(block.getId())) {
			RegionsAPI.placeRegion(player, block, event);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();
		if (!RegionsAPI.canInteractHere(player, block.getLocation())) {
			player.sendTip(RegionsAPI.BUSY);
			event.setCancelled();
			return;
		}
		int regionID = RegionsAPI.getRegionIDByLocation(block.getLocation());
		if (regionID != -1) {
			Map<String, String> info = RegionsAPI.getRegionInfo(regionID);
			if (block.getFloorX() == Integer.parseInt(info.get("Main_X")) && block.getFloorY() == Integer.parseInt(info.get("Main_Y")) && block.getFloorZ() == Integer.parseInt(info.get("Main_Z"))) {
				if (RegionsAPI.isRegionOwner(player.getName(), regionID)) {
					player.sendMessage(RegionsAPI.PREFIX + "§fПриват §3успешно §fудален§7!");
					player.getLevel().addSound(player, Sound.MOB_ENDERDRAGON_DEATH, 1, 1, player);
					SQLiteUtils.query("Regions.db", "DELETE FROM `AREAS` WHERE `Region_ID` = \'" + regionID + "\';");
					SQLiteUtils.query("Regions.db", "DELETE FROM `MEMBERS` WHERE `Region_ID` = \'" + regionID + "\';");
				} else {
					player.sendMessage(RegionsAPI.PREFIX + "§fВы не можете удалить чужой регион§7!");
					event.setCancelled();
				}
			}
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();
		Item item = event.getItem();
		if (item != null && item.getId() == Item.STICK) {
			if (player.level != FunctionsAPI.MAP) {
				player.sendTip(RegionsAPI.BIOME);
				return;
			}
			int regionID = RegionsAPI.getRegionIDByLocation(block.getLocation());
			if (regionID != -1) {
				player.sendTip(RegionsAPI.BUSY_BY.replace("{PLAYER}", RegionsAPI.getRegionOwner(regionID)));
				player.getLevel().addSound(player, Sound.RANDOM_FIZZ, 1, 1, player);
			} else {
				player.sendTip(RegionsAPI.FREE);
				player.getLevel().addSound(player, Sound.RANDOM_LEVELUP, 1, 1, player);
			}
			event.setCancelled();
			return;
		}
		if (!RegionsAPI.canInteractHere(player, block.getLocation())) {
			player.sendTip(RegionsAPI.BUSY);
			event.setCancelled();
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onItemFrameDropItem(ItemFrameDropItemEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();
		if (!RegionsAPI.canInteractHere(player, block.getLocation())) {
			player.sendTip(RegionsAPI.BUSY);
			event.setCancelled();
			return;
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onEntityExplode(EntityExplodeEvent event) {
		List<Block> blocks = event.getBlockList();
		Iterator iterator = blocks.iterator();
		while (iterator.hasNext()) {
			Block block = (Block)iterator.next();
			int id = block.getId();
			if (RegionsAPI.REGIONS.get(id) != null) {
				iterator.remove();
			}
		}
		event.setBlockList(blocks);
	}
}