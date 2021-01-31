package ru.jl1mbo.AnarchyCore.GameHandler.Auction;

import java.io.IOException;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import ru.jl1mbo.AnarchyCore.GameHandler.Auction.Utils.Inventory.StorageAuctionDoubleChest;
import ru.jl1mbo.AnarchyCore.Manager.FakeInventory.FakeInventoryAPI;
import ru.jl1mbo.AnarchyCore.Utils.ConfigUtils;

public class StorageAuction extends PluginBase {
    public static Map<Player, StorageAuctionDoubleChest> STORAGE_AUCTION_DOUBLE_CHEST = new HashMap<>();

    public static void showStorageAuction(Player player, boolean firstTime) {
        Config config = ConfigUtils.getAuctionStorageConfig(player.getName());
        StorageAuctionDoubleChest storageAuctionChest;
        if (firstTime) {
            storageAuctionChest = new StorageAuctionDoubleChest("§r§fХранилище Аукциона");
        } else {
            storageAuctionChest = STORAGE_AUCTION_DOUBLE_CHEST.get(player);
            storageAuctionChest.clearAll();
        }
        if (config.getAll().size() == 0) {
            player.sendMessage(AuctionAPI.PREFIX + "§fВ Вашем Хранилище Аукциона пусто§7!");
            return;
        }
        for (Map.Entry<String, Object> entry : config.getAll().entrySet()) {
            ArrayList<Object> itemData = (ArrayList<Object>) entry.getValue();
            CompoundTag nbt = null;
            if (itemData.size() > 6) {
                try {
                    nbt = NBTIO.read((byte[]) itemData.get(6), ByteOrder.LITTLE_ENDIAN);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (nbt == null) {
                nbt = new CompoundTag();
            }
            nbt.putString("UUID", entry.getKey());
            Item item = Item.get((int) itemData.get(0), (int) itemData.get(1), (int) itemData.get(2));
            item.setNamedTag(nbt);
            storageAuctionChest.addItem(item.setLore("\n§r§l§6• §r§fНажмите§7, §fчтобы забрать предмет с §6Хранилища§7!"));
            storageAuctionChest.setItem(49, Item.get(Item.DOUBLE_PLANT).setCustomName("§r§6Обновление Хранилища").setLore("\n§r§fПредметов§7: §6" + config.getAll().size() + "\n\n§r§l§6• §r§fНажмите§7, §fчтобы обновить §6Хранилище§7!"));
        }
        if (firstTime) {
            FakeInventoryAPI.openDoubleChestInventory(player, storageAuctionChest);
        }
        STORAGE_AUCTION_DOUBLE_CHEST.put(player, storageAuctionChest);
    }
}