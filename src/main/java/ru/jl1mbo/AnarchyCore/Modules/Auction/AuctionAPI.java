package ru.jl1mbo.AnarchyCore.Modules.Auction;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.plugin.PluginBase;
import ru.jl1mbo.AnarchyCore.Manager.FakeInventory.FakeInventoryAPI;
import ru.jl1mbo.AnarchyCore.Modules.Auction.Utils.TradeItem;
import ru.jl1mbo.AnarchyCore.Modules.Auction.Utils.Inventory.AuctionChest;
import ru.jl1mbo.AnarchyCore.Modules.Auction.Utils.Inventory.AuctionStorageChest;
import ru.jl1mbo.AnarchyCore.Utils.Utils;
import ru.jl1mbo.MySQLUtils.MySQLUtils;

public class AuctionAPI extends PluginBase {
	private static Map<Player, AuctionStorageChest> STORAGE_CHEST = new HashMap<>();
	public static LinkedHashMap<Integer, TradeItem> AUCTION = new LinkedHashMap<>();
	public static Map<Player, AuctionChest> AUCTION_CHEST = new HashMap<>();
	public static Map<Player, Integer> AUCTION_PAGE = new HashMap<>();
	public static double AUCTION_MAX_PRICE = 10000.0;
	public static int AUCTION_CHEST_SIZE = 36;
	public static int AUCTION_MAX_SELLS = 5;
	public static String PREFIX = "§l§7(§3Аукцион§7) §r";

	public static void register() {
		List<Integer> ids = MySQLUtils.getIntegerList("SELECT `ID` FROM `Auction`;");
		for (int id : ids) {
			Map<String, String> auctionData = MySQLUtils.getStringMap("SELECT * FROM `Auction` WHERE (`ID`) = '" + id + "';");
			CompoundTag namedTag = null;
			if (auctionData.get("namedTag") != null) {
				namedTag = Utils.convertHexToNBT(auctionData.get("namedTag"));
			}
			if (namedTag == null) {
				namedTag = new CompoundTag();
			}
			namedTag.putInt("ID", id);
			Item item = Item.get(Integer.parseInt(auctionData.get("Id")), Integer.parseInt(auctionData.get("Damage")), Integer.parseInt(auctionData.get("Count"))).setNamedTag(namedTag);
			AUCTION.put(id, new TradeItem(auctionData.get("Seller"), item, Double.parseDouble(auctionData.get("Price")), Long.parseLong(auctionData.get("Time")), id));
		}
	}

	public static void removeItem(int id, Item item) {
		AUCTION.remove(id);
		MySQLUtils.query("DELETE FROM `Auction` WHERE (`ID`) = '" + id + "';");
		for (AuctionChest auctionChest : AUCTION_CHEST.values()) {
			auctionChest.removeItem(item);
		}
	}

	public static int getPagesCount() {
		return AUCTION.size() == 0 ? 1 : (int) Math.ceil((float) AUCTION.size() / AUCTION_CHEST_SIZE);
	}

	public static void showAuction(Player player, boolean reflesh) {
		int playerPage = AUCTION_PAGE.get(player);
		AuctionChest auctionChest = new AuctionChest("Торговая Площадка");
		if (reflesh) {
			auctionChest = AUCTION_CHEST.getOrDefault(player, auctionChest);
			auctionChest.clearAll();
		}
		int tradeSize = AUCTION.size();
		int start = playerPage * AUCTION_CHEST_SIZE;
		int stop = Math.min(tradeSize, start + AUCTION_CHEST_SIZE);
		Object[] tradeItems = AUCTION.values().toArray();
		for (int i = start; i < stop; i++) {
			TradeItem tradeItem = (TradeItem) tradeItems[i];
			Item item = tradeItem.getItem().clone();
			CompoundTag namedTag = item.hasCompoundTag() ? item.getNamedTag() : new CompoundTag();
			namedTag.putInt("ID", tradeItem.getId());
			item.setNamedTag(namedTag);
			item.setLore("\n§rПродавец§7: §6" + tradeItem.getSeller() + "\n§rСтоимость§7: §6" + tradeItem.getPrice() + "\n§rДо окончания§7: §6" +
						 (tradeItem.getTime() / 86400 % 24) + " §fд§7. §6" + (tradeItem.getTime() / 3600 % 24) + " §fч§7. §6" + (tradeItem.getTime() / 60 % 60) + " §fмин§7. §6" +
						 (tradeItem.getTime() % 60) + " §fсек§7.\n\n§l§6• §rНажмите§7, §fчтобы купить предмет§7!");
			auctionChest.addItem(item);
		}
		auctionChest.setItem(47, Item.get(Item.MINECART_WITH_CHEST).setCustomName("§r§6Хранилище").setLore("\n§r§l§6• §rНажмите§7, §fчтобы перейти§7!"));
		auctionChest.setItem(49, Item.get(Item.DOUBLE_PLANT).setCustomName("§r§6Обновление страницы").setLore("\n§rТоваров§7: §6" + AUCTION.size() +
							 "\n§rСтраница§7: §6" + (AUCTION_PAGE.get(player) + 1) + "§7/§6" + getPagesCount() + "\n\n§l§6• §rНажмите§7, §fчтобы обновить страницу§7!"));
		auctionChest.setItem(50, Item.get(
								 Item.SIGN).setCustomName("§r§6Справка").setLore("\n§rЭто торговая площадка§7, §fкоторая создана\nдля покупки и продажи предметов§7.\n\n§fТорговая площадка также является\nотличным способом заработать §6Монет§7, §fпродавая\nфермерские товары§7, §fкоторые могут\nзаинтересовать других Игроков§7.\n\n§rЧтобы выставить предмет на продажу§7,\n§fвозьмите его в руку и введите\n§6/auc §7(§6цена§7)"));
		auctionChest.setItem(52, Item.get(Item.PAPER).setCustomName("§r§6Листнуть назад").setLore("\n§r§l§6• §rНажмите§7, §fчтобы перейти§7!"));
		auctionChest.setItem(53, Item.get(Item.PAPER).setCustomName("§r§6Листнуть вперед").setLore("\n§r§l§6• §rНажмите§7, §fчтобы перейти§7!"));
		for (int i = 36; i <= 44; i++) {
			auctionChest.setItem(i, Item.get(Item.STAINED_GLASS_PANE, 8).setCustomName("§r§7:D"));
		}
		FakeInventoryAPI.openDoubleChestInventory(player, auctionChest);
		AUCTION_CHEST.put(player, auctionChest);
	}

	public static void openAuctionStorageChest(Player player, boolean reflesh) {
		AuctionStorageChest auctionStorageChest = new AuctionStorageChest("Хранилище Аукциона");
		if (reflesh) {
			auctionStorageChest = STORAGE_CHEST.getOrDefault(player, new AuctionStorageChest("Хранилище Аукциона"));
			auctionStorageChest.clearAll();
		}
		List<Integer> ids = MySQLUtils.getIntegerList("SELECT `ID` FROM `AuctionStorage` WHERE UPPER (`Name`) = '" + player.getName().toUpperCase() + "';");
		for (int id : ids) {
			Map<String, String> storageData =  MySQLUtils.getStringMap("SELECT * FROM `AuctionStorage` WHERE (`ID`) = '" + id + "';");
			Item item = Item.get(Integer.parseInt(storageData.get("Id")), Integer.parseInt(storageData.get("Damage")),
								 Integer.parseInt(storageData.get("Count"))).setNamedTag(Utils.convertHexToNBT(storageData.get("namedTag")).putInt("ID", id));
			auctionStorageChest.addItem(item.setLore("\n§r§l§6• §rНажмите§7, §fчтобы забрать§7!"));
		}
		auctionStorageChest.setItem(49, Item.get(Item.DOUBLE_PLANT).setCustomName("§r§6Обновить").setLore("\n\n§l§6• §rНажмите§7, §fчтобы обновить страницу§7!"));
		FakeInventoryAPI.openDoubleChestInventory(player, auctionStorageChest);
		STORAGE_CHEST.put(player, auctionStorageChest);
	}
}