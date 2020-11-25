package Anarchy.Module.Auction.Utils;

import cn.nukkit.item.Item;

public class TradeItem {
	public String sellerName;
	public Double itemPrice;
	public Long sellTime;
	public Item sellItem;
	public String UUID;

	public TradeItem(Item sellItem, String sellerName, Double itemPrice, Long sellTime, String UUID) {
		this.sellerName = sellerName;
		this.itemPrice = itemPrice;
		this.sellTime = sellTime;
		this.sellItem = sellItem;
		this.UUID = UUID;
	}

	public boolean isValid() {
		return sellTime > System.currentTimeMillis() / 1000L;
	}

	public Long getTime() {
		return sellTime - System.currentTimeMillis() / 1000L;
	}
}