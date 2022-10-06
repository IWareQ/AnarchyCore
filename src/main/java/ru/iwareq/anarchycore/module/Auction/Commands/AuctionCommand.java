package ru.iwareq.anarchycore.module.Auction.Commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandEnum;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.item.Item;
import ru.iwareq.anarchycore.module.Auction.Auction;
import ru.iwareq.anarchycore.module.Auction.Utils.TradeItem;
import ru.iwareq.anarchycore.util.Utils;

public class AuctionCommand extends Command {

	public AuctionCommand() {
		super("auction", "§rОткрыть Аукцион", "", new String[]{"auc", "ah"});
		this.commandParameters.clear();
		this.commandParameters.put("auction", new CommandParameter[]{
				CommandParameter.newEnum("action", new CommandEnum("AuctionAction", "sell")),
				CommandParameter.newType("money", true, CommandParamType.FLOAT)
		});
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length == 0) {
				Auction.showAuction(player, 1);
				return true;
			}

			if (!Auction.canTrade(player)) {
				player.sendMessage(Auction.PREFIX + "Вы уже разместили или храните максимальное колличество лотов §7(§6" + Auction.MAX_SELLS + "§7)");
				return false;
			}

			if (!Utils.isDouble(args[1])) {
				if (args.length != 2) {
					player.sendMessage("§l§6• §rИспользование §7- /§6ah sell §7(§6цена§7)");
				}

				return false;
			}

			Item item = player.getInventory().getItemInHand();
			if (item.getId() == Item.AIR) {
				player.sendMessage(Auction.PREFIX + "Чтобы выставить предмет на продажу§7, §fвозьмите его в руку§7!");
				return false;
			}

			double price = Double.parseDouble(args[1]);
			if (price < Auction.MIN_PRICE || price > Auction.MAX_PRICE) {
				player.sendMessage(Auction.PREFIX + "Максимальная цена за предмет §7- §6" + Auction.MAX_PRICE + "");
				player.sendMessage(Auction.PREFIX + "Минимальная цена за предмет §7- §6" + Auction.MIN_PRICE + "");
				return false;
			}

			player.sendMessage(Auction.PREFIX + "Предмет на продажу §6успешно §fвыставлен за §6" + price + "§7, §fв колличестве §6" + item.getCount() + " §fшт§7.");
			player.getServer().broadcastMessage(Auction.PREFIX + "Игрок §6" + player.getName() + " §fвыставил предмет на продажу§7!");
			Auction.addItem(new TradeItem(player.getName(), item, price, System.currentTimeMillis() / 1000L + 259200));
			player.getInventory().setItemInHand(Item.get(Item.AIR));
		}

		return false;
	}
}
