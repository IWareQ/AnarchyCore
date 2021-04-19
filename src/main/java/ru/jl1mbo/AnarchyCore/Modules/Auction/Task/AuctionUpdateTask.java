package ru.jl1mbo.AnarchyCore.Modules.Auction.Task;

import java.util.Map.Entry;

import cn.nukkit.item.Item;
import cn.nukkit.scheduler.Task;
import ru.jl1mbo.AnarchyCore.Modules.Auction.AuctionAPI;
import ru.jl1mbo.AnarchyCore.Modules.Auction.Utils.TradeItem;
import ru.jl1mbo.AnarchyCore.Utils.Utils;
import ru.jl1mbo.MySQLUtils.MySQLUtils;

public class AuctionUpdateTask extends Task {

	@Override()
	public void onRun(int currentTick) {
		for (Entry<Integer, TradeItem> entry : AuctionAPI.AUCTION.entrySet()) {
			TradeItem tradeItem = entry.getValue();
			if (tradeItem.isOutdated()) {
				Item item = tradeItem.getItem();
				MySQLUtils.query("INSERT INTO `AuctionStorage` (`Name`, `Id`, `Damage`, `Count`, `namedTag`) VALUES ('" + tradeItem.getSeller() + "', '" + item.getId() + "', '" + item.getDamage() + "', '" + item.getCount() + "', '" + Utils.convertNbtToHex(item.getNamedTag()) + "');");
				AuctionAPI.removeItem(entry.getKey(), item);
			}
		}
	}
}