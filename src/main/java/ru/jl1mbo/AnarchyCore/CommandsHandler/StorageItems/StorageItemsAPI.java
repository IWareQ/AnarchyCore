package ru.jl1mbo.AnarchyCore.CommandsHandler.StorageItems;

import java.util.HashMap;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.item.Item;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.plugin.PluginManager;
import cn.nukkit.utils.Config;
import ru.jl1mbo.AnarchyCore.Main;
import ru.jl1mbo.AnarchyCore.CommandsHandler.StorageItems.Commands.StorageCommand;
import ru.jl1mbo.AnarchyCore.CommandsHandler.StorageItems.EventsListener.InventoryTransactionListener;
import ru.jl1mbo.AnarchyCore.CommandsHandler.StorageItems.EventsListener.PlayerJoinListener;
import ru.jl1mbo.AnarchyCore.CommandsHandler.StorageItems.Inventory.DoubleChest;
import ru.jl1mbo.AnarchyCore.Manager.FakeInventory.FakeInventoryAPI;

public class StorageItemsAPI {
	public static Config config;
	private static final Map<Player, DoubleChest> STORAGE_CHEST = new HashMap<>();

	public static void register() {
		PluginManager pluginManager = Server.getInstance().getPluginManager();
		pluginManager.registerEvents(new PlayerJoinListener(), Main.getInstance());
		pluginManager.registerEvents(new InventoryTransactionListener(), Main.getInstance());
		Server.getInstance().getCommandMap().register("", new StorageCommand());
	}

	public static void showStorageItemsChest(Player player, boolean value) {
		Config config = new Config(Main.getInstance().getDataFolder() + "/StorageItems/" + player.getName().toLowerCase() + ".yml", Config.YAML);
		DoubleChest doubleChest;
		if (value) {
			doubleChest = new DoubleChest("Хранилище Предметов");
		} else {
			doubleChest = STORAGE_CHEST.get(player);
			doubleChest.clearAll();
		}
		if (config.getAll().size() == 0) {
			player.sendMessage("§l§6• §rСписок §6активных покупок §fпуст§7!");
			return;
		}
		for (Map.Entry<String, Object> entry : config.getAll().entrySet()) {
			Item item = Item.get(config.getInt(entry.getKey() + ".id"), config.getInt(entry.getKey() + ".data"), config.getInt(entry.getKey() + ".count"));
			item.setNamedTag(new CompoundTag().putString("UUID", entry.getKey()));
			doubleChest.addItem(item.setLore("\n§r§l§6• §rНажмите§7, §fчтобы забрать предмет с §6Хранилища§7."));
			doubleChest.setItem(49, Item.get(Item.DOUBLE_PLANT).setCustomName("§r§6Обновление Хранилища").setLore("\n§rПредметов§7: §6" + config.getAll().size() +
								"\n\n§l§6• §rНажмите§7, §fчтобы обновить §6Хранилище§7."));
		}
		if (value) {
			FakeInventoryAPI.openDoubleChestInventory(player, doubleChest);
		}
		STORAGE_CHEST.put(player, doubleChest);
	}

	public static void addItem(String playerName, Item item) {
		Config config = new Config(Main.getInstance().getDataFolder() + "/StorageItems/" + playerName.toLowerCase() + ".yml", Config.YAML);
		String UUID = java.util.UUID.randomUUID().toString();
		config.set(playerName.toLowerCase() + ".Items" + UUID + ".id", item.getId());
		config.set(playerName.toLowerCase() + ".Items" + UUID + ".data", item.getDamage());
		config.set(playerName.toLowerCase() + ".Items" + UUID + ".count", item.getCount());
		config.save();
		config.reload();
	}

	public static void removeItem(Player player, String UUID) {
		Config config = new Config(Main.getInstance().getDataFolder() + "/StorageItems/" + player.getName().toLowerCase() + ".yml", Config.YAML);
		config.remove(player.getName().toLowerCase() + ".Items" + UUID);
		config.save();
		config.reload();
	}
}