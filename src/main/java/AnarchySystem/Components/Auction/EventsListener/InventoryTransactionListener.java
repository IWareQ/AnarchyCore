package AnarchySystem.Components.Auction.EventsListener;

import AnarchySystem.Main;
import AnarchySystem.Components.Auction.AuctionAPI;
import AnarchySystem.Components.Auction.StorageAuction;
import AnarchySystem.Components.Auction.Utils.TradeItem;
import AnarchySystem.Components.Auction.Utils.Inventory.AuctionChest;
import AnarchySystem.Components.Auction.Utils.Inventory.StorageAuctionDoubleChest;
import AnarchySystem.Components.EconomyAPI.EconomyAPI;
import AnarchySystem.Manager.FakeInventory.FakeInventoryAPI;
import AnarchySystem.Utils.ConfigUtils;
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
                                player.sendMessage(AuctionAPI.PREFIX + "§fВы уже находитесь на самой последней странице§7!");
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
                                player.sendMessage(AuctionAPI.PREFIX + "§fВы уже находитесь на первой странице§7!");
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
                            CompoundTag compoundTag = sourceItem.getNamedTag();
                            if (compoundTag != null && compoundTag.getString("UUID") != null) {
                                TradeItem tradeItem = AuctionAPI.AUCTION.get(compoundTag.getString("UUID"));
                                if (tradeItem != null) {
                                    if (EconomyAPI.myMoney(player.getName()) < tradeItem.itemPrice) {
                                        player.sendMessage(AuctionAPI.PREFIX + "§fНедостаточно монет для совершения покупки§7!");
                                        player.getLevel().addSound(player, Sound.NOTE_BASS, 1, 1, player);
                                        return;
                                    }
                                    PlayerInventory playerInventory = player.getInventory();
                                    if (playerInventory.canAddItem(sourceItem)) {
                                        if (tradeItem.sellerName.equals(player.getName())) {
                                            compoundTag.remove("UUID");
                                            auctionChest.removeItem(sourceItem);
                                            playerInventory.addItem(sourceItem.setNamedTag(compoundTag).setLore(null));
                                            player.getLevel().addSound(player, Sound.RANDOM_LEVELUP, 1, 1, player);
                                            player.sendMessage(AuctionAPI.PREFIX + "§fПредмет был снят с продажи и отправлен Вам в Инвентарь");
                                        } else {
                                            auctionChest.removeItem(sourceItem);
                                            compoundTag.remove("UUID");
                                            playerInventory.addItem(sourceItem.setNamedTag(compoundTag).setLore(null));
                                            player.getLevel().addSound(player, Sound.RANDOM_LEVELUP, 1, 1, player);
                                            player.sendMessage(AuctionAPI.PREFIX + "§fПредмет успешно куплен за §6" + String.format("%.1f", tradeItem.itemPrice) + "§7, §fв колличестве §6" + sourceItem.getCount() + " §fшт§7.");
                                            Player sellerPlayer = Server.getInstance().getPlayerExact(tradeItem.sellerName);
                                            if (sellerPlayer != null) {
                                                sellerPlayer.sendMessage(AuctionAPI.PREFIX + "§fИгрок §6" + player.getName() + " §fкупил Ваш товар за §6" + String.format("%.1f", tradeItem.itemPrice) + "");
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
                                    player.sendMessage(AuctionAPI.PREFIX + "§fПредмет уже продан или его сняли с продажи§7!");
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
                        CompoundTag nbt = sourceItem.getNamedTag();
                        Config config = ConfigUtils.getAuctionStorageConfig(player.getName());
                        if (nbt != null && nbt.getString("UUID") != null) {
                            PlayerInventory playerInventory = player.getInventory();
                            if (playerInventory.canAddItem(sourceItem)) {
                                storageChest.removeItem(sourceItem);
                                config.remove(nbt.getString("UUID"));
                                config.save();
                                config.reload();
                                nbt.remove("UUID");
                                playerInventory.addItem(sourceItem.setNamedTag(nbt).setLore());
                                player.getLevel().addSound(player, Sound.RANDOM_ORB, 1, 1, player);
                                player.sendMessage(AuctionAPI.PREFIX + "§fПредмет с Хранилища успешно взят§7!");
                            }
                        }
                    }
                }
            }
        }
    }
}