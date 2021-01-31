package ru.jl1mbo.AnarchyCore.CommandsHandler.NPC.EventsListener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.inventory.InventoryTransactionEvent;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.inventory.transaction.action.InventoryAction;
import cn.nukkit.inventory.transaction.action.SlotChangeAction;
import cn.nukkit.item.Item;
import cn.nukkit.level.Sound;
import ru.dragonestia.ironlib.IronLib;
import ru.jl1mbo.AnarchyCore.CommandsHandler.NPC.EventsListener.Inventory.Hopper;

public class InventoryTransactionListener implements Listener {

    @EventHandler()
    public void onInventoryTransaction(InventoryTransactionEvent event) {
        for (InventoryAction action : event.getTransaction().getActions()) {
            if (action instanceof SlotChangeAction) {
                SlotChangeAction slotChange = (SlotChangeAction) action;
                if (slotChange.getInventory() instanceof Hopper) {
                    Player player = event.getTransaction().getSource();
                    Item sourceItem = action.getSourceItem();
                    event.setCancelled(true);
                    switch (sourceItem.getId()) {
                        case Item.COBWEB: {
                            PlayerInventory playerInventory = player.getInventory();
                            Item artifact = IronLib.getInstance().getPrefabManager().getPrefab("cobweb_artifact").getItem(1);
                            Item goldenMoney = IronLib.getInstance().getPrefabManager().getPrefab("golden_money").getItem(5);
                            if (playerInventory.contains(artifact)) {
                                if (playerInventory.canAddItem(goldenMoney)) {
                                    playerInventory.removeItem(artifact);
                                    playerInventory.addItem(goldenMoney);
                                    player.getLevel().addSound(player, Sound.RANDOM_LEVELUP, 1, 1, player);
                                    player.sendMessage("§l§7(§3Собиратель§7) §r§fСпасибо за §6Артефакт§7, §fприходи еще§7!");
                                }
                            } else {
                                player.sendMessage("§l§7(§3Собиратель§7) §r§fНедостаточно §6Артефактов§7, §fдля совершения обмена§7!");
                                player.getLevel().addSound(player, Sound.NOTE_BASS, 1, 1, player);
                            }
                            break;
                        }
                        case Item.BLAZE_ROD: {
                            PlayerInventory playerInventory = player.getInventory();
                            Item artifact = IronLib.getInstance().getPrefabManager().getPrefab("blaze_rod_artifact").getItem(1);
                            Item goldenMoney = IronLib.getInstance().getPrefabManager().getPrefab("golden_money").getItem(5);
                            if (playerInventory.contains(artifact)) {
                                if (playerInventory.canAddItem(goldenMoney)) {
                                    playerInventory.removeItem(artifact);
                                    playerInventory.addItem(goldenMoney);
                                    player.getLevel().addSound(player, Sound.RANDOM_LEVELUP, 1, 1, player);
                                    player.sendMessage("§l§7(§3Собиратель§7) §r§fСпасибо за §6Артефакт§7, §fприходи еще§7!");
                                }
                            } else {
                                player.sendMessage("§l§7(§3Собиратель§7) §r§fНедостаточно §6Артефактов§7, §fдля совершения обмена§7!");
                                player.getLevel().addSound(player, Sound.NOTE_BASS, 1, 1, player);
                            }
                            break;
                        }
                        case Item.ICE_FROSTED: {
                            PlayerInventory playerInventory = player.getInventory();
                            Item artifact = IronLib.getInstance().getPrefabManager().getPrefab("ice_frosted_artifact").getItem(1);
                            Item goldenMoney = IronLib.getInstance().getPrefabManager().getPrefab("golden_money").getItem(5);
                            if (playerInventory.contains(artifact)) {
                                if (playerInventory.canAddItem(goldenMoney)) {
                                    playerInventory.removeItem(artifact);
                                    playerInventory.addItem(goldenMoney);
                                    player.getLevel().addSound(player, Sound.RANDOM_LEVELUP, 1, 1, player);
                                    player.sendMessage("§l§7(§3Собиратель§7) §r§fСпасибо за §6Артефакт§7, §fприходи еще§7!");
                                }
                            } else {
                                player.sendMessage("§l§7(§3Собиратель§7) §r§fНедостаточно §6Артефактов§7, §fдля совершения обмена§7!");
                                player.getLevel().addSound(player, Sound.NOTE_BASS, 1, 1, player);
                            }
                            break;
                        }
                        case Item.SKULL: {
                            PlayerInventory playerInventory = player.getInventory();
                            Item artifact = IronLib.getInstance().getPrefabManager().getPrefab("skull_artifact").getItem(1);
                            Item goldenMoney = IronLib.getInstance().getPrefabManager().getPrefab("golden_money").getItem(5);
                            if (playerInventory.contains(artifact)) {
                                if (playerInventory.canAddItem(goldenMoney)) {
                                    playerInventory.removeItem(artifact);
                                    playerInventory.addItem(goldenMoney);
                                    player.getLevel().addSound(player, Sound.RANDOM_LEVELUP, 1, 1, player);
                                    player.sendMessage("§l§7(§3Собиратель§7) §r§fСпасибо за §6Артефакт§7, §fприходи еще§7!");
                                }
                            } else {
                                player.sendMessage("§l§7(§3Собиратель§7) §r§fНедостаточно §6Артефактов§7, §fдля совершения обмена§7!");
                                player.getLevel().addSound(player, Sound.NOTE_BASS, 1, 1, player);
                            }
                            break;
                        }
                    }
                }
            }
        }
    }
}