package ru.iwareq.anarchycore.module.Commands.Storage;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.inventory.InventoryTransactionEvent;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.inventory.transaction.action.InventoryAction;
import cn.nukkit.inventory.transaction.action.SlotChangeAction;
import cn.nukkit.item.Item;
import cn.nukkit.level.Sound;
import cn.nukkit.nbt.tag.CompoundTag;
import ru.iwareq.anarchycore.module.Commands.Storage.Commands.StorageCommand;
import ru.iwareq.anarchycore.module.Commands.Storage.Inventory.StorageChest;

public class StorageEventsListener implements Listener {

	@EventHandler()
	public void onInventoryTransaction(InventoryTransactionEvent event) {
		Player player = event.getTransaction().getSource();
		for (InventoryAction action : event.getTransaction().getActions()) {
			if (action instanceof SlotChangeAction) {
				SlotChangeAction slotChange = (SlotChangeAction) action;
				if (slotChange.getInventory() instanceof StorageChest) {
					event.setCancelled();
					Item sourceItem = action.getSourceItem();
					StorageChest doubleChest = (StorageChest) slotChange.getInventory();
					if (sourceItem.getName().equals("§r§6Обновление Хранилища")) {
						StorageCommand.openStorageChest(player, true);
					} else {
						CompoundTag namedTag = sourceItem.getNamedTag();
						if (namedTag != null && namedTag.contains("ID")) {
							PlayerInventory playerInventory = player.getInventory();
							if (playerInventory.canAddItem(sourceItem)) {
								doubleChest.removeItem(sourceItem);
								// MySQLUtils.query("DELETE FROM `Storage` WHERE (`ID`) = '" + namedTag.getInt("ID") + "'");
								namedTag.remove("ID");
								namedTag.remove("display");
								playerInventory.addItem(sourceItem.setNamedTag(namedTag));
								player.getLevel().addSound(player, Sound.RANDOM_ORB, 1, 1, player);
								player.sendMessage("§l§6•  §rПредмет с §6Хранилища §fуспешно взят§7!");
							}
						}
					}
				}
			}
		}
	}
}