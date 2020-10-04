package Anarchy.Module.Auction;

import java.io.File;
import java.io.IOException;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Map;

import Anarchy.AnarchyMain;
import Anarchy.Manager.FakeChests.FakeChestsAPI;
import Anarchy.Module.Auction.Utils.TradeItem;
import Anarchy.Module.Auction.Utils.Inventory.AuctionChest;
import Anarchy.Module.Auction.Utils.Inventory.SellChest;
import Anarchy.Module.Auction.Utils.Inventory.TakeChest;
import Anarchy.Module.Economy.EconomyAPI;
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
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Config;

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
					case "§r§6Листнуть вперед": {
						int playerPage = AuctionAPI.AUCTION_PAGE.get(player);
						int tradeSize = AuctionAPI.AUCTION.size();
						int start = playerPage * AuctionAPI.AUCTION_CHEST_SIZE;
						int stop;
						if (tradeSize > start + AuctionAPI.AUCTION_CHEST_SIZE) {
							stop = start + AuctionAPI.AUCTION_CHEST_SIZE;
							AuctionAPI.AUCTION_PAGE.put(player, AuctionAPI.AUCTION_PAGE.get(player) + 1);
							AuctionAPI.showAuction(player, false);
							AuctionAPI.showAuction(player, true);
							player.getLevel().addSound(player, Sound.ITEM_BOOK_PAGE_TURN, 1, 1, player);
						} else {
							player.sendMessage(AuctionAPI.PREFIX + "§fВы уже находитесь на самой последней странице§7!");
							player.getLevel().addSound(player, Sound.NOTE_BASS, 1, 1, player);
							stop = tradeSize;
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
							if (tradeItem.sellerName.equals(playerName)) {
								Item item = tradeItem.sellItem.clone();
								CompoundTag compoundTag = item.hasCompoundTag() ? item.getNamedTag() : new CompoundTag();
								compoundTag.putString("UUID", tradeItem.UUID);
								item.setNamedTag(compoundTag);
								sellChest.addItem(item.setCustomName("§r§fСтоимость§7: §6" + String.format("%.1f", tradeItem.itemPrice) + "\n§r§fДо окончания§7: §6" + (tradeItem.getTime() / 3600) + " §fч§7. §6" + (tradeItem.getTime() / 60 % 60) + " §fмин§7. §6" + (tradeItem.getTime() % 60) + " §fсек§7." + (tradeItem.aboutMessage == null ? "" : "\n§r§fОписание§7: §6" + tradeItem.aboutMessage)));
							}
						}
						FakeChestsAPI.openDoubleChestInventory(player, sellChest);
					}
					break;

					case "§r§6Хранилище": {
						File dataFile = new File(AnarchyMain.datapath + "/Auction/PlayerItems/" + player.getName() + ".yml");
						Config config = new Config(dataFile, Config.YAML);
						if (config.getAll().isEmpty()) {
							dataFile.delete();
						}
						TakeChest takeChest = new TakeChest("§r§fХранилище Аукциона", dataFile);
						for (Map.Entry<String, Object> entry : config.getAll().entrySet()) {
							ArrayList<Object> itemData = (ArrayList<Object>)entry.getValue();
							Item item = Item.get((int)itemData.get(0), (int)itemData.get(1), (int)itemData.get(2));
							CompoundTag compoundTag = null;
							if (itemData.size() > 3) {
								try {
									compoundTag = NBTIO.read((byte[])itemData.get(3), ByteOrder.LITTLE_ENDIAN);
								} catch (IOException e) {
									Server.getInstance().getLogger().alert("AuctionEventsHandler: " + e);
									AnarchyMain.sendMessageToChat("AuctionEventsHandler.java\nСмотрите Server.log", 2000000004);
								}
							}
							if (compoundTag == null) {
								compoundTag = new CompoundTag();
							}
							compoundTag.putString("UUID", entry.getKey());
							item.setNamedTag(compoundTag);
							takeChest.addItem(item);
						}
						FakeChestsAPI.openDoubleChestInventory(player, takeChest);
					}
					break;

					default:
						CompoundTag compoundTag = sourceItem.getNamedTag();
						if (compoundTag != null && compoundTag.getString("UUID") != null) {
							TradeItem tradeItem = AuctionAPI.AUCTION.get(compoundTag.getString("UUID"));
							if (tradeItem != null) {
								if (tradeItem.sellerName.equals(player.getName())) {
									player.sendMessage(AuctionAPI.PREFIX + "§fВы пытаетесь купить свой товар§7!\n§l§6| §r§fДля снятия используйте вкладку §7(§3Ваши Предметы на Продаже§7)");
									player.getLevel().addSound(player, Sound.NOTE_BASS, 1, 1, player);
									return;
								}
								if (EconomyAPI.myMoney(player) < tradeItem.itemPrice) {
									player.sendMessage(AuctionAPI.PREFIX + "§fНедостаточно монет§7, §fдля совершения покупки§7!");
									player.getLevel().addSound(player, Sound.NOTE_BASS, 1, 1, player);
									return;
								}
								PlayerInventory playerInventory = player.getInventory();
								if (playerInventory.canAddItem(sourceItem)) {
									auctionChest.removeItem(sourceItem);
									compoundTag.remove("UUID");
									playerInventory.addItem(sourceItem.clearCustomName().setNamedTag(compoundTag));
									player.getLevel().addSound(player, Sound.RANDOM_LEVELUP, 1, 1, player);
									player.sendMessage(AuctionAPI.PREFIX + "§fПредмет успешно куплен за §6" + String.format("%.1f", tradeItem.itemPrice) + "§7, §fв колличестве §6" + sourceItem.getCount() + " §fшт§7.");
									Player sellerPlayer = Server.getInstance().getPlayerExact(tradeItem.sellerName);
									if (sellerPlayer != null) {
										sellerPlayer.sendMessage(AuctionAPI.PREFIX + "§fИгрок §6" + player.getName() + " §fкупил Ваш товар за §6" + String.format("%.1f", tradeItem.itemPrice) + "");
										EconomyAPI.addMoney(sellerPlayer, tradeItem.itemPrice);
									} else {
										EconomyAPI.addMoney(tradeItem.sellerName, tradeItem.itemPrice);
									}
									EconomyAPI.reduceMoney(player, tradeItem.itemPrice);
									AuctionAPI.AUCTION.remove(tradeItem.UUID);
								}
							} else {
								auctionChest.removeItem(sourceItem);
								FakeChestsAPI.closeInventory(player, auctionChest);
								player.sendMessage(AuctionAPI.PREFIX + "§fПредмет уже продан§7, §fили его сняли с продажи§7!");
							}
						}
						break;

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
								playerInventory.addItem(sourceItem.clearCustomName().setNamedTag(compoundTag));
								player.getLevel().addSound(player, Sound.RANDOM_LEVELUP, 1, 1, player);
								player.sendMessage(AuctionAPI.PREFIX + "§fПредмет был снят с продажи и отправлен Вам в Инвентарь");
								AuctionAPI.AUCTION.remove(tradeItem.UUID);
							}
						} else {
							FakeChestsAPI.closeInventory(player, sellChest);
							player.sendMessage(AuctionAPI.PREFIX + "§fПредмет уже продан§7!");
						}
					}
				} else if (slotChange.getInventory() instanceof TakeChest) {
					event.setCancelled();
					Item sourceItem = action.getSourceItem();
					TakeChest takeChest = (TakeChest)slotChange.getInventory();
					CompoundTag compoundTag = sourceItem.getNamedTag();
					if (compoundTag != null && compoundTag.getString("UUID") != null) {
						PlayerInventory playerInventory = player.getInventory();
						if (playerInventory.canAddItem(sourceItem)) {
							takeChest.removeItem(sourceItem);
							compoundTag.remove("UUID");
							playerInventory.addItem(sourceItem.clearCustomName().setNamedTag(compoundTag));
							player.getLevel().addSound(player, Sound.RANDOM_ORB, 1, 1, player);
							player.sendMessage(AuctionAPI.PREFIX + "§r§fПредмет с Хранилища успешно взят§7!");
						}
					} else {
						FakeChestsAPI.closeInventory(player, takeChest);
						player.sendMessage(AuctionAPI.PREFIX + "§fПредмет уже был получен§7!");
					}
				}
			}
		}
	}
}