package Anarchy.Module.Auction.Commands;

import java.io.File;
import java.util.Map;

import Anarchy.AnarchyMain;
import Anarchy.Module.Auction.AuctionAPI;
import Anarchy.Module.Auction.Utils.TradeItem;
import Anarchy.Utils.StringUtils;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.utils.Config;

public class AuctionCommand extends Command {
	
	public AuctionCommand() {
		super("auc", "Открыть Аукцион", "", new String[]{"ah"});
		commandParameters.clear();
	}
	
	@Override()
	public boolean execute(CommandSender commandSender, String s, String[] strings) {
		if (strings.length >= 1) {
			Long cooldownTime = AuctionAPI.AUCTION_COOLDOWN.get(commandSender);
			Long nowTime = System.currentTimeMillis() / 1000L;
			if (cooldownTime != null && cooldownTime > nowTime) {
				commandSender.sendMessage(AuctionAPI.PREFIX + "§fСледующее использование будет доступно через §c" + (cooldownTime - nowTime) + " §fсек§7.");
				return false;
			}
			int count = 0;
			String playerName = commandSender.getName();
			File dataFile = new File(AnarchyMain.datapath + "/Auction/PlayerItems/" + commandSender.getName() + ".yml");
			Config config = new Config(dataFile, Config.YAML);
			for (Map.Entry<String, TradeItem> entry : AuctionAPI.AUCTION.entrySet()) {
				TradeItem tradeItem = entry.getValue();
				if (tradeItem.sellerName.equals(playerName)) {
					count++;
				}
			}
			if (config.getAll().size() + count > AuctionAPI.AUCTION_MAX_SELLS) {
				commandSender.sendMessage(AuctionAPI.PREFIX + "§fВы достигли лимита§7!\n§l§6| §r§fСнимите товар с продажи или заберите предметы с §3Хранилища");
				return false;
			}
			if (!StringUtils.isInteger(strings[0])) {
				if (strings.length == 1) {
					commandSender.sendMessage("§l§6| §r§fИспользование §7- §6/auc §7(§3цена§7)");
				} else {
					commandSender.sendMessage("§l§6| §r§fИспользование §7- §6/auc §7(§3цена§7) (§3описание§7)");
				}
				return false;
			}
			PlayerInventory playerInventory = ((Player)commandSender).getInventory();
			Item sellItem = playerInventory.getItemInHand();
			if (sellItem == null || sellItem.getId() == Item.AIR) {
				commandSender.sendMessage(AuctionAPI.PREFIX + "§fЧтобы выставить предмет на продажу§7, §fвозьмите его в руку");
				return false;
			}
			int itemPrice = Integer.parseInt(strings[0]);
			if (itemPrice <= 0 || itemPrice > AuctionAPI.AUCTION_MAX_PRICE) {
				commandSender.sendMessage(AuctionAPI.PREFIX + "§fМаксимальная цена за предмет §7- §6" + AuctionAPI.AUCTION_MAX_PRICE + "");
				return false;
			}
			commandSender.sendMessage(AuctionAPI.PREFIX + "§fПредмет на продажу §3успешно §fвыставлен§7! (§fx§6" + sellItem.count + "§7) §fза §6" + itemPrice + "");
			Server.getInstance().broadcastMessage(AuctionAPI.PREFIX + "§fИгрок §6" + playerName + " §fвыставил предмет на продажу§7!");
			String UUID = java.util.UUID.randomUUID().toString();
			AuctionAPI.AUCTION.put(UUID, new TradeItem(sellItem, commandSender.getName(), strings.length > 1 && !StringUtils.implode(strings, 1).trim().equals("") ? StringUtils.implode(strings, 1) : null, itemPrice, AuctionAPI.getTradeTime(), UUID));
			playerInventory.setItemInHand(Item.get(Item.AIR));
			AuctionAPI.AUCTION_COOLDOWN.put(commandSender, nowTime + AuctionAPI.AUCTION_ADD_COOLDOWN);
		} else {
			Player player = (Player)commandSender;
			AuctionAPI.AUCTION_PAGE.put(player, 0);
			AuctionAPI.showAuction(player, true);
		}
		return false;
	}
}