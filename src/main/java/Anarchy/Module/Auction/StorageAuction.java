package Anarchy.Module.Auction;

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
	public static Map<Player, StorageAuctionDoubleChest> STORAGE_AUCTION_DOUBLE_CHEST = new HashMap<>();

	public static Config getStorageAuctionConfig(String playerName) {
		return new Config(AnarchyMain.folder + "/Auction/PlayerItems/" + playerName + ".yml", Config.YAML);
	}

	public static void showStorageAuction(Player player, boolean firstTime) {
		StorageAuctionDoubleChest storageAuctionChest;
		if (firstTime) {
			storageAuctionChest = new StorageAuctionDoubleChest("§r§fХранилище Аукциона");
		} else {
			storageAuctionChest = STORAGE_AUCTION_DOUBLE_CHEST.get(player);
			storageAuctionChest.clearAll();
		}
		if (getStorageAuctionConfig(player.getName()).getAll().size() == 0) {
			player.sendMessage(AuctionAPI.PREFIX + "§fВ Вашем Хранилище Аукциона пусто§7!");
			return;
		}
		for (Map.Entry<String, Object> entry : getStorageAuctionConfig(player.getName()).getAll().entrySet()) {
			ArrayList<Object> itemData = (ArrayList<Object>)entry.getValue();
			Item item = Item.get((int)itemData.get(0), (int)itemData.get(1), (int)itemData.get(2));
			CompoundTag nbt = new CompoundTag();
			nbt.putString("UUID", entry.getKey());
			item.setNamedTag(nbt);
			storageAuctionChest.addItem(item.setLore("\n§r§l§6• §r§fНажмите§7, §fчтобы забрать предмет с §6Хранилища§7!"));
			storageAuctionChest.setItem(49, Item.get(Item.DOUBLE_PLANT).setCustomName("§r§6Обновление Хранилища").setLore("\n§r§fПредметов§7: §6" + getStorageAuctionConfig(player.getName()).getAll().size() + "\n\n§r§l§6• §r§fНажмите§7, §fчтобы обновить §6Хранилище§7!"));
		}
		if (firstTime) {
			FakeInventoryAPI.openDoubleChestInventory(player, storageAuctionChest);
		}
		STORAGE_AUCTION_DOUBLE_CHEST.put(player, storageAuctionChest);
	}
}