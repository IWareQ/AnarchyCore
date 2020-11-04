package Anarchy.Module.Auction.Utils;

import cn.nukkit.item.Item;

public class TradeItem {
	public Double itemPrice;
	public Item sellItem;
	public String UUID;
	public String sellerName;
	public Long sellTime;
	
	public TradeItem(Item sellItem, String sellerName, Double itemPrice, Long sellTime, String UUID) {
		this.UUID = UUID;
		this.sellItem = sellItem;
		this.sellerName = sellerName;
		this.itemPrice = itemPrice;
		this.sellTime = sellTime;
	}
	
	public boolean isValid() {
		return sellTime > System.currentTimeMillis() / 1000L;
	}
	
	public long getTime() {
		return sellTime - System.currentTimeMillis() / 1000L;
	}
}