package ru.jl1mbo.AnarchyCore.CommandsHandler.Shop;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.inventory.InventoryTransactionEvent;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.inventory.transaction.action.InventoryAction;
import cn.nukkit.inventory.transaction.action.SlotChangeAction;
import cn.nukkit.item.Item;
import ru.jl1mbo.AnarchyCore.CommandsHandler.Shop.Utils.Chest;
import ru.jl1mbo.AnarchyCore.Manager.FakeInventory.FakeInventoryAPI;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.EconomyAPI.EconomyAPI;

public class ShopHandler extends Command implements Listener {
	private static final String PREFIX = "§l§7(§3Магазин§7) §r";

	public ShopHandler() {
		super("dshop", "§r§fМагазин");
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			Chest donateShopChest = new Chest("§r§fМагазин");
			donateShopChest.addItem(Item.get(Item.TOTEM, 0, 1).setLore("\n§r§fЦена§7: §6700\n\n§r§fНажмите§7, §fчтобы купить§7!"));
			donateShopChest.addItem(Item.get(Item.ENDER_PEARL, 0, 1).setLore("\n§r§fЦена§7: §6100\n\n§r§fНажмите§7, §fчтобы купить§7!"));
			donateShopChest.addItem(Item.get(Item.CHORUS_FRUIT, 0, 1).setLore("\n§r§fЦена§7: §625\n\n§r§fНажмите§7, §fчтобы купить§7!"));
			donateShopChest.addItem(Item.get(Item.ELYTRA, 0, 1).setLore("\n§r§fЦена§7: §62000\n\n§r§fНажмите§7, §fчтобы купить§7!"));
			donateShopChest.addItem(Item.get(Item.DRAGON_BREATH, 0, 1).setLore("\n§r§fЦена§7: §6750\n\n§r§fНажмите§7, §fчтобы купить§7!"));
			donateShopChest.addItem(Item.get(Item.SHULKER_BOX, 0, 1).setLore("\n§r§fЦена§7: §61000\n\n§r§fНажмите§7, §fчтобы купить§7!"));
			FakeInventoryAPI.openInventory(player, donateShopChest);
		}
		return false;
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onInventoryTransaction(InventoryTransactionEvent event) {
		Player player = event.getTransaction().getSource();
		for (InventoryAction action : event.getTransaction().getActions()) {
			if (action instanceof SlotChangeAction) {
				SlotChangeAction slotChange = (SlotChangeAction) action;
				if (slotChange.getInventory() instanceof Chest) {
					event.setCancelled(true);
					Item sourceItem = action.getSourceItem();
					PlayerInventory playerInventory = player.getInventory();
					switch (sourceItem.getId()) {
					case Item.TOTEM: {
						if (EconomyAPI.myMoney(player.getName()) >= 700) {
							if (playerInventory.canAddItem(sourceItem)) {
								playerInventory.addItem(Item.get(Item.TOTEM, 0, 1));
								player.sendMessage(PREFIX + "§6Тотем §fбыл успешно куплен§7!");
								EconomyAPI.reduceMoney(player.getName(), 700.0);
							}
						} else {
							player.sendMessage(PREFIX + "§fНедостаточно монет для совершения покупки§7!\n§fБаланс§7: §6" + EconomyAPI.myMoney(player.getName()));
						}
					}
					break;
					case Item.ENDER_PEARL: {
						if (EconomyAPI.myMoney(player.getName()) >= 100) {
							if (playerInventory.canAddItem(sourceItem)) {
								playerInventory.addItem(Item.get(Item.ENDER_PEARL, 0, 1));
								player.sendMessage(PREFIX + "§6Жемчуг края §fбыл успешно куплен§7!");
								EconomyAPI.reduceMoney(player.getName(), 100.0);
							}
						} else {
							player.sendMessage(PREFIX + "§fНедостаточно монет для совершения покупки§7!\n§fБаланс§7: §6" + EconomyAPI.myMoney(player.getName()));
						}
					}
					break;
					case Item.CHORUS_FRUIT: {
						if (EconomyAPI.myMoney(player.getName()) >= 25) {
							if (playerInventory.canAddItem(sourceItem)) {
								playerInventory.addItem(Item.get(Item.CHORUS_FRUIT, 0, 1));
								player.sendMessage(PREFIX + "§6Плод коруса §fбыл успешно куплен§7!");
								EconomyAPI.reduceMoney(player.getName(), 25.0);
							}
						} else {
							player.sendMessage(PREFIX + "§fНедостаточно монет для совершения покупки§7!\n§fБаланс§7: §6" + EconomyAPI.myMoney(player.getName()));
						}
					}
					break;
					case Item.ELYTRA: {
						if (EconomyAPI.myMoney(player.getName()) >= 2000) {
							if (playerInventory.canAddItem(sourceItem)) {
								playerInventory.addItem(Item.get(Item.ELYTRA, 0, 1));
								player.sendMessage(PREFIX + "§6Элитры §fбыл успешно куплен§7!");
								EconomyAPI.reduceMoney(player.getName(), 2000.0);
							}
						} else {
							player.sendMessage(PREFIX + "§fНедостаточно монет для совершения покупки§7!\n§fБаланс§7: §6" + EconomyAPI.myMoney(player.getName()));
						}
					}
					break;
					case Item.DRAGON_BREATH: {
						if (EconomyAPI.myMoney(player.getName()) >= 750) {
							if (playerInventory.canAddItem(sourceItem)) {
								playerInventory.addItem(Item.get(Item.DRAGON_BREATH, 0, 1));
								player.sendMessage(PREFIX + "§6Драконье дыхание §fбыл успешно куплен§7!");
								EconomyAPI.reduceMoney(player.getName(), 750.0);
							}
						} else {
							player.sendMessage(PREFIX + "§fНедостаточно монет для совершения покупки§7!\n§fБаланс§7: §6" + EconomyAPI.myMoney(player.getName()));
						}
					}
					break;
					case Item.SHULKER_BOX: {
						if (EconomyAPI.myMoney(player.getName()) >= 1000) {
							if (playerInventory.canAddItem(sourceItem)) {
								playerInventory.addItem(Item.get(Item.SHULKER_BOX, 0, 1));
								player.sendMessage(PREFIX + "§6Белый шалкер бокс §fбыл успешно куплен§7!");
								EconomyAPI.reduceMoney(player.getName(), 1000.0);
							}
						} else {
							player.sendMessage(PREFIX + "§fНедостаточно монет для совершения покупки§7!\n§fБаланс§7: §6" + EconomyAPI.myMoney(player.getName()));
						}
					}
					break;
					}
				}
			}
		}
	}
}