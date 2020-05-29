package Anarchy.Module.Auction.Commands;

import Anarchy.Module.Auction.AuctionAPI;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class BuyCommand extends Command {
	public BuyCommand() {
		super("buy", "Открыть аукцион");
		commandParameters.clear();
	}

	@Override
	public boolean execute(CommandSender commandSender, String s, String[] strings) {
		AuctionAPI.AUCTION_PAGE.put((Player) commandSender, 0);
		AuctionAPI.showAuction((Player) commandSender, true);
		return false;
	}
}