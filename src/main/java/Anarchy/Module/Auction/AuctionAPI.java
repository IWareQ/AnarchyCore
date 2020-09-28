package Anarchy.Module.Auction;

import java.io.File;
import java.io.IOException;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import Anarchy.AnarchyMain;
import Anarchy.Manager.FakeChests.FakeChestsAPI;
import Anarchy.Module.Auction.Utils.TradeItem;
import Anarchy.Module.Auction.Utils.Inventory.AuctionChest;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.item.Item;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;

public class AuctionAPI extends PluginBase {
	public static LinkedHashMap<String, TradeItem> AUCTION = new LinkedHashMap<>();
	public static Map<CommandSender, Long> AUCTION_COOLDOWN = new HashMap<>();
	public static Map<Player, AuctionChest> AUCTION_CHEST = new HashMap<>();
	public static Map<Player, Integer> AUCTION_PAGE = new HashMap<>();
	public static double AUCTION_MAX_PRICE = 10000.0;
	public static int AUCTION_CHEST_SIZE = 45; // 45
	public static int AUCTION_MAX_SELLS = 10; // 10
	public static int AUCTION_ADD_COOLDOWN = 60; // 60
	public static String PREFIX = "§l§7(§3Аукцион§7) §r";

	public static void register() {
		new File(AnarchyMain.datapath + "/Auction/PlayerItems/").mkdirs();
		File auctionData = new File(AnarchyMain.datapath + "/Auction/Auction.yml");
		if (auctionData.exists()) {
			Config config = new Config(auctionData, Config.YAML);
			for (Map.Entry<String, Object> entry : config.getAll().entrySet()) {
				ArrayList<Object> itemData = (ArrayList<Object>)entry.getValue();
				CompoundTag compoundTag = null;
				if (itemData.size() > 7) {
					try {
						compoundTag = NBTIO.read((byte[])itemData.get(7), ByteOrder.LITTLE_ENDIAN);
					} catch (IOException e) {
						Server.getInstance().getLogger().alert("§l§fОшибка в §fregister §7- §6" + e);
					}
				}
				if (compoundTag == null) {
					compoundTag = new CompoundTag();
				}
				compoundTag.putString("UUID", entry.getKey());
				Item item = Item.get((int)itemData.get(4), (int)itemData.get(5), (int)itemData.get(6));
				item.setNamedTag(compoundTag);
				AUCTION.put(entry.getKey(), new TradeItem(item, itemData.get(0).toString(), itemData.get(1) == null ? null : itemData.get(1).toString(), (double)itemData.get(2), Long.valueOf(itemData.get(3).toString()), entry.getKey()));
			}
		}
	}

	public static void unregister() {
		File auctionData = new File(AnarchyMain.datapath + "/Auction/Auction.yml");
		if (auctionData.exists()) {
			auctionData.delete();
		}
		Config config = new Config(auctionData, Config.YAML);
		for (Map.Entry<String, TradeItem> entry : AUCTION.entrySet()) {
			try {
				TradeItem tradeItem = entry.getValue();
				Item item = tradeItem.sellItem;
				config.set(entry.getKey(), item.hasCompoundTag() ? new Object[] {tradeItem.sellerName, tradeItem.aboutMessage, tradeItem.itemPrice, tradeItem.sellTime, item.getId(), item.getDamage(), item.getCount(), NBTIO.write(item.getNamedTag(), ByteOrder.LITTLE_ENDIAN)} : new Object[] {tradeItem.sellerName, tradeItem.aboutMessage, tradeItem.itemPrice, tradeItem.sellTime, item.getId(), item.getDamage(), item.getCount()});
			} catch (IOException e) {
				Server.getInstance().getLogger().alert("§l§fОшибка в §funregister §7- §6" + e);
			}
		}
		config.save();
	}

	public static Long getTradeTime() {
		return System.currentTimeMillis() / 1000L + 259200; // 259200
	}

	public static int getPagesCount() {
		return AUCTION.size() == 0 ? 1 : (int)Math.ceil((float)AUCTION.size() / AUCTION_CHEST_SIZE);
	}

	public static void updateAuction() {
		Iterator iterator = AUCTION.entrySet().iterator();
		while (iterator.hasNext()) {
			TradeItem tradeItem = (TradeItem)((Map.Entry)iterator.next()).getValue();
			if (!tradeItem.isValid()) {
				Player player = Server.getInstance().getPlayerExact(tradeItem.sellerName);
				if (player != null) {
					player.sendMessage(PREFIX + "§fВаш товар никто не купил§7, §fпоэтому мы вернули его обртано\n§l§6| §r§fСмотрите вкладку §7(§6Хранилище§7) §fв §7/§6ah");
				}
				try {
					File dataFile = new File(AnarchyMain.datapath + "/Auction/PlayerItems/" + tradeItem.sellerName + ".yml");
					Config config = new Config(dataFile, Config.YAML);
					config.set(tradeItem.UUID, tradeItem.sellItem.hasCompoundTag() ? new Object[] {tradeItem.sellItem.getId(), tradeItem.sellItem.getDamage(), tradeItem.sellItem.getCount(), NBTIO.write(tradeItem.sellItem.getNamedTag(), ByteOrder.LITTLE_ENDIAN)} : new Object[] {tradeItem.sellItem.getId(), tradeItem.sellItem.getDamage(), tradeItem.sellItem.getCount()});
					config.save();
				} catch (IOException e) {
					Server.getInstance().getLogger().alert("§l§fОшибка в §fupdateAuction §7- §6 " + e);
				}
				iterator.remove();
			}
		}
	}

	public static void showAuction(Player player, boolean firstTime) {
		int playerPage = AUCTION_PAGE.get(player);
		AuctionChest auctionChest;
		if (firstTime) {
			auctionChest = new AuctionChest(PREFIX);
		} else {
			auctionChest = AUCTION_CHEST.get(player);
			auctionChest.clearAll();
		}
		auctionChest.setTitle("§l§fТорговая Площадка §7(§3" + AUCTION.size() + "§7)");
		int tradeSize = AUCTION.size();
		if (tradeSize == 0) {
			player.sendMessage(AuctionAPI.PREFIX + "§fАукцион пуст§7!");
			return;
		}
		int start = playerPage * AUCTION_CHEST_SIZE;
		int stop;
		if (tradeSize > start + AUCTION_CHEST_SIZE) {
			stop = start + AUCTION_CHEST_SIZE;
		} else {
			stop = tradeSize;
		}
		Object[] tradeItems = AUCTION.values().toArray();
		for (int i = start; i < stop; i++) {
			TradeItem tradeItem = (TradeItem)tradeItems[i];
			Item item = tradeItem.sellItem.clone();
			CompoundTag compoundTag = item.hasCompoundTag() ? item.getNamedTag() : new CompoundTag();
			compoundTag.putString("UUID", tradeItem.UUID);
			item.setNamedTag(compoundTag);
			item.setCustomName("§r§fПродавец §7- §6" + tradeItem.sellerName + "\n§r§fСтоимость §7- §6" + tradeItem.itemPrice + "\n§r§fДо окончания §7- §6" + (tradeItem.getTime() / 3600) + " §fч§7. §6" + (tradeItem.getTime() / 60 % 60) + " §fмин§7. §6" + (tradeItem.getTime() % 60) + " §fсек§7." + (tradeItem.aboutMessage == null ? "" : "\n§r§fОписание §7- §6" + tradeItem.aboutMessage) + "\n\n§r§6• §fНажмите§7, §fчтобы купить");
			auctionChest.addItem(item);
		}
		if (playerPage >= 0) {
			auctionChest.setItem(49, Item.get(Item.EMERALD).setCustomName("§r§6Обновление страницы").setLore("§r§6• §fНажмите§7, §fчтобы обновить страницу"));
			auctionChest.setItem(52, Item.get(Item.PAPER).setCustomName("§r§6Листнуть назад").setLore("§r§6• §fНажмите§7, §fчтобы перейти"));
			auctionChest.setItem(53, Item.get(Item.PAPER).setCustomName("§r§6Листнуть вперед").setLore("§r§6• §fНажмите§7, §fчтобы перейти"));
			auctionChest.setItem(50, Item.get(Item.SIGN).setCustomName("§r§6Справка").setLore("§r§fЭто торговая площадка§7, §fкоторая создана\n§r§fдля покупки и продажи предметов§7.\n\n§r§fТорговая площадка также является\n§r§fотличным способом заработать §6Монет§7, §fпродавая\n§r§fфермерские товары§7, §fкоторые могут\n§r§fзаинтересовать других Игроков§7.\n\n§r§fЧтобы выставить предмет на продажу§7,\n§r§fвозьмите его в руку и введите\n§r§6/auc §7(§3цена§7)\n§r§fили\n§r§6/auc §7(§3цена§7) (§3описание§7)"));
			auctionChest.setItem(46, Item.get(Item.CHEST).setCustomName("§r§6Ваши Предметы на Продаже").setLore("§r§6• §fНажмите§7, §fчтобы открыть§7!"));
			auctionChest.setItem(47, Item.get(Item.MINECART_WITH_CHEST).setCustomName("§r§6Хранилище").setLore("§r§6• §fНажмите§7, §fчтобы открыть§7!"));
		}
		if (firstTime) {
			FakeChestsAPI.openInventory(player, auctionChest);
		}
		AUCTION_CHEST.put(player, auctionChest);
	}
}