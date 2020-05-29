package Anarchy.Module.Regions;

import Anarchy.AnarchyMain;
import Anarchy.Manager.Functions.FunctionsAPI;
import Anarchy.Utils.SQLiteUtils;
import Anarchy.Module.Auction.Utils.Form.SimpleTradeForm;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockAir;
import cn.nukkit.blockentity.BlockEntityChest;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.window.FormWindow;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.entity.EntityExplodeEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Sound;
import cn.nukkit.scheduler.NukkitRunnable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class RegionsEventsHandler implements Listener {
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onPlayerFormResponded(PlayerFormRespondedEvent event) {
		Player player = event.getPlayer();
		FormWindow formWindow = event.getWindow();
		if (formWindow.getResponse() == null) {
			return;
		}
		String playerName = player.getName();
		if (formWindow instanceof SimpleTradeForm) {
			SimpleTradeForm simpleTradeForm = (SimpleTradeForm) formWindow;
			switch (simpleTradeForm.getResponse().getClickedButton().getText()) {
				case "Информация о Регионах":
					{
						player.showFormWindow(new FormWindowSimple("Информация о Регионах", "Хочешь создать свой регион? Не проблема! Можешь следовать пунктам:\n\n§l§e| §r§fДобудь блок привта\n§e§l| §r§fПроверь, нет ли вблизи другого региона\n§l§e| §r§fПоставь блок для привата и будь уверене §7- §fтвою постройку не тронут!\n\nБлоки, которыми можно приватить:\n\n§l§e| §r§fЖелезный блок §7(§fприватит 3 × 3§7)\n§l§e| §r§fАлмазный блок §7(§fприватит 6 × 6§7)\n§l§e| §r§fИзумрудный блок §7(§fприватит 10 × 10§7)"));
					}
					break;

				case "Ваши Регионы":
					{
						ArrayList<Integer> regionsData = SQLiteUtils.selectIntegerList("Regions.db", "SELECT `Region_ID` FROM `AREAS` WHERE `Username` = '" + playerName + "';");
						if (regionsData == null || regionsData.isEmpty()) {
							player.showFormWindow(new FormWindowSimple("Регион > Мои регионы", "Вы не имеете регионов!"));
							return;
						}

						StringBuilder stringBuilder = new StringBuilder();
						for (int region_id: regionsData) {
							Map<String, String> regionInfo = RegionsAPI.getRegionInfo(region_id);
							stringBuilder.append("\n §7→ §fРегион §7(§e").append(regionInfo.get("Main_X")).append(", ").append(regionInfo.get("Main_Y")).append(", ").append(regionInfo.get("Main_Z")).append("§7)");
						}

						player.showFormWindow(new FormWindowSimple("Регион > Мои регионы", "Ваши регионы: " + stringBuilder.toString()));
					}
					break;

				case "Список регионов, где Вас добавили":
					{
						ArrayList<Integer> regionsData = SQLiteUtils.selectIntegerList("Regions.db", "SELECT `Region_ID` FROM `MEMBERS` WHERE `Username` = '" + playerName + "';");
						if (regionsData == null || regionsData.isEmpty()) {
							player.showFormWindow(new FormWindowSimple("Список регионов, где Вас добавили", "§fВас не добавили ни в §c1 §fиз регионов!"));
							return;
						}

						StringBuilder stringBuilder = new StringBuilder();
						for (int region_id: regionsData) {
							Map<String, String> regionInfo = RegionsAPI.getRegionInfo(region_id);
							stringBuilder.append("\n §7→ §fРегион Игрока §a").append(regionInfo.get("Username")).append(" §7(§e").append(regionInfo.get("Main_X")).append(", ").append(regionInfo.get("Main_Y")).append(", ").append(regionInfo.get("Main_Z")).append("§7)");
						}

						player.showFormWindow(new FormWindowSimple("Список регионов, где Вас добавили", "§fРегионы, в которых Вы состоите: " + stringBuilder.toString()));
					}
					break;
			}
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();
		if (!RegionsAPI.canInteractHere(player, block.getLocation())) {
			player.sendPopup(RegionsAPI.BUSY);
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
			player.sendPopup(RegionsAPI.BUSY);
			event.setCancelled();
			return;
		}

		int regionID = RegionsAPI.getRegionIDByLocation(block.getLocation());
		if (regionID != -1) {
			Map<String, String> info = RegionsAPI.getRegionInfo(regionID);
			if (block.getFloorX() == Integer.parseInt(info.get("Main_X")) && block.getFloorY() == Integer.parseInt(info.get("Main_Y")) && block.getFloorZ() == Integer.parseInt(info.get("Main_Z"))) {
				if (RegionsAPI.isRegionOwner(player.getName(), regionID)) {
					player.sendMessage(RegionsAPI.PREFIX + "§fВы удалили свой приват!");
					player.getLevel().addSound(player, Sound.MOB_ENDERDRAGON_DEATH, 1, 1, player);
					SQLiteUtils.query("Regions.db", "DELETE FROM `AREAS` WHERE `Region_ID` = '" + regionID + "';");
					SQLiteUtils.query("Regions.db", "DELETE FROM `MEMBERS` WHERE `Region_ID` = '" + regionID + "';");
				} else {
					player.sendMessage(RegionsAPI.PREFIX + "§fВы не можете удалить чужой регион!");
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
			if (player.level != FunctionsAPI.WORLD) {
				player.sendPopup(RegionsAPI.BIOME);
				return;
			}

			int regionID = RegionsAPI.getRegionIDByLocation(block.getLocation());
			if (regionID != -1) {
				player.sendPopup(RegionsAPI.BUSY_BY.replace("{PLAYER}", RegionsAPI.getRegionOwner(regionID)));
				player.getLevel().addSound(player, Sound.RANDOM_FIZZ, 1, 1, player);
			} else {
				player.sendPopup(RegionsAPI.FREE);
				player.getLevel().addSound(player, Sound.RANDOM_LEVELUP, 1, 1, player);
			}
			event.setCancelled();
			return;
		}

		if (!RegionsAPI.canInteractHere(player, block.getLocation())) {
			player.sendPopup(RegionsAPI.BUSY);
			event.setCancelled();
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onEntityExplode(EntityExplodeEvent event) {
		List<Block> blocks = event.getBlockList();
		Iterator iterator = blocks.iterator();
		while (iterator.hasNext()) {
			Block block = (Block) iterator.next();
			int id = block.getId();
			if (RegionsAPI.REGIONS.get(id) != null) {
				iterator.remove();
			} else if (id == 54 || id == 146) {
				iterator.remove();
				new NukkitRunnable() {
					public void run() {
						BlockEntityChest chest = (BlockEntityChest) block.level.getBlockEntity(block);
						if (chest != null) {
							Map<Integer, Item> map = chest.getInventory().getContents();
							for (Map.Entry<Integer, Item> entry: map.entrySet()) {
								block.level.dropItem(block, entry.getValue());
							}
							block.level.setBlock(block, new BlockAir());
						}
					}
				}.runTaskLater(AnarchyMain.plugin, 20);
			} else if (id == 52) {
				iterator.remove();
			}
		}
		event.setBlockList(blocks);
	}
}