package Anarchy.Module.Auction;

import Anarchy.AnarchyMain;
import Anarchy.Manager.FakeChests.FakeChestsAPI;
import Anarchy.Module.Auction.Utils.Inventory.AuctionChest;
import Anarchy.Module.Auction.Utils.TradeItem;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.item.Item;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;

import java.io.File;
import java.io.IOException;
import java.nio.ByteOrder;
import java.util.*;

public class AuctionAPI extends PluginBase {
	public static LinkedHashMap<String, TradeItem> AUCTION = new LinkedHashMap<>();
	public static Map<CommandSender, Long> AUCTION_COOLDOWN = new HashMap<>();
	public static Map<Player, AuctionChest> AUCTION_CHEST = new HashMap<>();
	public static Map<Player, Integer> AUCTION_PAGE = new HashMap<>();
	public static int AUCTION_MAX_PRICE = 100000;
	public static int AUCTION_CHEST_SIZE = 25;
	public static int AUCTION_MAX_SELLS = 10;
	public static int AUCTION_ADD_COOLDOWN = 30;
	public static String PREFIX = "§l§7(§cАукцион§7) §r";

	public static void register() {
		new File(AnarchyMain.datapath + "/Auction/PlayerItems/").mkdirs();
		File auctionData = new File(AnarchyMain.datapath + "/Auction/Auction.yml");
		if (auctionData.exists()) {
			Config config = new Config(auctionData, Config.YAML);
			for (Map.Entry<String, Object> entry: config.getAll().entrySet()) {
				ArrayList<Object> itemData = (ArrayList<Object> ) entry.getValue();
				CompoundTag compoundTag = null;
				if (itemData.size() > 7) {
					try {
						compoundTag = NBTIO.read((byte[]) itemData.get(7), ByteOrder.LITTLE_ENDIAN);
					} catch (IOException e) { /**/ }
				}
				if (compoundTag == null) {
					compoundTag = new CompoundTag();
				}
				compoundTag.putString("UUID", entry.getKey());
				Item item = Item.get((int) itemData.get(4), (int) itemData.get(5), (int) itemData.get(6));
				item.setNamedTag(compoundTag);
				AUCTION.put(entry.getKey(), new TradeItem(item, itemData.get(0).toString(), itemData.get(1) == null ? null : itemData.get(1).toString(), (int) itemData.get(2), Long.valueOf(itemData.get(3).toString()), entry.getKey()));
			}
		}
	}

	public static void unregister() {
		File auctionData = new File(AnarchyMain.datapath + "/Auction/Auction.yml");
		if (auctionData.exists()) {
			auctionData.delete();
		}
		Config config = new Config(auctionData, Config.YAML);
		for (Map.Entry<String, TradeItem> entry: AUCTION.entrySet()) {
			try {
				TradeItem tradeItem = entry.getValue();
				Item item = tradeItem.sellItem;
				config.set(entry.getKey(), item.hasCompoundTag() ? new Object[] {
					tradeItem.sellerName, tradeItem.aboutMessage, tradeItem.itemPrice, tradeItem.sellTime, item.getId(), item.getDamage(), item.getCount(), NBTIO.write(item.getNamedTag(), ByteOrder.LITTLE_ENDIAN)
				} : new Object[] {
					tradeItem.sellerName, tradeItem.aboutMessage, tradeItem.itemPrice, tradeItem.sellTime, item.getId(), item.getDamage(), item.getCount()
				});
			} catch (IOException e) { /**/ }
		}
		config.save();
	}

	public static Long getTradeTime() {
		return System.currentTimeMillis() / 1000L + 259200;
	}

	public static int getPagesCount() {
		return AUCTION.size() == 0 ? 1 : (int) Math.ceil((float) AUCTION.size() / AUCTION_CHEST_SIZE);
	}

	public static void updateAuction() {
		Iterator iterator = AUCTION.entrySet().iterator();
		while (iterator.hasNext()) {
			TradeItem tradeItem = (TradeItem)((Map.Entry) iterator.next()).getValue();
			if (!tradeItem.isValid()) {
				Player player = Server.getInstance().getPlayerExact(tradeItem.sellerName);
				if (player != null) {
					player.sendMessage(PREFIX + "§fВаш товар никто не купил, поэтому мы вернули его обртано.\n§l§e| §r§fСмотрите вкладку §l§7(§fХранилище§7) §fв §e/auc");
				}
				try {
					File dataFile = new File(AnarchyMain.datapath + "/Auction/PlayerItems/" + tradeItem.sellerName + ".yml");
					Config config = new Config(dataFile, Config.YAML);
					config.set(tradeItem.UUID, tradeItem.sellItem.hasCompoundTag() ? new Object[] {
						tradeItem.sellItem.getId(), tradeItem.sellItem.getDamage(), tradeItem.sellItem.getCount(), NBTIO.write(tradeItem.sellItem.getNamedTag(), ByteOrder.LITTLE_ENDIAN)
					} : new Object[] {
						tradeItem.sellItem.getId(), tradeItem.sellItem.getDamage(), tradeItem.sellItem.getCount()
					});
					config.save();
				} catch (IOException e) { /**/ }
				iterator.remove();
			}
		}
	}

	public static void showAuction(Player player, boolean firstTime) {
		int playerPage = AUCTION_PAGE.get(player);
		AuctionChest auctionChest;
		if (firstTime) {
			auctionChest = new AuctionChest();
		} else {
			auctionChest = AUCTION_CHEST.get(player);
			auctionChest.clearAll();
		}

		auctionChest.setTitle("§fАукцион, всего предметов §l" + AUCTION.size());
		int tradeSize = AUCTION.size();
		if (tradeSize == 0) {
			player.sendMessage(AuctionAPI.PREFIX + "§fНа аукционе нет предметов!");
			return;
		}

		int start = playerPage * AUCTION_CHEST_SIZE, stop;
		if (tradeSize > start + AUCTION_CHEST_SIZE) {
			stop = start + AUCTION_CHEST_SIZE;
			auctionChest.setItem(26, Item.get(Item.PAPER).setCustomName("§6Вперед"));
		} else {
			stop = tradeSize;
		}

		Object[] tradeItems = AUCTION.values().toArray();
		for (int i = start; i<stop; i++) {
			TradeItem tradeItem = (TradeItem) tradeItems[i];
			Item item = tradeItem.sellItem.clone();
			CompoundTag compoundTag = item.hasCompoundTag() ? item.getNamedTag() : new CompoundTag();
			compoundTag.putString("UUID", tradeItem.UUID);
			item.setNamedTag(compoundTag);
			item.setCustomName("§r§fПродавец: §e" + tradeItem.sellerName + "\n§fЦена: §e" + tradeItem.itemPrice + "\n§fИстекает через: §c" + (tradeItem.getTime() / 3600) + " §fч. §c" + (tradeItem.getTime() / 60 % 60) + " §fмин." + (tradeItem.aboutMessage == null ? "" : "\n§fОписание: §f" + tradeItem.aboutMessage));
			auctionChest.addItem(item);
		}

		if (playerPage != 0) {
			auctionChest.setItem(25, Item.get(Item.PAPER).setCustomName("§6Назад"));
		}

		if (firstTime) {
			FakeChestsAPI.openInventory(player, auctionChest);
		}
		AUCTION_CHEST.put(player, auctionChest);
	}
}