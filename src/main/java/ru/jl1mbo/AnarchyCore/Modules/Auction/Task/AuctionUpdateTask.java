package ru.jl1mbo.AnarchyCore.Modules.Auction.Task;

import java.util.Map.Entry;

import cn.nukkit.scheduler.Task;
import ru.jl1mbo.AnarchyCore.Modules.Auction.AuctionAPI;
import ru.jl1mbo.AnarchyCore.Modules.Auction.Utils.TradeItem;
import ru.jl1mbo.AnarchyCore.Utils.Utils;
import ru.jl1mbo.MySQLUtils.MySQLUtils;

public class AuctionUpdateTask extends Task {

	@Override()
	public void onRun(int currentTick) {
		for (Entry<String, TradeItem> entry : AuctionAPI.AUCTION.entrySet()) {
			TradeItem tradeItem = entry.getValue();
			if (tradeItem.isOutdated()) {
				MySQLUtils.query("INSERT INTO `AuctionStorage` (`Name`, `ItemId`, `ItemDamage`, `ItemCount`, `ItemNBT`) VALUES ('" + tradeItem.sellerName + "', '" + tradeItem.sellItem.getId() + "', '"
								 + tradeItem.sellItem.getDamage() + "', '" + tradeItem.sellItem.getCount() + "', '" + Utils.convertNbtToHex(tradeItem.sellItem.getNamedTag()) + "')");
				AuctionAPI.AUCTION.remove(entry.getKey());
			}
		}
	}
}