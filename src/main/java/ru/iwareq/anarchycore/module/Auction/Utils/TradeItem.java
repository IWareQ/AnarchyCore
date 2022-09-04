package ru.iwareq.anarchycore.module.Auction.Utils;

import cn.nukkit.item.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.iwareq.anarchycore.util.Utils;

@Getter
@AllArgsConstructor
public class TradeItem {

	private final String seller;
	private final Item item;
	private final double price;
	private final long time;
	private final int id;

	public TradeItem(String seller, Item item, double price, long time) {
		this.seller = seller;
		this.item = item;
		this.price = price;
		this.time = time;
		this.id = 0;
	}

	public boolean isOutdated() {
		return this.time <= System.currentTimeMillis() / 1000L;
	}

	public long getTime() {
		return this.time - System.currentTimeMillis() / 1000L;
	}

	public String getTimeString() {
		return Utils.getRemainingTime(this.getTime());
	}
}
