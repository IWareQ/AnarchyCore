package ru.jl1mbo.AnarchyCore.CommandsHandler.StorageItems.EventsListener;

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
import ru.jl1mbo.AnarchyCore.CommandsHandler.StorageItems.Inventory.DoubleChest;
import ru.jl1mbo.AnarchyCore.CommandsHandler.StorageItems.StorageItemsAPI;

public class InventoryTransactionListener implements Listener {

	@EventHandler()
	public void onInventoryTransaction(InventoryTransactionEvent event) {
		Player player = event.getTransaction().getSource();
		for (InventoryAction action : event.getTransaction().getActions()) {
			if (action instanceof SlotChangeAction) {
				SlotChangeAction slotChange = (SlotChangeAction) action;
				if (slotChange.getInventory() instanceof DoubleChest) {
					event.setCancelled();
					Item sourceItem = action.getSourceItem();
					DoubleChest doubleChest = (DoubleChest) slotChange.getInventory();
					if (sourceItem.getName().equals("§r§6Обновление Хранилища")) {
						StorageItemsAPI.showStorageItemsChest(player, false);
						StorageItemsAPI.showStorageItemsChest(player, true);
					} else {
						CompoundTag namedTag = sourceItem.getNamedTag();
						if (namedTag != null && namedTag.getString("UUID") != null) {
							PlayerInventory playerInventory = player.getInventory();
							if (playerInventory.canAddItem(sourceItem)) {
								doubleChest.removeItem(sourceItem);
								StorageItemsAPI.removeItem(player, namedTag.getString("UUID"));
								namedTag.remove("UUID");
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