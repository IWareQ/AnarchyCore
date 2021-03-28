package ru.jl1mbo.AnarchyCore.Modules.Commands.Storage.Commands;

import java.util.ArrayList;
import java.util.HashMap;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.Listener;
import cn.nukkit.item.Item;
import cn.nukkit.nbt.tag.CompoundTag;
import ru.jl1mbo.AnarchyCore.Manager.FakeInventory.FakeInventoryAPI;
import ru.jl1mbo.AnarchyCore.Modules.Commands.Storage.Inventory.StorageChest;
import ru.jl1mbo.AnarchyCore.Utils.SQLiteUtils;
import ru.jl1mbo.AnarchyCore.Utils.Utils;

public class StorageCommand extends Command implements Listener {
	private static HashMap<Player, StorageChest> storageChest = new HashMap<>();

	public StorageCommand() {
		super("storage", "§rХранилище купленных Предметов");
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player)sender;
			if (SQLiteUtils.getInteger("Storage.db", "SELECT `ID` FROM `Items` WHERE UPPER (`Name`) = '" + player.getName().toUpperCase() + "'") == -1) {
				player.sendMessage("§l§6• §rСписок §6активных покупок §fпуст§7!");
				return true;
			}
			openStorageChest(player, false);
		} else {
			if (args.length >= 2) {
				String[] split = args[0].split(":");
				String targetName = Utils.implode(args, 1);
				Item item = Item.get(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
				SQLiteUtils.query("Storage.db", "INSERT INTO `Items` (`Name`, `ItemId`, `ItemDamage`, `ItemCount`) VALUES ('" + targetName + "', '" + item.getId() + "', '" + item.getDamage() + "', '" +
								  item.getCount() + "')");
				sender.sendMessage("§l§6• §rИгрок §6" + targetName + " §fполучил §6" + item.getName() + " §fв колличестве §6" + item.getCount() + " §fшт§7.");
			}
		}
		return false;
	}

	public static void openStorageChest(Player player, boolean reflesh) {
		StorageChest doubleChest = new StorageChest("Хранилище Предметов");
		if (reflesh) {
			doubleChest = storageChest.getOrDefault(player, new StorageChest("Хранилище Предметов"));
			doubleChest.clearAll();
		}
		ArrayList<Integer> itemsId = SQLiteUtils.getIntegerList("Storage.db", "SELECT `ID` FROM `Items` WHERE UPPER (`Name`) = '" + player.getName().toUpperCase() + "'");
		for (int itemId : itemsId) {
			HashMap<String, Integer> storageData =  SQLiteUtils.getIntegerHashMap("Storage.db", "SELECT * FROM `Items` WHERE (`ID`) = '" + itemId + "'");
			Item item = Item.get(storageData.get("ItemId"), storageData.get("ItemDamage"), storageData.get("ItemCount")).setNamedTag(new CompoundTag().putInt("UUID", storageData.get("ID")));
			doubleChest.addItem(item.setLore("\n§r§l§6• §rНажмите§7, §fчтобы забрать§7!"));
		}
		doubleChest.setItem(49, Item.get(Item.DOUBLE_PLANT).setCustomName("§r§6Обновить").setLore("\n§rПредметов§7: §6" + SQLiteUtils.getIntegerList("Storage.db",
							"SELECT `ID` FROM `Items` WHERE UPPER (`Name`) = '" + player.getName().toUpperCase() + "'").size() +  "\n\n§l§6• §rНажмите§7, §fчтобы обновить страницу§7!"));
		FakeInventoryAPI.openDoubleChestInventory(player, doubleChest);
		storageChest.put(player, doubleChest);
	}
}