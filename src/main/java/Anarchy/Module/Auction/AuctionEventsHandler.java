package Anarchy.Module.Auction;

import java.util.Map;

import Anarchy.Module.Auction.Utils.TradeItem;
import Anarchy.Module.Auction.Utils.Inventory.AuctionChest;
import Anarchy.Module.Auction.Utils.Inventory.SellChest;
import Anarchy.Module.Auction.Utils.Inventory.StorageAuctionDoubleChest;
import Anarchy.Module.Economy.EconomyAPI;
import FakeInventoryAPI.FakeInventoryAPI;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.inventory.InventoryTransactionEvent;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.inventory.transaction.action.InventoryAction;
import cn.nukkit.inventory.transaction.action.SlotChangeAction;
import cn.nukkit.item.Item;
import cn.nukkit.level.Sound;
import cn.nukkit.nbt.tag.CompoundTag;

public class AuctionEventsHandler implements Listener {

	@EventHandler(priority = EventPriority.NORMAL)
	public void onInventoryTransaction(InventoryTransactionEvent event) {
		Player player = event.getTransaction().getSource();
		for (InventoryAction action : event.getTransaction().getActions()) {
			if (action instanceof SlotChangeAction) {
				SlotChangeAction slotChange = (SlotChangeAction)action;
				if (slotChange.getInventory() instanceof AuctionChest) {
					event.setCancelled();
					AuctionChest auctionChest = (AuctionChest)slotChange.getInventory();
					Item sourceItem = action.getSourceItem();
					switch (sourceItem.getName()) {
					case "§r§7:D": {
						player.getLevel().addSound(player, Sound.NOTE_BASS, 1, 1, player);
					}
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
							player.sendMessage(AuctionAPI.PREFIX + "§fВы уже находитесь на самой последней странице§7!");
							player.getLevel().addSound(player, Sound.NOTE_BASS, 1, 1, player);
						}
					}
					break;

					case "§r§6Обновление страницы": {
						AuctionAPI.showAuction(player, false);
						AuctionAPI.showAuction(player, true);
						player.getLevel().addSound(player, Sound.RANDOM_LEVELUP, 1, 1, player);
					}
					break;

					case "§r§6Справка": {
						player.getLevel().addSound(player, Sound.MOB_VILLAGER_HAGGLE, 1, 1, player);
					}
					break;

					case "§r§6Листнуть назад": {
						int playerPage = AuctionAPI.AUCTION_PAGE.get(player);
						if (playerPage == 0) {
							player.sendMessage(AuctionAPI.PREFIX + "§fВы уже находитесь на первой странице§7!");
							player.getLevel().addSound(player, Sound.NOTE_BASS, 1, 1, player);
						} else {
							AuctionAPI.AUCTION_PAGE.put(player, AuctionAPI.AUCTION_PAGE.get(player) - 1);
							AuctionAPI.showAuction(player, false);
							AuctionAPI.showAuction(player, true);
							player.getLevel().addSound(player, Sound.ITEM_BOOK_PAGE_TURN, 1, 1, player);
						}
					}
					break;

					case "§r§6Ваши Предметы на Продаже": {
						String playerName = player.getName();
						SellChest sellChest = new SellChest("§l§fВаши Предметы на Продаже");
						for (Map.Entry<String, TradeItem> entry : AuctionAPI.AUCTION.entrySet()) {
							TradeItem tradeItem = entry.getValue();
							if (tradeItem.getSellerName().equals(playerName)) {
								Item item = tradeItem.getSellItem().clone();
								CompoundTag compoundTag = item.hasCompoundTag() ? item.getNamedTag() : new CompoundTag();
								compoundTag.putString("UUID", tradeItem.getUUID());
								item.setNamedTag(compoundTag);
								sellChest.addItem(item.setLore("\n§r§fСтоимость§7: §6" + String.format("%.1f", tradeItem.getItemPrice()) + "\n§r§fДо окончания§7: §6" + (tradeItem.getTime() / 86400 % 24) + " §fд§7. §6" + (tradeItem.getTime() / 3600 % 24) + " §fч§7. §6" + (tradeItem.getTime() / 60 % 60) + " §fмин§7. §6" + (tradeItem.getTime() % 60) + " §fсек§7."));
							}
						}
						FakeInventoryAPI.openDoubleChestInventory(player, sellChest);
					}
					break;

					case "§r§6Хранилище": {
						AuctionAPI.showAuction(player, false);
						StorageAuction.showStorageAuction(player, true);
					}
					break;

					default:
						CompoundTag compoundTag = sourceItem.getNamedTag();
						if (compoundTag != null && compoundTag.getString("UUID") != null) {
							TradeItem tradeItem = AuctionAPI.AUCTION.get(compoundTag.getString("UUID"));
							if (tradeItem != null) {
								if (tradeItem.getSellerName().equals(player.getName())) {
									player.sendMessage(AuctionAPI.PREFIX + "§fВы пытаетесь купить свой товар§7!\n§l§6• §r§fДля снятия используйте вкладку §7(§6Ваши Предметы на Продаже§7)");
									player.getLevel().addSound(player, Sound.NOTE_BASS, 1, 1, player);
									return;
								}
								if (EconomyAPI.myMoney(player.getName()) < tradeItem.getItemPrice()) {
									player.sendMessage(AuctionAPI.PREFIX + "§fНедостаточно монет для совершения покупки§7!");
									player.getLevel().addSound(player, Sound.NOTE_BASS, 1, 1, player);
									return;
								}
								PlayerInventory playerInventory = player.getInventory();
								if (playerInventory.canAddItem(sourceItem)) {
									auctionChest.removeItem(sourceItem);
									compoundTag.remove("UUID");
									playerInventory.addItem(sourceItem.clearCustomName().clearCustomBlockData().setNamedTag(compoundTag).setLore());
									player.getLevel().addSound(player, Sound.RANDOM_LEVELUP, 1, 1, player);
									player.sendMessage(AuctionAPI.PREFIX + "§fПредмет успешно куплен за §6" + String.format("%.1f", tradeItem.getItemPrice()) + "§7, §fв колличестве §6" + sourceItem.getCount() + " §fшт§7.");
									Player sellerPlayer = Server.getInstance().getPlayerExact(tradeItem.getSellerName());
									if (sellerPlayer != null) {
										sellerPlayer.sendMessage(AuctionAPI.PREFIX + "§fИгрок §6" + player.getName() + " §fкупил Ваш товар за §6" + String.format("%.1f", tradeItem.getItemPrice()) + "");
										EconomyAPI.addMoney(sellerPlayer, tradeItem.getItemPrice());
									} else {
										EconomyAPI.addMoney(tradeItem.getSellerName(), tradeItem.getItemPrice());
									}
									EconomyAPI.reduceMoney(player, tradeItem.getItemPrice());
									AuctionAPI.AUCTION.remove(tradeItem.getUUID());
								}
							} else {
								auctionChest.removeItem(sourceItem);
								FakeInventoryAPI.closeInventory(player, auctionChest);
								player.sendMessage(AuctionAPI.PREFIX + "§fПредмет уже продан или его сняли с продажи§7!");
							}
						}

					}
				} else if (slotChange.getInventory() instanceof SellChest) {
					event.setCancelled();
					SellChest sellChest = (SellChest)slotChange.getInventory();
					Item sourceItem = action.getSourceItem();
					CompoundTag compoundTag = sourceItem.getNamedTag();
					if (compoundTag != null && compoundTag.getString("UUID") != null) {
						TradeItem tradeItem = AuctionAPI.AUCTION.get(compoundTag.getString("UUID"));
						if (tradeItem != null) {
							PlayerInventory playerInventory = player.getInventory();
							if (playerInventory.canAddItem(sourceItem)) {
								compoundTag.remove("UUID");
								sellChest.removeItem(sourceItem);
								playerInventory.addItem(sourceItem.clearCustomName().clearCustomBlockData().setNamedTag(compoundTag).setLore());
								player.getLevel().addSound(player, Sound.RANDOM_LEVELUP, 1, 1, player);
								player.sendMessage(AuctionAPI.PREFIX + "§fПредмет был снят с продажи и отправлен Вам в Инвентарь");
								AuctionAPI.AUCTION.remove(tradeItem.getUUID());
							}
						} else {
							FakeInventoryAPI.closeInventory(player, sellChest);
							player.sendMessage(AuctionAPI.PREFIX + "§fПредмет уже продан§7!");
						}
					}
				} else if (slotChange.getInventory() instanceof StorageAuctionDoubleChest) {
					event.setCancelled();
					Item sourceItem = action.getSourceItem();
					StorageAuctionDoubleChest storageChest = (StorageAuctionDoubleChest)slotChange.getInventory();
					switch (sourceItem.getName()) {
					case "§r§6Обновление Хранилища": {
						StorageAuction.showStorageAuction(player, false);
						StorageAuction.showStorageAuction(player, true);
					}
					break;

					default: {
						CompoundTag nbt = sourceItem.getNamedTag();
						if (nbt != null && nbt.getString("UUID") != null) {
							PlayerInventory playerInventory = player.getInventory();
							if (playerInventory.canAddItem(sourceItem)) {
								storageChest.removeItem(sourceItem);
								StorageAuction.getStorageConfig(player).remove(nbt.getString("UUID"));
								StorageAuction.getStorageConfig(player).save();
								StorageAuction.getStorageConfig(player).reload();
								nbt.remove("UUID");
								playerInventory.addItem(sourceItem.clearCustomName().clearCustomBlockData().setNamedTag(nbt).setLore());
								player.getLevel().addSound(player, Sound.RANDOM_ORB, 1, 1, player);
								player.sendMessage(AuctionAPI.PREFIX + "§fПредмет с Хранилища успешно взят§7!");
							}
						}
					}
					break;

					}
				}
			}
		}
	}
}