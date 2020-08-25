package Anarchy.Module.Auction;

import java.util.Map;

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
					AuctionChest inventory = (AuctionChest)slotChange.getInventory();
					Item sourceItem = action.getSourceItem();
					switch (sourceItem.getName()) {
						case "§r§6Следующая Страница":
						{
							AuctionAPI.AUCTION_PAGE.put(player, AuctionAPI.AUCTION_PAGE.get(player) + 1);
							AuctionAPI.showAuction(player, false);
							AuctionAPI.showAuction(player, true);
							player.getLevel().addSound(player, Sound.ITEM_BOOK_PAGE_TURN, 1, 1, player);
						}
						break;
						
						case "§r§6Обновление страницы":
						{
							AuctionAPI.showAuction(player, false);
							AuctionAPI.showAuction(player, true);
							player.getLevel().addSound(player, Sound.RANDOM_LEVELUP, 1, 1, player);
						}
						break;
						
						case "§r§6Справка":
						{
							player.getLevel().addSound(player, Sound.MOB_VILLAGER_HAGGLE, 1, 1, player);
						}
						break;
						
						case "§r§6Предыдущая Страница":
						{
							AuctionAPI.AUCTION_PAGE.put(player, AuctionAPI.AUCTION_PAGE.get(player) - 1);
							AuctionAPI.showAuction(player, false);
							AuctionAPI.showAuction(player, true);
							player.getLevel().addSound(player, Sound.ITEM_BOOK_PAGE_TURN, 1, 1, player);
						}
						break;
						
						case "§r§6Ваши Предметы на Продаже": 
						{
							String playerName = player.getName();
							SellChest sellChest = new SellChest("Ваши Предметы на Продаже");
							for (Map.Entry<String, TradeItem> entry : AuctionAPI.AUCTION.entrySet()) {
								TradeItem tradeItem = entry.getValue();
								if (tradeItem.sellerName.equals(playerName)) {
									Item item = tradeItem.sellItem.clone();
									CompoundTag compoundTag = item.hasCompoundTag() ? item.getNamedTag() : new CompoundTag();
									compoundTag.putString("UUID", tradeItem.UUID);
									item.setNamedTag(compoundTag);
									sellChest.addItem(item.setCustomName("§r§fСтоимость §7- §6" + tradeItem.itemPrice + "\n§r§fДо окончания §7- §3" + (tradeItem.getTime() / 3600) + " §fч§7. §3" + (tradeItem.getTime() / 60 % 60) + " §fмин§7." + (tradeItem.aboutMessage == null ? "" : "\n§r§fОписание §7- §6" + tradeItem.aboutMessage)));
								}
							}
							if (sellChest.isEmpty()) {
								player.sendMessage(AuctionAPI.PREFIX + "§fВы не имеете товаров§7, §fкоторые продаются сейчас§7!");
							} else {
								FakeChestsAPI.openDoubleChestInventory(player, sellChest);
							}
						}
						break;
						
						default: 
						CompoundTag compoundTag = sourceItem.getNamedTag();
						if (compoundTag != null && compoundTag.getString("UUID") != null) {
							TradeItem tradeItem = AuctionAPI.AUCTION.get(compoundTag.getString("UUID"));
							if (tradeItem != null) {
								if (tradeItem.sellerName.equals(player.getName())) {
									player.sendMessage(AuctionAPI.PREFIX + "§fВы пытаетесь купить свой товар§7!\n§l§6| §r§fДля снятия используйте вкладку §7(§3Ваши Предметы на Продаже§7)");
									player.getLevel().addSound(player, Sound.RANDOM_FIZZ, 1, 1, player);
									return;
								}
								if (EconomyAPI.myMoney(player) < tradeItem.itemPrice) {
									player.sendMessage(AuctionAPI.PREFIX + "§fНедостаточно монет§7, §fдля совершения покупки§7!");
									player.getLevel().addSound(player, Sound.RANDOM_FIZZ, 1, 1, player);
									return;
								}
								PlayerInventory playerInventory = player.getInventory();
								if (playerInventory.canAddItem(sourceItem)) {
									inventory.removeItem(sourceItem);
									compoundTag.remove("UUID");
									playerInventory.addItem(sourceItem.clearCustomName().setNamedTag(compoundTag));
									player.getLevel().addSound(player, Sound.RANDOM_LEVELUP, 1, 1, player);
									player.sendMessage(AuctionAPI.PREFIX + "§fПредмет успешно куплен за §6" + tradeItem.itemPrice + "");
									Player sellerPlayer = Server.getInstance().getPlayerExact(tradeItem.sellerName);
									if (sellerPlayer != null) {
										sellerPlayer.sendMessage(AuctionAPI.PREFIX + "§fИгрок §3" + player.getName() + " §fкупил Ваш товар за §6" + tradeItem.itemPrice + "");
										EconomyAPI.addMoney(sellerPlayer, tradeItem.itemPrice);
									} else {
										EconomyAPI.addMoney(tradeItem.sellerName, tradeItem.itemPrice);
									}
									EconomyAPI.reduceMoney(player, tradeItem.itemPrice);
									AuctionAPI.AUCTION.remove(tradeItem.UUID);
								}
							} else {
								inventory.removeItem(sourceItem);
								FakeChestsAPI.closeInventory(player, inventory);
								player.sendMessage(AuctionAPI.PREFIX + "§fПредмет уже продан§7, §fили его сняли с продажи§7!");
							}
						}
						break;
						
					}
				} else if (slotChange.getInventory() instanceof SellChest) {
					event.setCancelled();
					SellChest inventory = (SellChest)slotChange.getInventory();
					Item sourceItem = action.getSourceItem();
					CompoundTag compoundTag = sourceItem.getNamedTag();
					if (compoundTag != null && compoundTag.getString("UUID") != null) {
						TradeItem tradeItem = AuctionAPI.AUCTION.get(compoundTag.getString("UUID"));
						if (tradeItem != null) {
							PlayerInventory playerInventory = player.getInventory();
							if (playerInventory.canAddItem(sourceItem)) {
								compoundTag.remove("UUID");
								inventory.removeItem(sourceItem);
								playerInventory.addItem(sourceItem.setNamedTag(compoundTag).clearCustomName());
								player.getLevel().addSound(player, Sound.RANDOM_LEVELUP, 1, 1, player);
								player.sendMessage(AuctionAPI.PREFIX + "§fПредмет был снят с продажи и отправлен Вам в Инвентарь");
								AuctionAPI.AUCTION.remove(tradeItem.UUID);
							}
						} else {
							FakeChestsAPI.closeInventory(player, inventory);
							player.sendMessage(AuctionAPI.PREFIX + "§fПредмет уже продан§7!");
						}
					}
				} else if (slotChange.getInventory() instanceof TakeChest) {
					Item sourceItem = action.getSourceItem();
					PlayerInventory playerInventory = player.getInventory(); // получение инвенторя
					CompoundTag compoundTag = sourceItem.getNamedTag();
					if (compoundTag.getString("UUID") != null) {
						compoundTag.remove("UUID");
						sourceItem.setNamedTag(compoundTag);
						playerInventory.addItem(sourceItem.setNamedTag(compoundTag).clearCustomName()); // ставит обычный тэг предмета
						player.getLevel().addSound(player, Sound.RANDOM_ORB, 1, 1, player);
						player.sendMessage(AuctionAPI.PREFIX + "§fВы забрали предмет с Хранилища");
					} else {
						event.setCancelled();
					}
				}
			}
		}
	}
}