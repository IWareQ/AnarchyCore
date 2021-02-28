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
import ru.jl1mbo.AnarchyCore.CommandsHandler.NPC.EventsListener.Inventory.Hopper;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.CustomManager.Utils.CustomItemID;

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
					Item goldenMoney = Item.get(CustomItemID.GOLDEN_MONEY);
					switch (sourceItem.getId()) {
					case 1004: {
						PlayerInventory playerInventory = player.getInventory();
						Item artifact = Item.get(CustomItemID.TARANTULA_WEB);
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
					}
					break;

					case 1000: {
						PlayerInventory playerInventory = player.getInventory();
						Item artifact = Item.get(CustomItemID.SUN_WAND);
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
					case 1001: {
						PlayerInventory playerInventory = player.getInventory();
						Item artifact = Item.get(CustomItemID.PIECE_ICE);
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
					}
					break;
					case 1002: {
						PlayerInventory playerInventory = player.getInventory();
						Item artifact = Item.get(CustomItemID.REMAINS_GUARDIAN);
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
					}
					break;
					}
				}
			}
		}
	}
}