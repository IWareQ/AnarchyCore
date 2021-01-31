package ru.jl1mbo.AnarchyCore.CommandsHandler.StorageItems;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.item.Item;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.plugin.PluginManager;
import cn.nukkit.utils.Config;
import ru.jl1mbo.AnarchyCore.CommandsHandler.StorageItems.Commands.StorageCommand;
import ru.jl1mbo.AnarchyCore.CommandsHandler.StorageItems.EventsListener.InventoryTransactionListener;
import ru.jl1mbo.AnarchyCore.CommandsHandler.StorageItems.EventsListener.PlayerJoinListener;
import ru.jl1mbo.AnarchyCore.CommandsHandler.StorageItems.Inventory.DoubleChest;
import ru.jl1mbo.AnarchyCore.Main;
import ru.jl1mbo.AnarchyCore.Manager.FakeInventory.FakeInventoryAPI;
import ru.jl1mbo.AnarchyCore.Utils.ConfigUtils;

import java.util.HashMap;
import java.util.Map;

public class StorageItemsAPI {
    private static final Map<Player, DoubleChest> STORAGE_CHEST = new HashMap<>();

    public static void registers() {
        PluginManager pluginManager = Server.getInstance().getPluginManager();
        pluginManager.registerEvents(new PlayerJoinListener(), Main.getInstance());
        pluginManager.registerEvents(new InventoryTransactionListener(), Main.getInstance());
        Server.getInstance().getCommandMap().register("", new StorageCommand());
    }

    public static void showStorageItemsChest(Player player, boolean value) {
        Config config = ConfigUtils.getStorageItemsConfig(player.getName());
        DoubleChest doubleChest;
        if (value) {
            doubleChest = new DoubleChest("§r§fХранилище Предметов");
        } else {
            doubleChest = STORAGE_CHEST.get(player);
            doubleChest.clearAll();
        }
        if (config.getAll().size() == 0) {
            player.sendMessage("§l§6• §r§fВ Вашем §6Хранилище Предметов §fпусто§7!");
            return;
        }
        for (Map.Entry<String, Object> entry : config.getAll().entrySet()) {
            Item item = Item.get(config.getInt(entry.getKey() + ".ID"), config.getInt(entry.getKey() + ".DATA"), config.getInt(entry.getKey() + ".COUNT"));
            item.setNamedTag(new CompoundTag().putString("UUID", entry.getKey()));
            doubleChest.addItem(item.setLore("\n§r§l§6• §r§fНажмите§7, §fчтобы забрать предмет с §6Хранилище Предметов§7!"));
            doubleChest.setItem(49, Item.get(Item.DOUBLE_PLANT).setCustomName("§r§6Обновление Хранилища").setLore("\n§r§fПредметов§7: §6" + config.getAll().size() +
                    "\n\n§r§l§6• §r§fНажмите§7, §fчтобы обновить §6Хранилище Предметов§7!"));
        }
        if (value) {
            FakeInventoryAPI.openDoubleChestInventory(player, doubleChest);
        }
        STORAGE_CHEST.put(player, doubleChest);
    }

    public static void addItemsToStorageItems(String playerName, Item item) {
        Config config = ConfigUtils.getStorageItemsConfig(playerName);
        String UUID = java.util.UUID.randomUUID().toString();
        config.set(UUID + ".ID", item.getId());
        config.set(UUID + ".DATA", item.getDamage());
        config.set(UUID + ".COUNT", item.getCount());
        config.save();
        config.reload();
    }

    public static void removeItemsToStorageItems(String playerName, String UUID) {
        Config config = ConfigUtils.getStorageItemsConfig(playerName);
        config.remove(UUID);
        config.save();
        config.reload();
    }
}