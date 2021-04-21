package ru.jl1mbo.AnarchyCore.Modules.Auction;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.inventory.InventoryTransactionEvent;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.inventory.transaction.action.InventoryAction;
import cn.nukkit.inventory.transaction.action.SlotChangeAction;
import cn.nukkit.item.Item;
import cn.nukkit.level.Sound;
import cn.nukkit.nbt.tag.CompoundTag;
import ru.jl1mbo.AnarchyCore.Modules.Auction.Utils.Inventory.AuctionChest;
import ru.jl1mbo.AnarchyCore.Modules.Auction.Utils.Inventory.AuctionStorageChest;
import ru.jl1mbo.AnarchyCore.Modules.Auction.Utils.TradeItem;
import ru.jl1mbo.AnarchyCore.Modules.Economy.EconomyAPI;
import ru.jl1mbo.MySQLUtils.MySQLUtils;

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
						case "§r§7:D":
							player.getLevel().addSound(player, Sound.NOTE_BASS, 1, 1, player);
							break;
						case "§r§6Листнуть вперед": {
							int playerPage = AuctionAPI.AUCTION_PAGE.get(player);
							int tradeSize = AuctionAPI.AUCTION.size();
							int start = playerPage * AuctionAPI.AUCTION_CHEST_SIZE;
							if (tradeSize > start + AuctionAPI.AUCTION_CHEST_SIZE) {
								AuctionAPI.AUCTION_PAGE.put(player, AuctionAPI.AUCTION_PAGE.get(player) + 1);
								AuctionAPI.showAuction(player, false);
								AuctionAPI.showAuction(player, true);
								player.getLevel().addSound(player, Sound.ITEM_BOOK_PAGE_TURN, 1, 1, player);
							} else {
								player.sendMessage(AuctionAPI.PREFIX + "Вы уже находитесь на §6последней §fстранице§7!");
								player.getLevel().addSound(player, Sound.NOTE_BASS, 1, 1, player);
							}
							break;
						}
						case "§r§6Обновление страницы":
							AuctionAPI.showAuction(player, true);
							player.getLevel().addSound(player, Sound.RANDOM_LEVELUP, 1, 1, player);
							break;
						case "§r§6Справка":
							player.getLevel().addSound(player, Sound.MOB_VILLAGER_HAGGLE, 1, 1, player);
							break;
						case "§r§6Листнуть назад": {
							int playerPage = AuctionAPI.AUCTION_PAGE.get(player);
							if (playerPage == 0) {
								player.sendMessage(AuctionAPI.PREFIX + "Вы уже находитесь на §6первой §fстранице§7!");
								player.getLevel().addSound(player, Sound.NOTE_BASS, 1, 1, player);
							} else {
								AuctionAPI.AUCTION_PAGE.put(player, AuctionAPI.AUCTION_PAGE.get(player) - 1);
								AuctionAPI.showAuction(player, false);
								AuctionAPI.showAuction(player, true);
								player.getLevel().addSound(player, Sound.ITEM_BOOK_PAGE_TURN, 1, 1, player);
							}
							break;
						}
						case "§r§6Хранилище":
							AuctionAPI.openAuctionStorageChest(player, false);
							break;
						default:
							CompoundTag namedTag = sourceItem.getNamedTag();
							if (namedTag != null && namedTag.contains("ID")) {
								TradeItem tradeItem = AuctionAPI.AUCTION.get(namedTag.getInt("ID"));
								if (tradeItem != null) {
									if (EconomyAPI.myMoney(player.getName()) < tradeItem.getPrice()) {
										player.sendMessage(AuctionAPI.PREFIX + "Недостаточно §6монет §fдля совершения покупки§7!");
										player.getLevel().addSound(player, Sound.NOTE_BASS, 1, 1, player);
										return;
									}
									PlayerInventory playerInventory = player.getInventory();
									if (playerInventory.canAddItem(sourceItem)) {
										if (tradeItem.getSeller().equals(player.getName())) {
											namedTag.remove("ID");
											namedTag.remove("display");
											playerInventory.addItem(sourceItem.setNamedTag(namedTag));
											player.getLevel().addSound(player, Sound.RANDOM_LEVELUP, 1, 1, player);
											player.sendMessage(AuctionAPI.PREFIX + "Предмет был §6снят с продажи §fи отправлен Вам в Инвентарь");
										} else {
											namedTag.remove("ID");
											namedTag.remove("display");
											playerInventory.addItem(sourceItem.setNamedTag(namedTag));
											player.getLevel().addSound(player, Sound.RANDOM_LEVELUP, 1, 1, player);
											player.sendMessage(AuctionAPI.PREFIX + "Предмет успешно куплен за §6" + String.format("%.1f", tradeItem.getPrice()) + "§7, §fв колличестве §6" + sourceItem.getCount() + " §fшт§7.");
											Player target = Server.getInstance().getPlayerExact(tradeItem.getSeller());
											if (target != null) {
												target.sendMessage(AuctionAPI.PREFIX + "Игрок §6" + player.getName() + " §fкупил Ваш товар за §6" + String.format("%.1f", tradeItem.getPrice()) + "");
												EconomyAPI.addMoney(target.getName(), tradeItem.getPrice());
											} else {
												EconomyAPI.addMoney(tradeItem.getSeller(), tradeItem.getPrice());
											}
											EconomyAPI.reduceMoney(player.getName(), tradeItem.getPrice());
										}
										AuctionAPI.removeItem(tradeItem.getId(), sourceItem);
									}
								} else {
									auctionChest.removeItem(sourceItem);
									player.sendMessage(AuctionAPI.PREFIX + "Предмет уже §6продан §fили его §6сняли §fс продажи§7!");
								}
							}
							break;
					}
					event.setCancelled(true);
				} else if (slotChange.getInventory() instanceof AuctionStorageChest) {
					Item sourceItem = action.getSourceItem();
					AuctionStorageChest auctionStorageChest = (AuctionStorageChest) slotChange.getInventory();
					if (sourceItem.getName().equals("§r§6Обновить")) {
						AuctionAPI.openAuctionStorageChest(player, true);
					} else {
						CompoundTag namedTag = sourceItem.getNamedTag();
						if (namedTag != null && namedTag.contains("ID")) {
							PlayerInventory playerInventory = player.getInventory();
							if (playerInventory.canAddItem(sourceItem)) {
								auctionStorageChest.removeItem(sourceItem);
								MySQLUtils.query("DELETE FROM `AuctionStorage` WHERE (`ID`) = '" + namedTag.getInt("ID") + "';");
								namedTag.remove("ID");
								namedTag.remove("display");
								playerInventory.addItem(sourceItem.setNamedTag(namedTag));
								player.getLevel().addSound(player, Sound.RANDOM_ORB, 1, 1, player);
								player.sendMessage(AuctionAPI.PREFIX + "Предмет с Хранилища успешно взят§7!");
							}
						}
					}
					event.setCancelled(true);
				}
			}
		}
	}
}