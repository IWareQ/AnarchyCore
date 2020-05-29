package Anarchy.Module.Auction.Utils;

import cn.nukkit.item.Item;

public class TradeItem {
	public int itemPrice;
	public Item sellItem;
	public String UUID, sellerName, aboutMessage;
	public Long sellTime;

	public TradeItem(Item sellItem, String sellerName, String aboutMessage, int itemPrice, Long sellTime, String UUID) {
		this.UUID = UUID;
		this.sellItem = sellItem;
		this.sellerName = sellerName;
		this.aboutMessage = aboutMessage;
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