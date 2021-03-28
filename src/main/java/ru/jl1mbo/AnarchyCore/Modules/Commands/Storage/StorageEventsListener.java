package ru.jl1mbo.AnarchyCore.Modules.Commands.Storage;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.inventory.InventoryTransactionEvent;
import cn.nukkit.event.player.PlayerLocallyInitializedEvent;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.inventory.transaction.action.InventoryAction;
import cn.nukkit.inventory.transaction.action.SlotChangeAction;
import cn.nukkit.item.Item;
import cn.nukkit.level.Sound;
import cn.nukkit.nbt.tag.CompoundTag;
import ru.jl1mbo.AnarchyCore.Modules.Commands.Storage.Commands.StorageCommand;
import ru.jl1mbo.AnarchyCore.Modules.Commands.Storage.Inventory.StorageChest;
import ru.jl1mbo.AnarchyCore.Utils.SQLiteUtils;

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
						if (namedTag != null && namedTag.getInt("UUID") > 0) {
							PlayerInventory playerInventory = player.getInventory();
							if (playerInventory.canAddItem(sourceItem)) {
								doubleChest.removeItem(sourceItem);
								SQLiteUtils.query("Storage.db", "DELETE FROM `Items` WHERE (`ID`) = '" + namedTag.getInt("UUID") + "'");
								namedTag.remove("Id");
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

	@EventHandler()
	public void onPlayerLocallyInitialized(PlayerLocallyInitializedEvent event) {
		Player player = event.getPlayer();
		if (SQLiteUtils.getInteger("Storage.db", "SELECT `ID` FROM `Items` WHERE UPPER (`Name`) = '" + player.getName().toUpperCase() + "'") != -1) {
			player.sendTitle("§l§6Активная покупка", "§7/§fstorage", 0, 60, 0);
		}
	}
}