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
				MySQLUtils.query("INSERT INTO `AuctionStorage` (`Name`, `ID`, `Damage`, `Count`, `namedTag`) VALUES ('" + tradeItem.getSeller() + "', '" + tradeItem.getItem().getId() + "', '"
								 + tradeItem.getItem().getDamage() + "', '" + tradeItem.getItem().getCount() + "', '" + Utils.convertNbtToHex(tradeItem.getItem().getNamedTag()) + "');");
				AuctionAPI.AUCTION.remove(entry.getKey());
			}
		}
	}
}