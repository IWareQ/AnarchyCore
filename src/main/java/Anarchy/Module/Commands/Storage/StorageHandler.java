package Anarchy.Module.Commands.Storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import Anarchy.AnarchyMain;
import Anarchy.Module.Commands.Storage.Utils.StorageDoubleChest;
import Anarchy.Utils.StringUtils;
import FakeInventoryAPI.FakeInventoryAPI;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
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

public class StorageHandler extends Command implements Listener {
	public static Map<Player, StorageDoubleChest> STORAGE_DOUBLE_CHEST = new HashMap<>();

	public StorageHandler() {
		super("storage", "§r§fХранилище купленных Предметов");
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player)sender;
			if (args.length == 0) {
				showStorage(player, true);
			}
		} else if (args.length >= 2 && !(sender instanceof Player)) {
			String[] split = args[0].split(":");
			String playerName = StringUtils.implode(args, 1).toLowerCase();
			Item item = Item.get(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
			sender.sendMessage(AnarchyMain.PREFIX + "§fИгрок §6" + playerName + " §fполучил §6" + item.getName() + " §fв колличестве §6" + split[2] + " §fшт§7!");
			Config config = getStorageConfig(playerName);
			config.set(UUID.randomUUID().toString(), new Object[] {Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2])});
			config.save();
			config.reload();
		}
		return false;
	}

	@EventHandler()
	public void onInventoryTransaction(InventoryTransactionEvent event) {
		Player player = event.getTransaction().getSource();
		for (InventoryAction action : event.getTransaction().getActions()) {
			if (action instanceof SlotChangeAction) {
				SlotChangeAction slotChange = (SlotChangeAction)action;
				if (slotChange.getInventory() instanceof StorageDoubleChest) {
					event.setCancelled();
					Item sourceItem = action.getSourceItem();
					StorageDoubleChest storageChest = (StorageDoubleChest)slotChange.getInventory();
					switch (sourceItem.getName()) {
					case "§r§6Обновление Хранилища": {
						showStorage(player, false);
						showStorage(player, true);
					}
					break;

					default: {
						CompoundTag compoundTag = sourceItem.getNamedTag();
						Config config = getStorageConfig(player.getName());
						if (compoundTag != null && compoundTag.getString("UUID") != null) {
							PlayerInventory playerInventory = player.getInventory();
							if (playerInventory.canAddItem(sourceItem)) {
								storageChest.removeItem(sourceItem);
								config.remove(compoundTag.getString("UUID"));
								config.save();
								config.reload();
								compoundTag.remove("UUID");
								playerInventory.addItem(sourceItem.clearCustomName().clearCustomBlockData().setNamedTag(compoundTag).setLore());
								player.getLevel().addSound(player, Sound.RANDOM_ORB, 1, 1, player);
								player.sendMessage("§l§7(§3Хранилище§7) §r§fПредмет с §6Хранилища §fуспешно взят§7!");
							}
						}
					}
					break;
					}
				}
			}
		}
	}

	private static Config getStorageConfig(String playerName) {
		return new Config(AnarchyMain.folder + "/StorageItems/" + playerName + ".yml", Config.YAML);
	}

	private static void showStorage(Player player, boolean firstTime) {
		Config config = getStorageConfig(player.getName());
		StorageDoubleChest storageChest;
		if (firstTime) {
			storageChest = new StorageDoubleChest("§r§fХранилище");
		} else {
			storageChest = STORAGE_DOUBLE_CHEST.get(player);
			storageChest.clearAll();
		}
		if (config.getAll().size() == 0) {
			player.sendMessage("§l§7(§3Хранилище§7) §r§fВ Вашем§6 Хранилище §fпусто§7!");
			return;
		}
		for (Map.Entry<String, Object> entry : config.getAll().entrySet()) {
			ArrayList<Object> itemData = (ArrayList<Object>)entry.getValue();
			Item item = Item.get((int)itemData.get(0), (int)itemData.get(1), (int)itemData.get(2));
			item.setNamedTag(new CompoundTag().putString("UUID", entry.getKey()));
			storageChest.addItem(item.setLore("\n§r§l§6• §r§fНажмите§7, §fчтобы забрать предмет с §6Хранилище§7!"));
			storageChest.setItem(49, Item.get(Item.DOUBLE_PLANT).setCustomName("§r§6Обновление Хранилища").setLore("\n§r§fПредметов§7: §6" + config.getAll().size() + "\n\n§r§l§6• §r§fНажмите§7, §fчтобы обновить §6Хранилище Купленых Предметов§7!"));
		}
		if (firstTime) {
			FakeInventoryAPI.openDoubleChestInventory(player, storageChest);
		}
		STORAGE_DOUBLE_CHEST.put(player, storageChest);
	}
}