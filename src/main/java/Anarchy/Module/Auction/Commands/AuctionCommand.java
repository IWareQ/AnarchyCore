package Anarchy.Module.Auction.Commands;

import java.io.File;
import java.util.Map;

import Anarchy.AnarchyMain;
import Anarchy.Module.Auction.AuctionAPI;
import Anarchy.Module.Auction.Utils.TradeItem;
import Anarchy.Utils.SQLiteUtils;
import Anarchy.Utils.StringUtils;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.item.Item;
import cn.nukkit.level.Sound;
import cn.nukkit.utils.Config;

public class AuctionCommand extends Command {

	public AuctionCommand() {
		super("ah", "§r§fОткрыть Аукцион", "", new String[] {"auc"});
		this.commandParameters.clear();
		this.commandParameters.put("default", new CommandParameter[] {new CommandParameter("money", CommandParamType.INT, false)});
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player)sender;
			if (args.length >= 1) {
				Long cooldownTime = AuctionAPI.AUCTION_COOLDOWN.get(player);
				Long nowTime = System.currentTimeMillis() / 1000L;
				if (cooldownTime != null && cooldownTime > nowTime) {
					player.sendMessage(AuctionAPI.PREFIX + "§fСледующее использование будет доступно через §6" + (cooldownTime - nowTime) + " §fсек§7.");
					return false;
				}
				int count = 0;
				File dataFile = new File(AnarchyMain.folder + "/Auction/PlayerItems/" + player.getName() + ".yml");
				Config config = new Config(dataFile, Config.YAML);
				for (Map.Entry<String, TradeItem> entry : AuctionAPI.AUCTION.entrySet()) {
					TradeItem tradeItem = entry.getValue();
					if (tradeItem.getSellerName().equals(player.getName())) {
						count++;
					}
				}
				if (config.getAll().size() + count > AuctionAPI.AUCTION_MAX_SELLS) {
					player.sendMessage(AuctionAPI.PREFIX + "§fВы уже разместили или храните максимальное колличество лотов §7(§6" + AuctionAPI.AUCTION_MAX_SELLS + "§7)");
					return false;
				}
				if (!StringUtils.isDouble(args[0])) {
					if (args.length != 1) {
						player.sendMessage("§l§6| §r§fИспользование §7- /§6auc §7(§3цена§7)");
						player.getLevel().addSound(player, Sound.MOB_VILLAGER_HAGGLE, 1, 1, player);
					}
					return false;
				}
				Item sellItem = player.getInventory().getItemInHand();
				if (sellItem == null || sellItem.getId() == Item.AIR) {
					player.sendMessage(AuctionAPI.PREFIX + "§fЧтобы выставить предмет на продажу§7, §fвозьмите его в руку");
					return false;
				}
				Double itemPrice = Double.parseDouble(args[0]);
				if (itemPrice <= 0 || itemPrice > AuctionAPI.AUCTION_MAX_PRICE) {
					player.sendMessage(AuctionAPI.PREFIX + "§fМаксимальная цена за предмет §7- §6" + AuctionAPI.AUCTION_MAX_PRICE + "");
					return false;
				}
				String UUID = java.util.UUID.randomUUID().toString();
				SQLiteUtils.query("Auction.db", "INSERT INTO Auction (Sellername, ItemPrice, SellTime, ItemID, ItemDATA, ItemCOUNT, UUID) VALUES (" + player.getName() + ", " + itemPrice + ", " + AuctionAPI.getTradeTime() + ", " + sellItem.getId() +", " + sellItem.getDamage() + ", " + sellItem.getCount() + ", " + UUID +");");
				player.sendMessage(AuctionAPI.PREFIX + "§fПредмет на продажу §6успешно §fвыставлен за §6" + String.format("%.1f", itemPrice) + "§7, §fв колличестве §6" + sellItem.count + " §fшт§7.");
				Server.getInstance().broadcastMessage(AuctionAPI.PREFIX + "§fИгрок §6" + player.getName() + " §fвыставил предмет на продажу§7!");
				//AuctionAPI.AUCTION.put(UUID, new TradeItem(player.getName(), itemPrice, AuctionAPI.getTradeTime(), sellItem, UUID));
				player.getInventory().setItemInHand(Item.get(Item.AIR));
				//AuctionAPI.AUCTION_COOLDOWN.put(player, nowTime + AuctionAPI.AUCTION_ADD_COOLDOWN);
				//AuctionAPI.saveAuction();
			} else {
				AuctionAPI.AUCTION_PAGE.put(player, 0);
				AuctionAPI.showAuction(player, true);
			}
			//player.sendMessage(AuctionAPI.PREFIX + "§fАукцион временно отключен на Тех§7. §fОбслуживание§7!");
		}
		return false;
	}
}