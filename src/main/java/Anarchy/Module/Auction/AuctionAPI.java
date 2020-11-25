package Anarchy.Module.Auction;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import Anarchy.AnarchyMain;
import Anarchy.Module.Auction.Utils.TradeItem;
import Anarchy.Module.Auction.Utils.Inventory.AuctionChest;
import FakeInventoryAPI.FakeInventoryAPI;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.item.Item;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;

public class AuctionAPI extends PluginBase {
	public static LinkedHashMap<String, TradeItem> AUCTION = new LinkedHashMap<>();
	public static Map<CommandSender, Long> AUCTION_COOLDOWN = new HashMap<>();
	public static Map<Player, AuctionChest> AUCTION_CHEST = new HashMap<>();
	public static Map<Player, Integer> AUCTION_PAGE = new HashMap<>();
	public static double AUCTION_MAX_PRICE = 10000.0;
	public static int AUCTION_CHEST_SIZE = 36;
	public static int AUCTION_MAX_SELLS = 5;
	public static int AUCTION_ADD_COOLDOWN = 60;
	public static String PREFIX = "§l§7(§3Аукцион§7) §r";

	public static void register() {
		new File(AnarchyMain.folder + "/Auction/PlayerItems/").mkdirs();
		File auctionData = new File(AnarchyMain.folder + "/Auction/Auction.yml");
		if (auctionData.exists()) {
			Config config = new Config(auctionData, Config.YAML);
			for (Map.Entry<String, Object> entry : config.getAll().entrySet()) {
				ArrayList<Object> itemData = (ArrayList<Object>)entry.getValue();
				CompoundTag compoundTag = null;
				if (compoundTag == null) {
					compoundTag = new CompoundTag();
				}
				compoundTag.putString("UUID", entry.getKey());
				Item item = Item.get((int)itemData.get(3), (int)itemData.get(4), (int)itemData.get(5));
				item.setNamedTag(compoundTag);
				AUCTION.put(entry.getKey(), new TradeItem(itemData.get(0).toString(), Double.parseDouble(itemData.get(1).toString()), Long.parseLong(itemData.get(2).toString()), item, entry.getKey()));
			}
		}
	}

	public static void saveAuction() {
		File auctionData = new File(AnarchyMain.folder + "/Auction/Auction.yml");
		if (auctionData.exists()) {
			auctionData.delete();
		}
		Config config = new Config(auctionData, Config.YAML);
		for (Map.Entry<String, TradeItem> entry : AUCTION.entrySet()) {
			TradeItem tradeItem = entry.getValue();
			Item item = tradeItem.getSellItem();
			config.set(entry.getKey(), new Object[] {tradeItem.getSellerName(), tradeItem.getItemPrice(), tradeItem.getTime(), item.getId(), item.getDamage(), item.getCount()});
		}
		config.save();
	}

	public static Long getTradeTime() {
		return System.currentTimeMillis() / 1000L + 259200;
	}

	public static int getPagesCount() {
		return AUCTION.size() == 0 ? 1 : (int)Math.ceil((float)AUCTION.size() / AUCTION_CHEST_SIZE);
	}

	public static void updateAuction() {
		Iterator iterator = AUCTION.entrySet().iterator();
		while (iterator.hasNext()) {
			TradeItem tradeItem = (TradeItem)((Map.Entry)iterator.next()).getValue();
			if (!tradeItem.isValid()) {
				Player player = Server.getInstance().getPlayerExact(tradeItem.getSellerName());
				if (player != null) {
					player.sendMessage(PREFIX + "§fВаш товар никто не купил§7, §fпоэтому мы вернули его обртано\n§l§6| §r§fСмотрите вкладку §7(§6Хранилище§7) §fв §7/§6ah");
				}
				File dataFile = new File(AnarchyMain.folder + "/Auction/PlayerItems/" + tradeItem.getSellerName() + ".yml");
				Config config = new Config(dataFile, Config.YAML);
				config.set(tradeItem.getUUID(), new Object[] {tradeItem.getSellItem().getId(), tradeItem.getSellItem().getDamage(), tradeItem.getSellItem().getCount()});
				config.save();
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
		auctionChest.setTitle("§l§fТорговая Площадка");
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
			Item item = tradeItem.getSellItem().clone();
			CompoundTag compoundTag = item.hasCompoundTag() ? item.getNamedTag() : new CompoundTag();
			compoundTag.putString("UUID", tradeItem.getUUID());
			item.setNamedTag(compoundTag);
			item.setLore("\n§r§fПродавец§7: §6" + tradeItem.getSellerName() + "\n§r§fСтоимость§7: §6" + tradeItem.getItemPrice() + "\n§r§fДо окончания§7: §6" + (tradeItem.getTime() / 86400 % 24) + " §fд§7. §6" + (tradeItem.getTime() / 3600 % 24) + " §fч§7. §6" + (tradeItem.getTime() / 60 % 60) + " §fмин§7. §6" + (tradeItem.getTime() % 60) + " §fсек§7.\n\n§r§l§6• §r§fНажмите§7, §fчтобы купить предмет§7!");
			auctionChest.setItem(i, item);
		}
		if (playerPage >= 0) {
			auctionChest.setItem(46, Item.get(Item.CHEST).setCustomName("§r§6Ваши Предметы на Продаже").setLore("\n§r§l§6• §r§fНажмите§7, §fчтобы открыть§7!"));
			auctionChest.setItem(47, Item.get(Item.MINECART_WITH_CHEST).setCustomName("§r§6Хранилище").setLore("\n§r§l§6• §r§fНажмите§7, §fчтобы открыть§7!"));
			auctionChest.setItem(49, Item.get(Item.DOUBLE_PLANT).setCustomName("§r§6Обновление страницы").setLore("\n§r§fТоваров§7: §6" + AUCTION.size() + "\n§r§fСтраница§7: §6" + (AUCTION_PAGE.get(player) + 1) + "§7/§6" + getPagesCount() + "\n\n§r§l§6• §r§fНажмите§7, §fчтобы обновить страницу§7!"));
			auctionChest.setItem(50, Item.get(Item.SIGN).setCustomName("§r§6Справка").setLore("\n§r§fЭто торговая площадка§7, §fкоторая создана\n§r§fдля покупки и продажи предметов§7.\n\n§r§fТорговая площадка также является\n§r§fотличным способом заработать §6Монет§7, §fпродавая\n§r§fфермерские товары§7, §fкоторые могут\n§r§fзаинтересовать других Игроков§7.\n\n§r§fЧтобы вы��тавить предмет на продажу§7,\n§r§fвозьмите его в руку и введите\n§r§6/auc §7(§6цена§7)"));
			auctionChest.setItem(52, Item.get(Item.PAPER).setCustomName("§r§6Листнуть назад").setLore("\n§r§l§6• §r§fНажмите§7, §fчтобы перейти"));
			auctionChest.setItem(53, Item.get(Item.PAPER).setCustomName("§r§6Листнуть вперед").setLore("\n§r§l§6• §r§fНажмите§7, §fчтобы перейти"));
			for (int i = 36; i <= 44; i++) {
				auctionChest.setItem(i, Item.get(Item.STAINED_GLASS_PANE, 8).setCustomName("§r§7:D"));
			}
		}
		if (firstTime) {
			FakeInventoryAPI.openInventory(player, auctionChest);
		}
		AUCTION_CHEST.put(player, auctionChest);
	}
}