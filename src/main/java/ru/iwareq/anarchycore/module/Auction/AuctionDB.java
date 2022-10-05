package ru.iwareq.anarchycore.module.Auction;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.nbt.tag.CompoundTag;
import me.hteppl.data.database.SQLiteDatabase;
import org.jdbi.v3.core.Handle;
import ru.iwareq.anarchycore.module.Auction.Utils.TradeItem;
import ru.iwareq.anarchycore.util.NbtConverter;
import ru.iwareq.anarchycore.util.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuctionDB extends SQLiteDatabase {

	public AuctionDB() {
		super("Auction");

		this.executeScheme("CREATE TABLE IF NOT EXISTS Items\n" +
				"(\n" +
				"    ID          INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
				"    SellerName  varchar(32) NOT NULL COLLATE NOCASE,\n" +
				"    Price       REAL        NOT NULL,\n" +
				"    ItemID      INT         NOT NULL,\n" +
				"    ItemDamage  INT         NOT NULL,\n" +
				"    ItemCount   INT         NOT NULL,\n" +
				"    NbtHex      varchar(32) NOT NULL,\n" +
				"    ExpiredTime INT         NOT NULL\n" +
				");");

		this.executeScheme("CREATE TABLE IF NOT EXISTS Storage\n" +
				"(\n" +
				"    ID         INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
				"    Name       varchar(32) NOT NULL COLLATE NOCASE,\n" +
				"    ItemID     int(16)     NOT NULL,\n" +
				"    ItemDamage int(16)     NOT NULL,\n" +
				"    ItemCount  int(16)     NOT NULL,\n" +
				"    NbtHex     varchar(32) DEFAULT NULL\n" +
				");");
	}

	public void addItem(TradeItem item) {
		try (Handle handle = this.connect()) {
			handle.createUpdate("INSERT INTO Items (SellerName, Price, ItemID, ItemDamage, ItemCount, NbtHex, " +
							"ExpiredTime) VALUES (:sellerName, :price, :itemId, :itemDamage, :itemCount, :nbtHex, " +
							":expiredTime)")
					.bind("sellerName", item.getSeller())
					.bind("price", item.getPrice())
					.bind("itemId", item.getItem().getId())
					.bind("itemDamage", item.getItem().getDamage())
					.bind("itemCount", item.getItem().getCount())
					.bind("nbtHex", NbtConverter.toHex(item.getItem().getNamedTag()))
					.bind("expiredTime", item.getTime())
					.execute();
		}
	}

	public void removeItem(int id) {
		try (Handle handle = this.connect()) {
			handle.createUpdate("DELETE FROM Items WHERE ID = :id")
					.bind("id", id).execute();
		}
	}

	public boolean canTrade(Player player) {
		try (Handle handle = this.connect()) {
			int storageItemsCount = handle.createQuery("SELECT ID FROM Storage WHERE Name = :sellerName")
					.bind("sellerName", player.getName().toLowerCase())
					.mapTo(Integer.class).list().size();
			return handle.createQuery("SELECT ID FROM Items WHERE SellerName = :sellerName")
					.bind("sellerName", player.getName().toLowerCase())
					.mapTo(Integer.class).list().size() + storageItemsCount < Auction.MAX_SELLS;
		}
	}

	public Map<Integer, TradeItem> getAuctionItems() {
		try (Handle handle = this.connect()) {
			Map<Integer, TradeItem> result = new HashMap<>();
			List<Integer> ids = handle.select("SELECT ID FROM Items").mapTo(Integer.class).list();
			for (int id : ids) {
				TradeItem tradeItem = handle.select("SELECT * FROM Items WHERE ID = ?", id)
						.map(rowView -> {
							String seller = rowView.getColumn("SellerName", String.class);

							int itemId = rowView.getColumn("ItemId", Integer.class);
							int damage = rowView.getColumn("ItemDamage", Integer.class);
							int count = rowView.getColumn("ItemCount", Integer.class);

							Item item = Item.get(itemId, damage, count);

							CompoundTag namedTag = null;
							String hex = rowView.getColumn("NbtHex", String.class);
							if (hex != null) {
								namedTag = NbtConverter.toNbt(hex);
							}

							if (namedTag == null) {
								namedTag = new CompoundTag();
							}

							namedTag.putInt("ID", id);
							item = item.setNamedTag(namedTag);

							double price = rowView.getColumn("Price", Double.class);
							long time = rowView.getColumn("ExpiredTime", Long.class);
							return new TradeItem(seller, item, price, System.currentTimeMillis() / 1000L + time, id);
						}).one();

				result.put(id, tradeItem);
			}

			return result;
		}
	}

	public Map<Integer, Item> getStorageItems(Player player) {
		try (Handle handle = this.connect()) {
			Map<Integer, Item> result = new HashMap<>();
			List<Integer> ids =
					handle.select("SELECT ID FROM Storage WHERE Name = ?", player.getName().toLowerCase()).mapTo(Integer.class).list();
			for (int id : ids) {
				Item tradeItem = handle.select("SELECT * FROM Storage WHERE ID = ?", id)
						.map(rowView -> {
							int itemId = rowView.getColumn("ItemId", Integer.class);
							int damage = rowView.getColumn("ItemDamage", Integer.class);
							int count = rowView.getColumn("ItemCount", Integer.class);

							Item item = Item.get(itemId, damage, count);

							CompoundTag namedTag = null;
							String hex = rowView.getColumn("NbtHex", String.class);
							if (hex != null) {
								namedTag = NbtConverter.toNbt(hex);
							}

							if (namedTag == null) {
								namedTag = new CompoundTag();
							}

							namedTag.putInt("ID", id);
							return item.setNamedTag(namedTag);
						}).one();

				result.put(id, tradeItem);
			}

			return result;
		}
	}

	public void removeStorageItem(String name, int id) {
		try (Handle handle = this.connect()) {
			handle.createUpdate("DELETE FROM Storage WHERE ID = :id AND Name = :name")
					.bind("name", name.toLowerCase())
					.bind("id", id).execute();
		}
	}

	public void addStorageItem(TradeItem item) {
		try (Handle handle = this.connect()) {
			handle.createUpdate("INSERT INTO Storage (Name, ItemID, ItemDamage, ItemCount, NbtHex) VALUES (:name, :itemId, :itemDamage, :itemCount, :nbtHex)")
					.bind("name", item.getSeller().toLowerCase())
					.bind("itemId", item.getItem().getId())
					.bind("itemDamage", item.getItem().getDamage())
					.bind("itemCount", item.getItem().getCount())
					.bind("nbtHex", NbtConverter.toHex(item.getItem().getNamedTag()))
					.execute();
		}
	}
}