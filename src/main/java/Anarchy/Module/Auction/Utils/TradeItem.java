package Anarchy.Module.Auction.Utils;

import cn.nukkit.item.Item;

public class TradeItem {
	private String sellerName;
	private Double itemPrice;
	private Long sellTime;
	private Item sellItem;
	private String UUID;
	
	public TradeItem(String sellerName, Double itemPrice, Long sellTime, Item sellItem, String UUID) {
		this.sellerName = sellerName;
		this.itemPrice = itemPrice;
		this.sellTime = sellTime;
		this.sellItem = sellItem;
		this.UUID = UUID;
	}
	
	public String getSellerName() {
		return sellerName;
	}
	
	public Double getItemPrice() {
		return itemPrice;
	}
	
	public Item getSellItem() {
		return sellItem;
	}
	
	public boolean isValid() {
		return sellTime > System.currentTimeMillis() / 1000L;
	}
	
	public long getTime() {
		return sellTime - System.currentTimeMillis() / 1000L;
	}
	
	public String getUUID() {
		return UUID;
	}
}