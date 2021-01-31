package ru.jl1mbo.AnarchyCore.GameHandler.ExperienceBottle.EventsListener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.inventory.InventoryTransactionEvent;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.inventory.transaction.action.InventoryAction;
import cn.nukkit.inventory.transaction.action.SlotChangeAction;
import cn.nukkit.item.Item;
import ru.jl1mbo.AnarchyCore.GameHandler.ExperienceBottle.ExperienceBottleAPI;
import ru.jl1mbo.AnarchyCore.GameHandler.ExperienceBottle.Inventory.Chest;

public class InventoryTransactionListener implements Listener {
	
	@EventHandler()
	public void onInventoryTransaction(InventoryTransactionEvent event) {
		Player player = event.getTransaction().getSource();
		for (InventoryAction action : event.getTransaction().getActions()) {
			if (action instanceof SlotChangeAction) {
				SlotChangeAction slotChange = (SlotChangeAction) action;
				if (slotChange.getInventory() instanceof Chest) {
					event.setCancelled(true);
					Item sourceItem = action.getSourceItem();
					PlayerInventory playerInventory = player.getInventory();
					switch (sourceItem.getName()) {
					case "§r§fБутыль Опыта §7(§630 LVL§7) (§61395 EXP§7)": {
						if (ExperienceBottleAPI.convert(player) >= 1395) {
							double xp = ExperienceBottleAPI.convert(player) - 1395;
							Item bottle = Item.get(Item.EXPERIENCE_BOTTLE);
							bottle.setDamage(1395);
							bottle.setCustomName("§r§fБутыль Опыта §7(§630 LVL§7)");
							if (playerInventory.canAddItem(bottle)) {
								playerInventory.addItem(bottle);
								player.setExperience(0, 0);
								player.addExperience((int) xp);
								player.sendMessage("§l§a| §r§fВы успешно сохранили свой уровень в бутыльке§7!");
							}
						} else {
							player.sendMessage("§l§c| §r§fВам необходимо §61395 EXP §fдля обмена§7!");
						}
					}
					break;
					case "§r§fБутыль Опыта §7(§640 LVL§7) (§62921 EXP§7)": {
						if (ExperienceBottleAPI.convert(player) >= 2921) {
							double xp = ExperienceBottleAPI.convert(player) - 2921;
							Item bottle = Item.get(Item.EXPERIENCE_BOTTLE);
							bottle.setDamage(2921);
							bottle.setCustomName("§r§fБутыль Опыта §7(§640 LVL§7)");
							if (playerInventory.canAddItem(bottle)) {
								playerInventory.addItem(bottle);
								player.setExperience(0, 0);
								player.addExperience((int) xp);
								player.sendMessage("§l§a| §r§fВы успешно сохранили свой уровень в бутыльке§7!");
							}
						} else {
							player.sendMessage("§l§c| §r§fВам необходимо §62921 EXP §fдля обмена§7!");
						}
					}
					break;
					case "§r§fБутыль Опыта §7(§660 LVL§7) (§68672 EXP§7)": {
						if (ExperienceBottleAPI.convert(player) >= 8672) {
							double xp = ExperienceBottleAPI.convert(player) - 8672;
							Item bottle = Item.get(Item.EXPERIENCE_BOTTLE);
							bottle.setDamage(8672);
							bottle.setCustomName("§r§fБутыль Опыта §7(§660 LVL§7)");
							if (playerInventory.canAddItem(bottle)) {
								playerInventory.addItem(bottle);
								player.setExperience(0, 0);
								player.addExperience((int) xp);
								player.sendMessage("§l§a| §r§fВы успешно сохранили свой уровень в бутыльке§7!");
							}
						} else {
							player.sendMessage("§l§c| §r§fВам необходимо §68672 EXP §fдля обмена§7!");
						}
					}
					break;
					}
				}
			}
		}
	}
}