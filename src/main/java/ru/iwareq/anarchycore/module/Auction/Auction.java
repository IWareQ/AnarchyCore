package ru.iwareq.anarchycore.module.Auction;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.level.Sound;
import cn.nukkit.nbt.tag.CompoundTag;
import ru.iwareq.anarchycore.manager.FakeInventory.FakeInventoryAPI;
import ru.iwareq.anarchycore.module.Auction.Utils.Inventory.AuctionChest;
import ru.iwareq.anarchycore.module.Auction.Utils.Inventory.AuctionStorageChest;
import ru.iwareq.anarchycore.module.Auction.Utils.TradeItem;
import ru.iwareq.anarchycore.module.Auth.AuthAPI;
import ru.iwareq.anarchycore.module.Economy.EconomyAPI;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Auction {

	public static final String PREFIX = "§l§7(§3Аукцион§7) §r";

	public static final double MAX_PRICE = 25_000D;
	public static final double MIN_PRICE = 1D;
	public static final int MAX_SELLS = 10;
	public static final Map<Player, AuctionChest> PLAYER_AUCTION = new HashMap<>();
	private static final int CHEST_SIZE = 36;
	private static final Map<Integer, TradeItem> ITEMS = new ConcurrentHashMap<>();
	private static final Map<String, Integer> PLAYER_PAGE = new HashMap<>();
	private static final AuctionDB DB = new AuctionDB();

	public static void load() {
		ITEMS.putAll(DB.getAuctionItems());
	}

	public static int getPagesCount() {
		return ITEMS.isEmpty() ? 1 : (int) Math.ceil((double) ITEMS.size() / CHEST_SIZE);
	}

	public static int getPlayerPage(Player player) {
		return PLAYER_PAGE.getOrDefault(player.getName().toLowerCase(), 1);
	}

	public static void showAuction(Player player, int page) {
		int start = (page - 1) * CHEST_SIZE;
		int stop = Math.min(ITEMS.size(), start + CHEST_SIZE);

		AuctionChest auctionChest = new AuctionChest(page);
		TradeItem[] items = ITEMS.values().toArray(new TradeItem[0]);
		for (int index = start; index < stop; index++) {
			TradeItem tradeItem = items[index];

			Item item = tradeItem.getItem().clone();
			CompoundTag namedTag = item.getNamedTag();
			if (namedTag == null) {
				namedTag = new CompoundTag();
			}

			namedTag.putInt("ID", tradeItem.getId());
			item.setNamedTag(namedTag);
			item.setLore("\n§rПродавец§7: §6" + tradeItem.getSeller()
					+ "\n§rСтоимость§7: §6" + tradeItem.getPrice() + ""
					+ "\n§rДо окончания§7: §6" + tradeItem.getTimeString()
					+ "\n\n§l§6• §rНажмите§7, §fчтобы купить предмет§7!");

			auctionChest.addItem(item);
		}

		FakeInventoryAPI.closeDoubleChestInventory(player, auctionChest);
		FakeInventoryAPI.openDoubleChestInventory(player, auctionChest);

		PLAYER_PAGE.put(player.getName(), page);
		PLAYER_AUCTION.put(player, auctionChest);
	}

	public static void showStorageAuction(Player player) {
		AuctionStorageChest storageChest = new AuctionStorageChest("Хранилище");
		Map<Integer, Item> items = DB.getStorageItems(player);
		items.forEach((id, item) -> {
			CompoundTag namedTag = item.getNamedTag();
			if (namedTag == null) {
				namedTag = new CompoundTag();
			}

			namedTag.putInt("ID", id);
			item.setNamedTag(namedTag);
			item.setLore("\n§l§6• §rНажмите§7, §fчтобы забрать предмет§7!");

			storageChest.addItem(item);
		});

		FakeInventoryAPI.openDoubleChestInventory(player, storageChest);
	}

	public static void buyItem(Player player, int id, Item item) {
		TradeItem tradeItem = ITEMS.get(id);
		if (tradeItem != null) {
			if (AuthAPI.getMoney(player.getName()) < tradeItem.getPrice()) {
				player.sendMessage(Auction.PREFIX + "Недостаточно §6монет §fдля совершения покупки§7!");
				player.getLevel().addSound(player, Sound.NOTE_BASS, 1, 1, player);
				return;
			}

			PlayerInventory playerInventory = player.getInventory();
			if (playerInventory.canAddItem(item)) {
				CompoundTag namedTag = item.getNamedTag();
				if (tradeItem.getSeller().equals(player.getName())) {
					namedTag.remove("ID");
					namedTag.remove("display");

					playerInventory.addItem(item.setNamedTag(namedTag));

					Auction.removeItem(id, item);

					player.getLevel().addSound(player, Sound.RANDOM_LEVELUP, 1, 1, player);
					player.sendMessage(Auction.PREFIX + "Предмет был §6снят с продажи §fи отправлен Вам в Инвентарь");
				} else {
					namedTag.remove("ID");
					namedTag.remove("display");

					playerInventory.addItem(item.setNamedTag(namedTag));

					Auction.removeItem(id, item);

					player.getLevel().addSound(player, Sound.RANDOM_LEVELUP, 1, 1, player);
					player.sendMessage(Auction.PREFIX + "Предмет успешно куплен за §6" + tradeItem.getPrice() + "§7," +
							" §fв колличестве §6" + item.getCount() + " §fшт§7.");
					Player target = Server.getInstance().getPlayerExact(tradeItem.getSeller());
					if (target != null) {
						target.sendMessage(Auction.PREFIX + "Игрок §6" + player.getName() + " §fкупил Ваш товар за §6" + tradeItem.getPrice() + "");
						EconomyAPI.addMoney(target.getName(), tradeItem.getPrice());
					} else {
						EconomyAPI.addMoney(tradeItem.getSeller(), tradeItem.getPrice());
					}

					EconomyAPI.reduceMoney(player.getName(), tradeItem.getPrice());
				}

				Auction.removeItem(tradeItem.getId(), item);
			}
		} else {
			player.sendMessage(Auction.PREFIX + "Предмет уже §6продан §fили его §6сняли §fс продажи§7!");
		}
	}

	public static void removeItem(int id, Item item) {
		DB.removeItem(id);

		ITEMS.remove(id);

		PLAYER_AUCTION.keySet().forEach(p -> {
			showAuction(p, getPlayerPage(p));
			p.getLevel().addSound(p, Sound.BLOCK_BARREL_CLOSE, 1, 1, p);
		});
	}

	public static boolean canTrade(Player player) {
		return DB.canTrade(player);
	}

	public static void addItem(TradeItem item) {
		DB.addItem(item);

		load();

		PLAYER_AUCTION.keySet().forEach(p -> {
			showAuction(p, getPlayerPage(p));
			p.getLevel().addSound(p, Sound.BLOCK_BARREL_OPEN, 1, 1, p);
		});
	}

	public static void addStorageItem(TradeItem item) {
		DB.addStorageItem(item);
	}

	public static void removeStorageItem(String name, int id) {
		DB.removeStorageItem(name, id);
	}

	public static void checkItem() {
		ITEMS.forEach((id, tradeItem) -> {
			if (tradeItem.isOutdated()) {
				Item item = tradeItem.getItem();
				Auction.addStorageItem(tradeItem);
				Auction.removeItem(id, item);
			}
		});
	}
}