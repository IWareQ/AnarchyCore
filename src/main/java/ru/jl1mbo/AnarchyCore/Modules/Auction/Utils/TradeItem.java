package ru.jl1mbo.AnarchyCore.Modules.Auction.Utils;

import cn.nukkit.item.Item;

public class TradeItem {
	private String seller;
	private double price;
	private long time;
	private Item item;
	private String uuid;

	public TradeItem(Item item, String seller, double price, long time, String UUID) {
		this.seller = seller;
		this.price = price;
		this.time = time;
		this.item = item;
		this.uuid = UUID;
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

	public String getUUID() {
		return this.uuid;
	}

	public boolean isOutdated() {
		return this.time <= System.currentTimeMillis() / 1000L;
	}

	public long getTime() {
		return this.time - System.currentTimeMillis() / 1000L;
	}
}