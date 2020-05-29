package Anarchy.Module.Auction.Commands;

import Anarchy.AnarchyMain;
import Anarchy.Module.Auction.AuctionAPI;
import Anarchy.Module.Auction.Utils.Form.SimpleTradeForm;
import Anarchy.Module.Auction.Utils.TradeItem;
import Anarchy.Utils.StringUtils;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.utils.Config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AuctionCommand extends Command {
	public AuctionCommand() {
		super("auc", "Аукцион", "", new String[] {
			"ah",
			"trade"
		});
		commandParameters.clear();
	}

	@Override
	public boolean execute(CommandSender commandSender, String s, String[] strings) {
		if (strings.length >= 1) {
			Long cooldownTime = AuctionAPI.AUCTION_COOLDOWN.get(commandSender), nowTime = System.currentTimeMillis() / 1000L;
			if (cooldownTime != null && cooldownTime > nowTime) {
				commandSender.sendMessage(AuctionAPI.PREFIX + "§fСледующее использование будет доступно через §c" + (cooldownTime - nowTime) + " §fсек.");
				return false;
			}

			int count = 0;
			String playerName = commandSender.getName();
			File dataFile = new File(AnarchyMain.datapath + "/Auction/PlayerItems/" + commandSender.getName() + ".yml");
			Config config = new Config(dataFile, Config.YAML);
			for (Map.Entry<String, TradeItem> entry: AuctionAPI.AUCTION.entrySet()) {
				TradeItem tradeItem = entry.getValue();
				if (tradeItem.sellerName.equals(playerName)) {
					count++;
				}
			}

			if (config.getAll().size() + count > AuctionAPI.AUCTION_MAX_SELLS) {
				commandSender.sendMessage(AuctionAPI.PREFIX + "§fВы достигли лимита! Снимите товар с продажи или заберите предметы с хранилища");
				return false;
			}

			if (!StringUtils.isInteger(strings[0])) {
				if (strings.length == 1) {
					commandSender.sendMessage("§l§e| §fИспользование §7- §e/auc <цена>");
				} else {
					commandSender.sendMessage("§l§e| §fИспользование §7- §e/auc <цена> <описание>");
				}
				return false;
			}

			PlayerInventory playerInventory = ((Player) commandSender).getInventory();
			Item sellItem = playerInventory.getItemInHand();
			if (sellItem == null || sellItem.getId() == Item.AIR) {
				commandSender.sendMessage(AuctionAPI.PREFIX + "§fВозьмите в руку предмет для продажи!");
				return false;
			}

			int itemPrice = Integer.parseInt(strings[0]);
			if (itemPrice<= 0 || itemPrice > AuctionAPI.AUCTION_MAX_PRICE) {
				commandSender.sendMessage(AuctionAPI.PREFIX + "§fМаксимальная цена за предмет §7- §e" + AuctionAPI.AUCTION_MAX_PRICE + "");
				return false;
			}

			commandSender.sendMessage(AuctionAPI.PREFIX + "§fВы выставили предмет на продажу! §7(§ex" + sellItem.count + "§7) §fза §e" + itemPrice + "");
			Server.getInstance().broadcastMessage(AuctionAPI.PREFIX + "§fИгрок §a" + playerName + " §fвыставил предмет на продажу!\n§l§e| §r§fБыстро посмотреть предложение §7- §c/buy");
			String UUID = java.util.UUID.randomUUID().toString();
			AuctionAPI.AUCTION.put(UUID, new TradeItem(sellItem, commandSender.getName(), strings.length > 1 && !StringUtils.implode(strings, 1).trim().equals("") ? StringUtils.implode(strings, 1) : null, itemPrice, AuctionAPI.getTradeTime(), UUID));
			playerInventory.setItemInHand(Item.get(Item.AIR));
			AuctionAPI.AUCTION_COOLDOWN.put(commandSender, nowTime + AuctionAPI.AUCTION_ADD_COOLDOWN);
		} else {
			List<ElementButton> formButtons = new ArrayList<>();
			formButtons.add(new ElementButton("Аукцион"));
			formButtons.add(new ElementButton("Выставленные товары"));
			formButtons.add(new ElementButton("Хранилище"));
			((Player) commandSender).showFormWindow(new SimpleTradeForm("Аукцион > Выберите Категорию", "§eТЕКСТ ЕЩЕ НЕ ПРИДУМАЛ", formButtons));
		}
		return false;
	}
}