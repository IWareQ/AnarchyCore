package Anarchy.Module.Auction;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Anarchy.AnarchyMain;
import Anarchy.Module.Auction.Utils.Inventory.StorageAuctionDoubleChest;
import FakeInventoryAPI.FakeInventoryAPI;
import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;

public class StorageAuction extends PluginBase {
	public static Map<Player, StorageAuctionDoubleChest> STORAGE_DOUBLE_CHEST = new HashMap<>();

	public static Config getStorageConfig(Player player) {
		File storageFile = new File(AnarchyMain.folder + "/Auction/PlayerItems/" + player.getName() + ".yml");
		Config config = new Config(storageFile, Config.YAML);
		return config;
	}

	public static void showStorageAuction(Player player, boolean firstTime) {
		StorageAuctionDoubleChest storageChest;
		if (firstTime) {
			storageChest = new StorageAuctionDoubleChest("§r§fХранилище Аукциона");
		} else {
			storageChest = STORAGE_DOUBLE_CHEST.get(player);
			storageChest.clearAll();
		}
		if (getStorageConfig(player).getAll().size() == 0) {
			player.sendMessage(AuctionAPI.PREFIX + "§fВ Вашем Хранилище Аукциона пусто§7!");
			return;
		}
		for (Map.Entry<String, Object> entry : getStorageConfig(player).getAll().entrySet()) {
			ArrayList<Object> itemData = (ArrayList<Object>)entry.getValue();
			Item item = Item.get((int)itemData.get(0), (int)itemData.get(1), (int)itemData.get(2));
			CompoundTag nbt = new CompoundTag();
			nbt.putString("UUID", entry.getKey());
			item.setNamedTag(nbt);
			storageChest.addItem(item.setLore("\n§r§l§6• §r§fНажмите§7, §fчтобы забрать предмет с §6Хранилища§7!"));
			storageChest.setItem(49, Item.get(Item.DOUBLE_PLANT).setCustomName("§r§6Обновление Хранилища").setLore("\n§r§fПредметов§7: §6" + getStorageConfig(player).getAll().size() + "\n\n§r§l§6• §r§fНажмите§7, §fчтобы обновить §6Хранилище§7!"));
		}
		if (firstTime) {
			FakeInventoryAPI.openDoubleChestInventory(player, storageChest);
		}
		STORAGE_DOUBLE_CHEST.put(player, storageChest);
	}
}