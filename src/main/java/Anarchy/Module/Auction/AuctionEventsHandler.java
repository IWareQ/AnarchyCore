package Anarchy.Module.Auction;

import Anarchy.AnarchyMain;
import Anarchy.Manager.FakeChests.FakeChestsAPI;
import Anarchy.Module.Auction.Utils.Form.SimpleTradeForm;
import Anarchy.Module.Auction.Utils.Inventory.AuctionChest;
import Anarchy.Module.Auction.Utils.Inventory.SellChest;
import Anarchy.Module.Auction.Utils.Inventory.TakeChest;
import Anarchy.Module.Auction.Utils.TradeItem;
import Anarchy.Module.Economy.EconomyAPI;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.inventory.InventoryTransactionEvent;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.window.FormWindow;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.inventory.transaction.action.InventoryAction;
import cn.nukkit.inventory.transaction.action.SlotChangeAction;
import cn.nukkit.item.Item;
import cn.nukkit.level.Sound;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Config;

import java.io.File;
import java.io.IOException;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Map;

public class AuctionEventsHandler implements Listener {
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
				case "Аукцион":
					{
						AuctionAPI.AUCTION_PAGE.put(player, 0);
						AuctionAPI.showAuction(player, true);
					}
					break;

				case "Выставленные товары":
					{
						SellChest sellChest = new SellChest("Выставленные товары");
						for (Map.Entry<String, TradeItem> entry: AuctionAPI.AUCTION.entrySet()) {
							TradeItem tradeItem = entry.getValue();
							if (tradeItem.sellerName.equals(playerName)) {
								Item item = tradeItem.sellItem.clone();
								CompoundTag compoundTag = item.hasCompoundTag() ? item.getNamedTag() : new CompoundTag();
								compoundTag.putString("UUID", tradeItem.UUID);
								item.setNamedTag(compoundTag);
								sellChest.addItem(item.setCustomName("§r§fЦена: §e" + tradeItem.itemPrice + "\n§fИстекает через: §c" + (tradeItem.getTime() / 3600) + " §fч. §c" + (tradeItem.getTime() / 60 % 60) + " §fмин." + (tradeItem.aboutMessage == null ? "" : "\n§fОписание: §f" + tradeItem.aboutMessage)));
							}
						}

						if (sellChest.isEmpty()) {
							player.sendMessage(AuctionAPI.PREFIX + "§fВы не имеете товаров, которые продаются сейчас!");
						} else {
							FakeChestsAPI.openDoubleChestInventory(player, sellChest);
						}
					}
					break;

				case "Хранилище":
					{
						File dataFile = new File(AnarchyMain.datapath + "/Auction/PlayerItems/" + playerName + ".yml");
						Config config = new Config(dataFile, Config.YAML);
						if (!dataFile.exists()) {
							player.sendMessage(AuctionAPI.PREFIX + "§fВ Хранилище пусто!");
							return;
						} else {
							if (config.getAll().isEmpty()) {
								dataFile.delete();
							}
						}

						TakeChest takeChest = new TakeChest("Хранилище", dataFile);
						for (Map.Entry<String, Object> entry: config.getAll().entrySet()) {
							ArrayList<Object> itemData = (ArrayList<Object> ) entry.getValue();
							Item item = Item.get((int) itemData.get(0), (int) itemData.get(1), (int) itemData.get(2));
							CompoundTag compoundTag = null;
							if (itemData.size() > 3) {
								try {
									compoundTag = NBTIO.read((byte[]) itemData.get(3), ByteOrder.LITTLE_ENDIAN);
								} catch (IOException e) { /**/ }
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
			}
		}
	}

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
						case "§6Вперед":
							{
								AuctionAPI.AUCTION_PAGE.put(player, AuctionAPI.AUCTION_PAGE.get(player) + 1);
								AuctionAPI.showAuction(player, false);
								player.getLevel().addSound(player, Sound.ITEM_BOOK_PAGE_TURN, 1, 1, player);
							}
							break;

						case "§eНазад":
							{
								AuctionAPI.AUCTION_PAGE.put(player, AuctionAPI.AUCTION_PAGE.get(player) - 1);
								AuctionAPI.showAuction(player, false);
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
										inventory.removeItem(sourceItem);
										return;
									}

									if (EconomyAPI.myMoney(player)<tradeItem.itemPrice) {
										player.sendMessage(AuctionAPI.PREFIX + "§fНедостаточно монет, для совершения покупки!");
										player.getLevel().addSound(player, Sound.RANDOM_FIZZ, 1, 1, player);
										inventory.removeItem(sourceItem);
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
								player.sendMessage(AuctionAPI.PREFIX + "§fВы сняли предмет с продажи");
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
						player.sendMessage(AuctionAPI.PREFIX + "§fВы забрали предиет с хранилища");
					} else {
						event.setCancelled();
					}
				}
			}
		}
	}
}