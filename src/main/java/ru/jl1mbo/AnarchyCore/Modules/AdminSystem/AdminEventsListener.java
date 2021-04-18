package ru.jl1mbo.AnarchyCore.Modules.AdminSystem;

import java.util.HashMap;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityBarrel;
import cn.nukkit.blockentity.BlockEntityChest;
import cn.nukkit.blockentity.BlockEntityShulkerBox;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.ProjectileLaunchEvent;
import cn.nukkit.event.inventory.InventoryTransactionEvent;
import cn.nukkit.event.player.PlayerBucketEmptyEvent;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.event.player.PlayerCommandPreprocessEvent;
import cn.nukkit.event.player.PlayerDropItemEvent;
import cn.nukkit.event.player.PlayerFoodLevelChangeEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.inventory.transaction.action.InventoryAction;
import cn.nukkit.inventory.transaction.action.SlotChangeAction;
import cn.nukkit.inventory.transaction.data.UseItemData;
import cn.nukkit.item.Item;
import cn.nukkit.level.Sound;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.InventoryTransactionPacket;
import ru.jl1mbo.AnarchyCore.Manager.FakeInventory.FakeInventoryAPI;
import ru.jl1mbo.AnarchyCore.Modules.AdminSystem.Inventory.AdminChest;
import ru.jl1mbo.AnarchyCore.Utils.Utils;

public class AdminEventsListener implements Listener {

	@EventHandler()
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (AdminAPI.isBanned(player.getName())) {
			Map<String, String> banData = AdminAPI.getBanData(player.getName());
			if (Long.parseLong(banData.get("Time")) <= System.currentTimeMillis() / 1000L) {
				AdminAPI.removeBan(player.getName(), "", "buy");
			} else {
				player.setImmobile(true);
			}
		}
		if (AdminAPI.isSpectate(player.getName())) {
			AdminAPI.removeSpectate(player);
		}
	}

	@EventHandler()
	public void onDataPacketReceive(DataPacketReceiveEvent event) {
		DataPacket dataPacket = event.getPacket();
		Player player = event.getPlayer();
		if (dataPacket instanceof InventoryTransactionPacket) {
			InventoryTransactionPacket transactionPacket = (InventoryTransactionPacket)dataPacket;
			if (transactionPacket.transactionType == InventoryTransactionPacket.TYPE_USE_ITEM) {
				UseItemData useItemData = (UseItemData)transactionPacket.transactionData;
				Vector3 blockVector3 = new Vector3(useItemData.blockPos.getX(), useItemData.blockPos.getY(), useItemData.blockPos.getZ());
				Block block = player.getLevel().getBlock(blockVector3);
				Item item = player.getInventory().getItemInHand();
				Map<String, String> spectateData = AdminAPI.getSpectateData(player.getName());
				switch (item.getId()) {
				case Item.REDSTONE_DUST:
					if (AdminAPI.isSpectate(player.getName()) && player.getGamemode() == 3) {
						AdminAPI.removeSpectate(player);
					}
					break;

				case Item.CHEST:
					if (AdminAPI.isSpectate(player.getName()) && player.getGamemode() == 3) {
						AdminAPI.openCheckInventoryChest(player, spectateData.get("Target"));
					}
					break;

				case Item.CLOCK:
					if (AdminAPI.isSpectate(player.getName()) && player.getGamemode() == 3) {
						AdminAPI.sendAdminPanelForm(player, spectateData.get("Target"));
					}
					break;

				case Item.MOB_SPAWNER:
					if (AdminAPI.isSpectate(player.getName()) && player.getGamemode() == 3) {
						AdminAPI.addCheatCheacker(player, Server.getInstance().getPlayerExact(spectateData.get("Target")));
					}
					break;

				default:
					if (player.getGamemode() == 3) {
						BlockEntity blockEntity = block.getLevel().getBlockEntity(blockVector3);
						if (blockEntity instanceof BlockEntityChest) {
							BlockEntityChest blockEntityChest = (BlockEntityChest)blockEntity;
							Map<Integer, Item> contents = blockEntityChest.getInventory().getContents();
							AdminChest doubleChest = new AdminChest("\u041f\u0440\u043e\u0441\u043c\u043e\u0442\u0440 \u0441\u043e\u0434\u0435\u0440\u0436\u0438\u043c\u043e\u0433\u043e");
							doubleChest.setContents(contents);
							FakeInventoryAPI.openInventory(player, doubleChest);
						} else if (blockEntity instanceof BlockEntityBarrel) {
							BlockEntityBarrel entityBarrel = (BlockEntityBarrel)blockEntity;
							Map<Integer, Item> contents = entityBarrel.getInventory().getContents();
							AdminChest doubleChest = new AdminChest("\u041f\u0440\u043e\u0441\u043c\u043e\u0442\u0440 \u0441\u043e\u0434\u0435\u0440\u0436\u0438\u043c\u043e\u0433\u043e");
							doubleChest.setContents(contents);
							FakeInventoryAPI.openInventory(player, doubleChest);
						} else if (blockEntity instanceof BlockEntityShulkerBox) {
							BlockEntityShulkerBox entityShulkerBox = (BlockEntityShulkerBox)blockEntity;
							Map<Integer, Item> contents = entityShulkerBox.getInventory().getContents();
							AdminChest doubleChest = new AdminChest("\u041f\u0440\u043e\u0441\u043c\u043e\u0442\u0440 \u0441\u043e\u0434\u0435\u0440\u0436\u0438\u043c\u043e\u0433\u043e");
							doubleChest.setContents(contents);
							FakeInventoryAPI.openInventory(player, doubleChest);
						}
					}
					break;

				}
			}
		}
	}

	@EventHandler()
	public void onInventoryTransaction(InventoryTransactionEvent event) {
		for (InventoryAction action : event.getTransaction().getActions()) {
			if (action instanceof SlotChangeAction) {
				SlotChangeAction slotChange = (SlotChangeAction) action;
				if (slotChange.getInventory() instanceof AdminChest) {
					Player player = event.getTransaction().getSource();
					AdminChest doubleChest = (AdminChest) slotChange.getInventory();
					Item sourceItem = action.getSourceItem();
					event.setCancelled(true);
					CompoundTag namedTag = sourceItem.getNamedTag();
					if (sourceItem.getName().equals("§r§6Назад")) {
						if (namedTag != null && namedTag.getString("Target") != null) {
							AdminAPI.openCheckInventoryChest(player, namedTag.getString("Target"));
						}
					} else if (sourceItem.getName().equals("§r§6Открыть Сундук Края")) {
						if (namedTag != null && namedTag.getString("Target") != null) {
							AdminAPI.openCheckEnderChest(player, namedTag.getString("Target"));
						}
					} else if (sourceItem.getName().equals("§r§6Справка")) {
						player.getLevel().addSound(player, Sound.MOB_VILLAGER_HAGGLE, 1, 1, player);
					} else if (sourceItem.getId() == Item.UNDYED_SHULKER_BOX) {
						ListTag<CompoundTag> list = (ListTag<CompoundTag>) sourceItem.getNamedTag().getList("Items");
						if (list != null) {
							for (CompoundTag compound : list.getAll()) {
								Item item = NBTIO.getItemHelper(compound);
								for (int i = 0; i < list.size(); i++) {
									doubleChest.setItem(i, item);
								}
							}
						}
					} else {
						player.getLevel().addSound(player, Sound.NOTE_BASSATTACK, 1, 1, player);
					}
				}
			}
		}
	}

	@EventHandler()
	public void onPlayerChat(PlayerChatEvent event) {
		Player player = event.getPlayer();
		if (AdminAPI.isMuted(player.getName())) {
			Map<String, String> muteData = AdminAPI.getMuteData(player.getName());
			if (Long.parseLong(muteData.get("Time")) <= System.currentTimeMillis() / 1000L) {
				AdminAPI.removeMute(player.getName(), "", "buy");
			} else {
				player.sendMessage("§l§6• §rТебя замутили§7! §fАдминистратор закрыл тебе доступ к чату на §6" + Utils.getRemainingTime(Long.parseLong(
									   muteData.get("Time"))) + " §fпо причине §6" + muteData.get("Reason") + "§7!\n§fНо не расстраивайся§7, §fвсё наладится§7!");
				event.setCancelled(true);
			}
		}
		if (AdminAPI.isBanned(player.getName())) {
			player.sendMessage("§l§6• §rАккаунт §6заблокирован§7!");
			event.setCancelled(true);
		}
	}

	@EventHandler()
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		if (AdminAPI.isBanned(player.getName()) || AdminAPI.isCheatCheck(player.getName())) {
			event.setCancelled(true);
		}
	}

	@EventHandler()
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		if (AdminAPI.isBanned(player.getName()) || AdminAPI.isCheatCheck(player.getName())) {
			event.setCancelled(true);
		}
	}

	@EventHandler()
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		Entity damager = event.getDamager();
		if (damager instanceof Player) {
			Player player = (Player)damager;
			if (AdminAPI.isBanned(player.getName()) || AdminAPI.isCheatCheck(player.getName())) {
				event.setCancelled(true);
				player.setImmobile(true);
			}
		}
	}

	@EventHandler()
	public void onEntityDamage(EntityDamageEvent event) {
		Entity entity = event.getEntity();
		if (entity instanceof Player) {
			Player player = (Player)entity;
			if (AdminAPI.isBanned(player.getName()) || AdminAPI.isCheatCheck(player.getName())) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler()
	public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
		Player player = event.getPlayer();
		if (AdminAPI.isBanned(player.getName()) || AdminAPI.isCheatCheck(player.getName())) {
			event.setCancelled(true);
		}
	}

	@EventHandler()
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		if (AdminAPI.isBanned(player.getName())) {
			event.setCancelled(true);
		}
	}

	@EventHandler()
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		if (AdminAPI.isBanned(player.getName()) || AdminAPI.isCheatCheck(player.getName())) {
			event.setCancelled(true);
		}
	}

	@EventHandler()
	public void onPlayerFoodLevelChange(PlayerFoodLevelChangeEvent event) {
		Player player = event.getPlayer();
		if (AdminAPI.isBanned(player.getName()) || AdminAPI.isCheatCheck(player.getName())) {
			event.setCancelled(true);
		}
	}

	@EventHandler()
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if (AdminAPI.isCheatCheck(player.getName())) {
			AdminAPI.removeCheatCheacker(player.getName());
			AdminAPI.addBan(player.getName(), "перезаход при проверке", "CheatCheacker", 30 * 86400);
		}
	}

	@EventHandler()
	public void onProjectileLaunch(ProjectileLaunchEvent event) {
		Entity entity = event.getEntity();
		if (entity instanceof Player) {
			Player player = (Player)entity;
			if (AdminAPI.isBanned(player.getName()) || AdminAPI.isCheatCheck(player.getName())) {
				event.setCancelled(true);
			}
		}
	}
}