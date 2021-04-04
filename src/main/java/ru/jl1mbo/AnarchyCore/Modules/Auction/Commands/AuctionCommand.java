package ru.jl1mbo.AnarchyCore.Modules.Auction.Commands;

import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.item.Item;
import ru.jl1mbo.AnarchyCore.Modules.Auction.AuctionAPI;
import ru.jl1mbo.AnarchyCore.Modules.Auction.Utils.TradeItem;
import ru.jl1mbo.AnarchyCore.Utils.Utils;
import ru.jl1mbo.MySQLUtils.MySQLUtils;

public class AuctionCommand extends Command {

	public AuctionCommand() {
		super("auction", "§rОткрыть Аукцион", "", new String[] {"auc", "ah"});
		this.commandParameters.clear();
		this.commandParameters.put("auction", new CommandParameter[] {CommandParameter.newType("money", false, CommandParamType.INT)});
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length == 0) {
				AuctionAPI.AUCTION_PAGE.put(player, 0);
				AuctionAPI.showAuction(player, true);
				return true;
			}
			if (args[0].equals("storage")) {
				AuctionAPI.openAuctionStorageChest(player, false);
			} else {
				int count = 0;
				for (Map.Entry<String, TradeItem> entry : AuctionAPI.AUCTION.entrySet()) {
					TradeItem tradeItem = entry.getValue();
					if (tradeItem.sellerName.equals(player.getName())) {
						count++;
					}
				}
				if (MySQLUtils.getInteger("SELECT COUNT(*) as COUNT FROM `AuctionStorage` WHERE UPPER (`Name`) = '" + player.getName().toUpperCase() + "'") + count >= AuctionAPI.AUCTION_MAX_SELLS) {
					player.sendMessage(AuctionAPI.PREFIX + "Вы уже разместили или храните максимальное колличество лотов §7(§6" + AuctionAPI.AUCTION_MAX_SELLS +
									   "§7)");
					return false;
				}
				if (!Utils.isDouble(args[0])) {
					if (args.length != 1) {
						player.sendMessage("§l§6• §r§fИспользование §7- /§6auc §7(§6цена§7)");
					}
					return false;
				}
				Item sellItem = player.getInventory().getItemInHand();
				if (sellItem == null || sellItem.getId() == Item.AIR) {
					player.sendMessage(AuctionAPI.PREFIX + "§fЧтобы выставить предмет на продажу§7, §fвозьмите его в руку");
					return false;
				}
				double itemPrice = Double.parseDouble(args[0]);
				if (itemPrice <= 0 || itemPrice > AuctionAPI.AUCTION_MAX_PRICE) {
					player.sendMessage(AuctionAPI.PREFIX + "§fМаксимальная цена за предмет §7- §6" + AuctionAPI.AUCTION_MAX_PRICE + "");
					return false;
				}
				player.sendMessage(AuctionAPI.PREFIX + "§fПредмет на продажу §6успешно §fвыставлен за §6" + String.format("%.1f",
								   itemPrice) + "§7, §fв колличестве §6" + sellItem.count + " §fшт§7.");
				Server.getInstance().broadcastMessage(AuctionAPI.PREFIX + "§fИгрок §6" + player.getName() + " §fвыставил предмет на продажу§7!");
				String UUID = java.util.UUID.randomUUID().toString();
				AuctionAPI.AUCTION.put(UUID, new TradeItem(sellItem, player.getName(), itemPrice, AuctionAPI.getTradeTime(), UUID));
				player.getInventory().setItemInHand(Item.get(Item.AIR));
			}
		}
		return false;
	}
}