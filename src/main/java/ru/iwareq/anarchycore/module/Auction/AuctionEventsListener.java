package ru.iwareq.anarchycore.module.Auction;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.inventory.InventoryTransactionEvent;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.inventory.transaction.action.InventoryAction;
import cn.nukkit.inventory.transaction.action.SlotChangeAction;
import cn.nukkit.item.Item;
import cn.nukkit.level.Sound;
import cn.nukkit.nbt.tag.CompoundTag;
import ru.iwareq.anarchycore.module.Auction.Utils.Inventory.AuctionChest;
import ru.iwareq.anarchycore.module.Auction.Utils.Inventory.AuctionStorageChest;

public class AuctionEventsListener implements Listener {

	@EventHandler()
	public void onInventoryTransaction(InventoryTransactionEvent event) {
		Player player = event.getTransaction().getSource();
		for (InventoryAction action : event.getTransaction().getActions()) {
			if (action instanceof SlotChangeAction) {
				SlotChangeAction slotChange = (SlotChangeAction) action;
				if (slotChange.getInventory() instanceof AuctionChest) {
					AuctionChest auctionChest = (AuctionChest) slotChange.getInventory();
					Item sourceItem = action.getSourceItem();
					switch (sourceItem.getName()) {
						case "§r§6Листнуть вперед": {
							int playerPage = Auction.getPlayerPage(player);
							if (playerPage == Auction.getPagesCount()) {
								Auction.showAuction(player, 1);
							} else {
								Auction.showAuction(player, playerPage + 1);
							}

							player.getLevel().addSound(player, Sound.ITEM_BOOK_PAGE_TURN, 1, 1, player);
							break;
						}

						case "§r§6Справка":
							player.getLevel().addSound(player, Sound.MOB_VILLAGER_HAGGLE, 1, 1, player);
							break;
						case "§r§6Листнуть назад": {
							int playerPage = Auction.getPlayerPage(player);
							if (playerPage == 1) {
								Auction.showAuction(player, Auction.getPagesCount());
							} else {
								Auction.showAuction(player, playerPage - 1);
							}

							player.getLevel().addSound(player, Sound.ITEM_BOOK_PAGE_TURN, 1, 1, player);
							break;
						}
						case "§r§6Хранилище":
							Auction.showStorageAuction(player);
							break;
						default:
							CompoundTag namedTag = sourceItem.getNamedTag();
							if (namedTag != null && namedTag.contains("ID")) {
								Auction.buyItem(player, namedTag.getInt("ID"), sourceItem);
							}
							break;
					}
					event.setCancelled(true);
				} else if (slotChange.getInventory() instanceof AuctionStorageChest) {
					Item sourceItem = action.getSourceItem();
					AuctionStorageChest auctionStorageChest = (AuctionStorageChest) slotChange.getInventory();

					if (sourceItem.getName().equalsIgnoreCase("§r§6Назад")) {
						Auction.showAuction(player, Auction.getPlayerPage(player));
						event.setCancelled(true);
						return;
					}

					CompoundTag namedTag = sourceItem.getNamedTag();
					if (namedTag != null && namedTag.contains("ID")) {
						PlayerInventory playerInventory = player.getInventory();
						if (playerInventory.canAddItem(sourceItem)) {
							auctionStorageChest.removeItem(sourceItem);
							Auction.removeStorageItem(player.getName(), namedTag.getInt("ID"));
							namedTag.remove("ID");
							namedTag.remove("display");
							playerInventory.addItem(sourceItem.setNamedTag(namedTag));
							player.getLevel().addSound(player, Sound.RANDOM_ORB, 1, 1, player);
							player.sendMessage(Auction.PREFIX + "Предмет с Хранилища успешно взят§7!");
						}
					}

					event.setCancelled(true);
				}
			}
		}
	}
}