package ru.jl1mbo.AnarchyCore.GameHandler.Auction;

import java.io.IOException;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.item.Item;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.PluginManager;
import cn.nukkit.utils.Config;
import ru.jl1mbo.AnarchyCore.Main;
import ru.jl1mbo.AnarchyCore.GameHandler.Auction.Commands.AuctionCommand;
import ru.jl1mbo.AnarchyCore.GameHandler.Auction.EventsListener.InventoryTransactionListener;
import ru.jl1mbo.AnarchyCore.GameHandler.Auction.Utils.TradeItem;
import ru.jl1mbo.AnarchyCore.GameHandler.Auction.Utils.Inventory.AuctionChest;
import ru.jl1mbo.AnarchyCore.Manager.FakeInventory.FakeInventoryAPI;
import ru.jl1mbo.AnarchyCore.Utils.ConfigUtils;

public class AuctionAPI extends PluginBase {
	public static LinkedHashMap<String, TradeItem> AUCTION = new LinkedHashMap<>();
	public static Map<Player, AuctionChest> AUCTION_CHEST = new HashMap<>();
	public static Map<Player, Integer> AUCTION_PAGE = new HashMap<>();
	public static double AUCTION_MAX_PRICE = 10000.0;
	public static int AUCTION_CHEST_SIZE = 36;
	public static int AUCTION_MAX_SELLS = 5;
	public static String PREFIX = "§l§7(§3Аукцион§7) §r";

	public static void register() {
		PluginManager pluginManager = Server.getInstance().getPluginManager();
		pluginManager.registerEvents(new InventoryTransactionListener(), Main.getInstance());
		Server.getInstance().getCommandMap().register("", new AuctionCommand());
		for (Map.Entry<String, Object> entry : ConfigUtils.getAuctionConfig().getAll().entrySet()) {
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
			Item item = Item.get((int) itemData.get(3), (int) itemData.get(4), (int) itemData.get(5));
			item.setNamedTag(nbt);
			AUCTION.put(entry.getKey(), new TradeItem(item, itemData.get(0).toString(), (double) itemData.get(1), Long.valueOf(itemData.get(2).toString()), entry.getKey()));
		}
	}

	public static void saveAuction() {
		Config config = ConfigUtils.getAuctionConfig();
		for (Map.Entry<String, TradeItem> entry : AUCTION.entrySet()) {
			try {
				TradeItem tradeItem = entry.getValue();
				Item item = tradeItem.sellItem;
				config.set(entry.getKey(), item.hasCompoundTag() ? new Object[] {tradeItem.sellerName, tradeItem.itemPrice, tradeItem.sellTime, item.getId(), item.getDamage(), item.getCount(), NBTIO.write(item.getNamedTag(), ByteOrder.LITTLE_ENDIAN)} :
						   new Object[] {tradeItem.sellerName, tradeItem.itemPrice, tradeItem.sellTime, item.getId(), item.getDamage(), item.getCount()});
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		config.save();
		config.reload();
	}

	public static Long getTradeTime() {
		return System.currentTimeMillis() / 1000L + 259200;
	}

	public static int getPagesCount() {
		return AUCTION.size() == 0 ? 1 : (int) Math.ceil((float) AUCTION.size() / AUCTION_CHEST_SIZE);
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
		int stop = Math.min(tradeSize, start + AUCTION_CHEST_SIZE);
		Object[] tradeItems = AUCTION.values().toArray();
		for (int i = start; i < stop; i++) {
			TradeItem tradeItem = (TradeItem) tradeItems[i];
			Item item = tradeItem.sellItem.clone();
			CompoundTag compoundTag = item.hasCompoundTag() ? item.getNamedTag() : new CompoundTag();
			compoundTag.putString("UUID", tradeItem.UUID);
			item.setNamedTag(compoundTag);
			item.setLore("\n§r§fПродавец§7: §6" + tradeItem.sellerName + "\n§r§fСтоимость§7: §6" + tradeItem.itemPrice + "\n§r§fДо окончания§7: §6" +
						 (tradeItem.getTime() / 86400 % 24) + " §fд§7. §6" + (tradeItem.getTime() / 3600 % 24) + " §fч§7. §6" + (tradeItem.getTime() / 60 % 60) + " §fмин§7. §6" +
						 (tradeItem.getTime() % 60) + " §fсек§7.\n\n§r§l§6• §r§fНажмите§7, §fчтобы купить предмет§7!");
			auctionChest.addItem(item);
		}
		if (playerPage >= 0) {
			auctionChest.setItem(47, Item.get(Item.MINECART_WITH_CHEST).setCustomName("§r§6Хранилище").setLore("\n§r§l§6• §r§fНажмите§7, §fчтобы открыть§7!"));
			auctionChest.setItem(49, Item.get(Item.DOUBLE_PLANT).setCustomName("§r§6Обновление страницы").setLore("\n§r§fТоваров§7: §6" + AUCTION.size() +
								 "\n§r§fСтраница§7: §6" + (AUCTION_PAGE.get(player) + 1) + "§7/§6" + getPagesCount() + "\n\n§r§l§6• §r§fНажмите§7, §fчтобы обновить страницу§7!"));
			auctionChest.setItem(50, Item.get(
									 Item.SIGN).setCustomName("§r§6Справка").setLore("\n§r§fЭто торговая площадка§7, §fкоторая создана\n§r§fдля покупки и продажи предметов§7.\n\n§r§fТорговая площадка также является\n§r§fотличным способом заработать §6Монет§7, §fпродавая\n§r§fфермерские товары§7, §fкоторые могут\n§r§fзаинтересовать других Игроков§7.\n\n§r§fЧтобы вы��тавить предмет на продажу§7,\n§r§fвозьмите его в руку и введите\n§r§6/auc §7(§6цена§7)"));
			auctionChest.setItem(52, Item.get(Item.PAPER).setCustomName("§r§6Листнуть назад").setLore("\n§r§l§6• §r§fНажмите§7, §fчтобы перейти"));
			auctionChest.setItem(53, Item.get(Item.PAPER).setCustomName("§r§6Листнуть вперед").setLore("\n§r§l§6• §r§fНажмите§7, §fчтобы перейти"));
			for (int i = 36; i <= 44; i++) {
				auctionChest.setItem(i, Item.get(Item.STAINED_GLASS_PANE, 8).setCustomName("§r§7:D"));
			}
		}
		if (firstTime) {
			FakeInventoryAPI.openDoubleChestInventory(player, auctionChest);
		}
		AUCTION_CHEST.put(player, auctionChest);
	}
}