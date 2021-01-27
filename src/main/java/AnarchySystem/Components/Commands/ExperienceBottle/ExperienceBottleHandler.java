package AnarchySystem.Components.Commands.ExperienceBottle;

import AnarchySystem.Components.Commands.ExperienceBottle.Utils.ExperienceBottleChest;
import AnarchySystem.Manager.FakeInventory.FakeInventoryAPI;
import AnarchySystem.Manager.WorldSystem.WorldSystemAPI;
import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.inventory.InventoryTransactionEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.inventory.transaction.action.InventoryAction;
import cn.nukkit.inventory.transaction.action.SlotChangeAction;
import cn.nukkit.item.Item;

public class ExperienceBottleHandler implements Listener {
	private static final String PREFIX = "§l§7(§3Магазин§7) §r";

	private static double convert(Player player) {
		return convertLevelToExperience(player.getExperienceLevel()) + player.getExperience();
	}

	private static double convertLevelToExperience(double level) {
		double experience = 0;
		if (level <= 16) {
			experience = (level * level) + 6 * level;
		} else if (level >= 17 && level <= 31) {
			experience = 2.5 * (level * level) - 40.5 * level + 360;
		} else if (level >= 32) {
			experience = 4.5 * (level * level) - 162.5 * level + 2220;
		}
		return experience;
	}

	@EventHandler()
	public void onInventoryTransaction(InventoryTransactionEvent event) {
		Player player = event.getTransaction().getSource();
		for (InventoryAction action : event.getTransaction().getActions()) {
			if (action instanceof SlotChangeAction) {
				SlotChangeAction slotChange = (SlotChangeAction) action;
				if (slotChange.getInventory() instanceof ExperienceBottleChest) {
					event.setCancelled(true);
					Item sourceItem = action.getSourceItem();
					PlayerInventory playerInventory = player.getInventory();
					switch (sourceItem.getName()) {
					case "§r§fБутыль Опыта §7(§630 LVL§7) (§61395 EXP§7)": {
						if (convert(player) >= 1395) {
							double xp = convert(player) - 1395;
							Item bottle = Item.get(Item.EXPERIENCE_BOTTLE);
							bottle.setDamage(1395);
							bottle.setCustomName("§r§fБутыль Опыта §7(§630 LVL§7)");
							if (playerInventory.canAddItem(bottle)) {
								playerInventory.addItem(bottle);
								player.setExperience(0, 0);
								player.addExperience((int) xp);
								player.sendMessage(PREFIX + "§fВы успешно сохранили свой уровень в бутыльке§7!");
							} else {
								player.getLevel().dropItem(player.getPosition(), bottle);
								player.setExperience(0, 0);
								player.setExperience((int) xp);
								player.sendMessage(PREFIX +
												   "§fВы успешно сохранили свой уровень в бутыльке§7!\n§l§6• §r§fВаш инвентарь был §6переполнен§7, §fпо этому мы §6выбросили §fВаш купленный предмет рядом с Вами§7!");
							}
						} else {
							player.sendMessage(PREFIX + "§fВам необходимо §61395 EXP §fдля обмена§7!");
						}
					}
					break;
					case "§r§fБутыль Опыта §7(§640 LVL§7) (§62921 EXP§7)": {
						if (convert(player) >= 2921) {
							double xp = convert(player) - 2921;
							Item bottle = Item.get(Item.EXPERIENCE_BOTTLE);
							bottle.setDamage(2921);
							bottle.setCustomName("§r§fБутыль Опыта §7(§640 LVL§7)");
							if (playerInventory.canAddItem(bottle)) {
								playerInventory.addItem(bottle);
								player.setExperience(0, 0);
								player.addExperience((int) xp);
								player.sendMessage(PREFIX + "§fВы успешно сохранили свой уровень в бутыльке§7!");
							} else {
								player.getLevel().dropItem(player.getPosition(), bottle);
								player.setExperience(0, 0);
								player.setExperience((int) xp);
								player.sendMessage(PREFIX +
												   "§fВы успешно сохранили свой уровень в бутыльке§7!\n§l§6• §r§fВаш инвентарь был §6переполнен§7, §fпо этому мы §6выбросили §fВаш купленный предмет рядом с Вами§7!");
							}
						} else {
							player.sendMessage(PREFIX + "§fВам необходимо §62921 EXP §fдля обмена§7!");
						}
					}
					break;
					case "§r§fБутыль Опыта §7(§660 LVL§7) (§68672 EXP§7)": {
						if (convert(player) >= 8672) {
							double xp = convert(player) - 8672;
							Item bottle = Item.get(Item.EXPERIENCE_BOTTLE);
							bottle.setDamage(8672);
							bottle.setCustomName("§r§fБутыль Опыта §7(§660 LVL§7)");
							if (playerInventory.canAddItem(bottle)) {
								playerInventory.addItem(bottle);
								player.setExperience(0, 0);
								player.addExperience((int) xp);
								player.sendMessage(PREFIX + "§fВы успешно сохранили свой уровень в бутыльке§7!");
							} else {
								player.getLevel().dropItem(player.getPosition(), bottle);
								player.setExperience(0, 0);
								player.setExperience((int) xp);
								player.sendMessage(PREFIX +
												   "§fВы успешно сохранили свой уровень в бутыльке§7!\n§l§6• §r§fВаш инвентарь был §6переполнен§7, §fпо этому мы §6выбросили §fВаш купленный предмет рядом с Вами§7!");
							}
						} else {
							player.sendMessage(PREFIX + "§fВам необходимо §68672 EXP §fдля обмена§7!");
						}
					}
					break;
					}
				}
			}
		}
	}

	@EventHandler()
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();
		Item item = event.getItem();
		if (block != null && block.getId() == Block.BOOKSHELF && block.getLevel().equals(WorldSystemAPI.getSpawn())) {
			ExperienceBottleChest experienceChest = new ExperienceBottleChest("§r§fОбмен Опыта");
			experienceChest.setItem(11, Item.get(Item.EXPERIENCE_BOTTLE).setCustomName("§r§fБутыль Опыта §7(§630 LVL§7) (§61395 EXP§7)").setLore("\n§r§l§6• §r§fУ Вас §6" + convert(
										player) + " §fопыта§7!"));
			experienceChest.setItem(13, Item.get(Item.EXPERIENCE_BOTTLE).setCustomName("§r§fБутыль Опыта §7(§640 LVL§7) (§62921 EXP§7)").setLore("\n§r§l§6• §r§fУ Вас §6" + convert(
										player) + " §fопыта§7!"));
			experienceChest.setItem(15, Item.get(Item.EXPERIENCE_BOTTLE).setCustomName("§r§fБутыль Опыта §7(§660 LVL§7) (§68672 EXP§7)").setLore("\n§r§l§6• §r§fУ Вас §6" + convert(
										player) + " §fопыта§7!"));
			FakeInventoryAPI.openInventory(player, experienceChest);
		}
		if (item != null && item.getId() == Item.EXPERIENCE_BOTTLE && item.getDamage() > 0) {
			item.setCount(item.getCount() - 1);
			player.getInventory().setItemInHand(item);
			player.addExperience(item.getDamage());
			event.setCancelled(true);
		}
	}
}