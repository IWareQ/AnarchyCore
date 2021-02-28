package ru.jl1mbo.AnarchyCore.GameHandler.Auction.EventsListener;

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
import cn.nukkit.utils.Config;
import ru.jl1mbo.AnarchyCore.GameHandler.Auction.AuctionAPI;
import ru.jl1mbo.AnarchyCore.GameHandler.Auction.StorageAuction;
import ru.jl1mbo.AnarchyCore.GameHandler.Auction.Utils.TradeItem;
import ru.jl1mbo.AnarchyCore.GameHandler.Auction.Utils.Inventory.AuctionChest;
import ru.jl1mbo.AnarchyCore.GameHandler.Auction.Utils.Inventory.StorageAuctionDoubleChest;
import ru.jl1mbo.AnarchyCore.Manager.FakeInventory.FakeInventoryAPI;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.EconomyAPI.EconomyAPI;
import ru.jl1mbo.AnarchyCore.Utils.ConfigUtils;

public class InventoryTransactionListener implements Listener {

	@EventHandler()
	public void onInventoryTransaction(InventoryTransactionEvent event) {
		Player player = event.getTransaction().getSource();
		for (InventoryAction action : event.getTransaction().getActions()) {
			if (action instanceof SlotChangeAction) {
				SlotChangeAction slotChange = (SlotChangeAction) action;
				if (slotChange.getInventory() instanceof AuctionChest) {
					event.setCancelled();
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
						AuctionAPI.showAuction(player, false);
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
						AuctionAPI.showAuction(player, false);
						FakeInventoryAPI.closeDoubleChestInventory(player, auctionChest);
						StorageAuction.showStorageAuction(player, true);
						break;
					default:
						CompoundTag namedTag = sourceItem.getNamedTag();
						if (namedTag != null && namedTag.getString("UUID") != null) {
							TradeItem tradeItem = AuctionAPI.AUCTION.get(namedTag.getString("UUID"));
							if (tradeItem != null) {
								if (EconomyAPI.myMoney(player.getName()) < tradeItem.itemPrice) {
									player.sendMessage(AuctionAPI.PREFIX + "Недостаточно §6монет §fдля совершения покупки§7!");
									player.getLevel().addSound(player, Sound.NOTE_BASS, 1, 1, player);
									return;
								}
								PlayerInventory playerInventory = player.getInventory();
								if (playerInventory.canAddItem(sourceItem)) {
									if (tradeItem.sellerName.equals(player.getName())) {
										namedTag.remove("UUID");
										namedTag.remove("display");
										auctionChest.removeItem(sourceItem);
										playerInventory.addItem(sourceItem.setNamedTag(namedTag));
										player.getLevel().addSound(player, Sound.RANDOM_LEVELUP, 1, 1, player);
										player.sendMessage(AuctionAPI.PREFIX + "Предмет был §6снят с продажи §fи отправлен Вам в Инвентарь");
									} else {
										auctionChest.removeItem(sourceItem);
										namedTag.remove("UUID");
										namedTag.remove("display");
										auctionChest.removeItem(sourceItem);
										playerInventory.addItem(sourceItem.setNamedTag(namedTag));
										player.getLevel().addSound(player, Sound.RANDOM_LEVELUP, 1, 1, player);
										player.sendMessage(AuctionAPI.PREFIX + "Предмет успешно куплен за §6" + String.format("%.1f",
														   tradeItem.itemPrice) + "§7, §fв колличестве §6" + sourceItem.getCount() + " §fшт§7.");
										Player sellerPlayer = Server.getInstance().getPlayerExact(tradeItem.sellerName);
										if (sellerPlayer != null) {
											sellerPlayer.sendMessage(AuctionAPI.PREFIX + "Игрок §6" + player.getName() + " §fкупил Ваш товар за §6" + String.format("%.1f", tradeItem.itemPrice) + "");
											EconomyAPI.addMoney(sellerPlayer.getName(), tradeItem.itemPrice);
										} else {
											EconomyAPI.addMoney(tradeItem.sellerName, tradeItem.itemPrice);
										}
										EconomyAPI.reduceMoney(player.getName(), tradeItem.itemPrice);
									}
									AuctionAPI.AUCTION.remove(tradeItem.UUID);
								}
							} else {
								auctionChest.removeItem(sourceItem);
								FakeInventoryAPI.closeInventory(player, auctionChest);
								player.sendMessage(AuctionAPI.PREFIX + "Предмет уже §6продан §fили его §6сняли §fс продажи§7!");
							}
						}
						break;
					}
				} else if (slotChange.getInventory() instanceof StorageAuctionDoubleChest) {
					event.setCancelled();
					Item sourceItem = action.getSourceItem();
					StorageAuctionDoubleChest storageChest = (StorageAuctionDoubleChest) slotChange.getInventory();
					if (sourceItem.getName().equals("§r§6Обновление Хранилища")) {
						StorageAuction.showStorageAuction(player, false);
						StorageAuction.showStorageAuction(player, true);
					} else {
						CompoundTag namedTag = sourceItem.getNamedTag();
						Config config = ConfigUtils.getAuctionStorageConfig(player.getName());
						if (namedTag != null && namedTag.getString("UUID") != null) {
							PlayerInventory playerInventory = player.getInventory();
							if (playerInventory.canAddItem(sourceItem)) {
								storageChest.removeItem(sourceItem);
								config.remove(namedTag.getString("UUID"));
								config.save();
								config.reload();
								namedTag.remove("UUID");
								namedTag.remove("display");
								playerInventory.addItem(sourceItem.setNamedTag(namedTag));
								player.getLevel().addSound(player, Sound.RANDOM_ORB, 1, 1, player);
								player.sendMessage(AuctionAPI.PREFIX + "Предмет с Хранилища успешно взят§7!");
							}
						}
					}
				}
			}
		}
	}
}