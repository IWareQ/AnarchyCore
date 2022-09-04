package ru.iwareq.anarchycore.module.Commands.Storage.Commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.Listener;
import ru.iwareq.anarchycore.module.Commands.Storage.Inventory.StorageChest;

import java.util.HashMap;

public class StorageCommand extends Command implements Listener {

	private static final HashMap<Player, StorageChest> STORAGE_CHEST = new HashMap<>();

	public StorageCommand() {
		super("storage", "§rХранилище купленных Предметов");
		this.commandParameters.clear();
	}

	public static void openStorageChest(Player player, boolean reflesh) {
		/*StorageChest storageChest = new StorageChest("Хранилище Предметов");
		if (reflesh) {
			storageChest = STORAGE_CHEST.getOrDefault(player, storageChest);
			storageChest.clearAll();
		}
		List<Integer> ids = MySQLUtils.getIntegerList("SELECT `ID` FROM `Storage` WHERE UPPER (`Name`) = '" + player.getName().toUpperCase() + "'");
		for (int id : ids) {
			Map<String, Integer> storageData = MySQLUtils.getIntegerMap("SELECT * FROM `Storage` WHERE (`ID`) = '" + id + "'");
			Item item = Item.get(storageData.get("Id"), storageData.get("Damage"), storageData.get("Count")).setNamedTag(new CompoundTag().putInt("ID", id));
			storageChest.addItem(item.setLore("\n§r§l§6• §rНажмите§7, §fчтобы забрать§7!"));
		}
		storageChest.setItem(49, Item.get(Item.DOUBLE_PLANT).setCustomName("§r§6Обновить").setLore("\n\n§l§6• §rНажмите§7, §fчтобы обновить страницу§7!"));
		FakeInventoryAPI.openDoubleChestInventory(player, storageChest);
		STORAGE_CHEST.put(player, storageChest);*/
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		/*if (sender instanceof Player) {
			Player player = (Player) sender;
			if (MySQLUtils.getInteger("SELECT `ID` FROM `Storage` WHERE UPPER (`Name`) = '" + player.getName().toUpperCase() + "'") == -1) {
				player.sendMessage("§l§6• §rСписок §6активных покупок §fпуст§7!");
				return true;
			}
			openStorageChest(player, false);
		} else {
			if (args.length >= 2) {
				String[] split = args[0].split(":");
				String targetName = Utils.implode(args, 1);
				Item item = Item.get(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
				MySQLUtils.query("INSERT INTO `Storage` (`Name`, `Id`, `Damage`, `Count`) VALUES ('" + targetName + "', '" + item.getId() + "', '" + item.getDamage() + "', '" + item.getCount() + "')");
				sender.sendMessage("§l§6• §rИгрок §6" + targetName + " §fполучил §6" + item.getName() + " §fв колличестве §6" + item.getCount() + " §fшт§7.");
			}
		}*/
		return false;
	}
}