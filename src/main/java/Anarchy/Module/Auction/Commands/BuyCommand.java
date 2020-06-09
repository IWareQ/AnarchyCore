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
		Player player = (Player) commandSender;
		AuctionAPI.AUCTION_PAGE.put(player, 0);
		AuctionAPI.showAuction(player, true);
		return false;
	}
}