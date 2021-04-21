package ru.jl1mbo.AnarchyCore.Modules.Auction.Commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.item.Item;
import ru.jl1mbo.AnarchyCore.Modules.Auction.AuctionAPI;
import ru.jl1mbo.AnarchyCore.Modules.Auction.Utils.TradeItem;
import ru.jl1mbo.AnarchyCore.Utils.Utils;
import ru.jl1mbo.MySQLUtils.MySQLUtils;

import java.util.Map.Entry;

public class AuctionCommand extends Command {

	public AuctionCommand() {
		super("auction", "§rОткрыть Аукцион", "", new String[]{"auc", "ah"});
		this.commandParameters.clear();
		this.commandParameters.put("auction", new CommandParameter[]{CommandParameter.newType("money", false, CommandParamType.INT)});
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
			int count = 0;
			for (Entry<Integer, TradeItem> entry : AuctionAPI.AUCTION.entrySet()) {
				TradeItem tradeItem = entry.getValue();
				if (tradeItem.getSeller().equals(player.getName())) {
					count++;
				}
			}
			if (MySQLUtils.getInteger("SELECT COUNT(*) as COUNT FROM `AuctionStorage` WHERE UPPER (`Name`) = '" + player.getName().toUpperCase() + "';") + count >= AuctionAPI.AUCTION_MAX_SELLS) {
				player.sendMessage(AuctionAPI.PREFIX + "Вы уже разместили или храните максимальное колличество лотов §7(§6" + AuctionAPI.AUCTION_MAX_SELLS + "§7)");
				return false;
			}
			if (!Utils.isDouble(args[0])) {
				if (args.length != 1) {
					player.sendMessage("§l§6• §rИспользование §7- /§6auc §7(§6цена§7)");
				}
				return false;
			}
			Item item = player.getInventory().getItemInHand();
			if (item.getId() == Item.AIR) {
				player.sendMessage(AuctionAPI.PREFIX + "Чтобы выставить предмет на продажу§7, §fвозьмите его в руку§7!");
				return false;
			}
			double price = Double.parseDouble(args[0]);
			if (price <= 0 || price > AuctionAPI.AUCTION_MAX_PRICE) {
				player.sendMessage(AuctionAPI.PREFIX + "Максимальная цена за предмет §7- §6" + AuctionAPI.AUCTION_MAX_PRICE + "");
				return false;
			}
			player.sendMessage(AuctionAPI.PREFIX + "Предмет на продажу §6успешно §fвыставлен за §6" + String.format("%.1f", price) + "§7, §fв колличестве §6" + item.getCount() + " §fшт§7.");
			player.getServer().broadcastMessage(AuctionAPI.PREFIX + "Игрок §6" + player.getName() + " §fвыставил предмет на продажу§7!");
			MySQLUtils.query("INSERT INTO `Auction` (`Seller`, `Price`, `Id`, `Damage`, `Count`, `namedTag`, `Time`) VALUES ('" + player.getName() + "', '" + price + "', '" + item.getId() + "', '" + item.getDamage() + "', '" + item.getCount() + "', '" + Utils.convertNbtToHex(item.getNamedTag()) + "', '" + System.currentTimeMillis() / 1000L + 259200 + "');");
			AuctionAPI.register();
			player.getInventory().setItemInHand(Item.get(Item.AIR));
		}
		return false;
	}
}