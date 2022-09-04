package ru.iwareq.anarchycore.module.Auction.Task;

import cn.nukkit.scheduler.Task;
import ru.iwareq.anarchycore.module.Auction.Auction;

public class AuctionUpdateTask extends Task {

	@Override()
	public void onRun(int currentTick) {
		Auction.checkItem();
	}
}