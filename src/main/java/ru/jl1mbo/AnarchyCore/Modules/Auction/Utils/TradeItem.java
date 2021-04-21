package ru.jl1mbo.AnarchyCore.Modules.Auction.Utils;

import cn.nukkit.item.Item;

public class TradeItem {

	private final String seller;
	private final Item item;
	private final double price;
	private final long time;
	private final int id;

	public TradeItem(String seller, Item item, double price, long time, int id) {
		this.seller = seller;
		this.item = item;
		this.price = price;
		this.time = time;
		this.id = id;
	}

	public String getSeller() {
		return this.seller;
	}

	public double getPrice() {
		return this.price;
	}

	public Item getItem() {
		return this.item;
	}

	public int getId() {
		return this.id;
	}

	public boolean isOutdated() {
		return this.time <= System.currentTimeMillis() / 1000L;
	}

	public long getTime() {
		return this.time - System.currentTimeMillis() / 1000L;
	}
}