package Anarchy.Module.Auction;

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
		for (InventoryAction action: event.getTransaction().getActions()) {
			if (action instanceof SlotChangeAction) {
				SlotChangeAction slotChange = (SlotChangeAction) action;
				if (slotChange.getInventory() instanceof AuctionChest) {
					event.setCancelled();
					AuctionChest inventory = (AuctionChest) slotChange.getInventory();
					Item sourceItem = action.getSourceItem();
					switch (sourceItem.getName()) {
					case "§r§eСледующая Страница\n\n§r§e• §fНажмите, чтобы перейти":
						{
							AuctionAPI.AUCTION_PAGE.put(player, AuctionAPI.AUCTION_PAGE.get(player) + 1);
							AuctionAPI.showAuction(player, false);
							AuctionAPI.showAuction(player, true);
							player.getLevel().addSound(player, Sound.ITEM_BOOK_PAGE_TURN, 1, 1, player);
						}
						break;

					case "§r§eОбновление страницы\n\n§r§e• §fНажмите, чтобы обновить страницу":
						{
							AuctionAPI.showAuction(player, false);
							AuctionAPI.showAuction(player, true);
							player.getLevel().addSound(player, Sound.RANDOM_LEVELUP, 1, 1, player);
						}
						break;

					case "§r§eСправка\n\n§r§fЭто торговая площадка§7, §fкоторая создана\n§r§fдля покупки и продажи предметов§7.\n\n§r§fТорговая площадка также является\n§r§fотличным способом заработать §eМонет§7, §fпродавая\n§r§fфермерские товары§7, §fкоторые могут\n§r§fзаинтересовать других Игроков§7.\n\n§r§fЧтобы выставить предмет на продажу§7,\n§r§fвозьмите его в руку и введите\n§r§e/auc §7(§eцена§7)\n§r§fили\n§r§e/auc §7(§eцена§7) (§eописание§7)":
						{
							player.getLevel().addSound(player, Sound.MOB_VILLAGER_HAGGLE, 1, 1, player);
						}
						break;

					case "§r§eПредыдущая Страница\n\n§r§e• §fНажмите, чтобы перейти":
						{
							AuctionAPI.AUCTION_PAGE.put(player, AuctionAPI.AUCTION_PAGE.get(player) - 1);
							AuctionAPI.showAuction(player, false);
							AuctionAPI.showAuction(player, true);
							player.getLevel().addSound(player, Sound.ITEM_BOOK_PAGE_TURN, 1, 1, player);
						}
						break;

					default:
						CompoundTag compoundTag = sourceItem.getNamedTag();
						if (compoundTag != null && compoundTag.getString("UUID") != null) {
							TradeItem tradeItem = AuctionAPI.AUCTION.get(compoundTag.getString("UUID"));
							if (tradeItem != null) {
								if (tradeItem.sellerName.equals(player.getName())) {
									player.sendMessage(AuctionAPI.PREFIX + "§fВы пытаетесь купить свой товар!\n§l§e| §r§fДля снятия используйте вкладку §l§7(§fВыставленные товары§7)");
									player.getLevel().addSound(player, Sound.RANDOM_FIZZ, 1, 1, player);
									return;
								}

								if (EconomyAPI.myMoney(player) < tradeItem.itemPrice) {
									player.sendMessage(AuctionAPI.PREFIX + "§fНедостаточно монет, для совершения покупки!");
									player.getLevel().addSound(player, Sound.RANDOM_FIZZ, 1, 1, player);
									return;
								}

								PlayerInventory playerInventory = player.getInventory();
								if (playerInventory.canAddItem(sourceItem)) {
									inventory.removeItem(sourceItem);
									compoundTag.remove("UUID");
									playerInventory.addItem(sourceItem.clearCustomName().setNamedTag(compoundTag));
									player.getLevel().addSound(player, Sound.RANDOM_LEVELUP, 1, 1, player);
									player.sendMessage(AuctionAPI.PREFIX + "§fВы купили предмет за §e" + tradeItem.itemPrice + "");
									Player sellerPlayer = Server.getInstance().getPlayerExact(tradeItem.sellerName);
									if (sellerPlayer != null) {
										sellerPlayer.sendMessage(AuctionAPI.PREFIX + "§fИгрок §a" + player.getName() + " §fкупил Ваш товар за §e" + tradeItem.itemPrice + "");
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
								player.sendMessage(AuctionAPI.PREFIX + "§fПредмет уже продан!");
							}
						}
						break;
					}
				} else if (slotChange.getInventory() instanceof SellChest) {
					event.setCancelled();
					SellChest inventory = (SellChest) slotChange.getInventory();
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
								player.sendMessage(AuctionAPI.PREFIX + "§fПредмет был снят с продажи и отправлен в инвентарь");
								AuctionAPI.AUCTION.remove(tradeItem.UUID);
							}
						} else {
							FakeChestsAPI.closeInventory(player, inventory);
							player.sendMessage(AuctionAPI.PREFIX + "§fПредмет уже продан!");
						}
					}
				} else if (slotChange.getInventory() instanceof TakeChest) {
					Item sourceItem = action.getSourceItem();
					CompoundTag compoundTag = sourceItem.getNamedTag();
					if (compoundTag.getString("UUID") != null) {
						compoundTag.remove("UUID");
						sourceItem.setNamedTag(compoundTag);
						player.getLevel().addSound(player, Sound.RANDOM_ORB, 1, 1, player);
						player.sendMessage(AuctionAPI.PREFIX + "§fВы забрали предмет с хранилища");
					} else {
						event.setCancelled();
					}
				}
			}
		}
	}
}