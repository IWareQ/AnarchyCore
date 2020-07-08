package Anarchy.Module.Auction.Commands;

import java.io.File;
/* import java.io.IOException;
import java.nio.ByteOrder;
import java.util.ArrayList; */
import java.util.Map;

import Anarchy.AnarchyMain;
import Anarchy.Manager.FakeChests.FakeChestsAPI;
import Anarchy.Module.Auction.AuctionAPI;
import Anarchy.Module.Auction.Utils.TradeItem;
import Anarchy.Module.Auction.Utils.Inventory.SellChest;
import Anarchy.Utils.StringUtils;
import FormAPI.Forms.Elements.SimpleForm;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
//import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Config;

public class AuctionCommand extends Command {
	
	public AuctionCommand() {
		super("auc", "Аукцион", "", new String[]{"ah", "trade"});
		commandParameters.clear();
	}
	
	@Override()
	public boolean execute(CommandSender commandSender, String s, String[] strings) {
		if (strings.length >= 1) {
			Long cooldownTime = AuctionAPI.AUCTION_COOLDOWN.get(commandSender);
			Long nowTime = System.currentTimeMillis() / 1000L;
			if (cooldownTime != null && cooldownTime > nowTime) {
				commandSender.sendMessage(AuctionAPI.PREFIX + "§fСледующее использование будет доступно через §c6" + (cooldownTime - nowTime) + " §fсекунд");
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
				commandSender.sendMessage(AuctionAPI.PREFIX + "§fВы достигли лимита§7!\n§l§e| §r§fСнимите товар с продажи или заберите предметы с хранилища");
				return false;
			}
			if (!StringUtils.isInteger(strings[0])) {
				if (strings.length == 1) {
					commandSender.sendMessage("§l§e| §r§fИспользование §7- §e/auc §7(§6цена§7)");
				} else {
					commandSender.sendMessage("§l§e| §r§fИспользование §7- §e/auc §7(§6цена§7) (§6описание§7)");
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
				commandSender.sendMessage(AuctionAPI.PREFIX + "§fМаксимальная цена за предмет §7- §e" + AuctionAPI.AUCTION_MAX_PRICE + "");
				return false;
			}
			commandSender.sendMessage(AuctionAPI.PREFIX + "§fПредмет на продажу §6успешно §fвыставлен§7! (§fx§e" + sellItem.count + "§7) §fза §e" + itemPrice + "");
			Server.getInstance().broadcastMessage(AuctionAPI.PREFIX + "§fИгрок §e" + playerName + " §fвыставил предмет на продажу§7!\n§l§e| §r§fБыстро посмотреть предложение §7- §e/buy");
			String UUID = java.util.UUID.randomUUID().toString();
			AuctionAPI.AUCTION.put(UUID, new TradeItem(sellItem, commandSender.getName(), strings.length > 1 && !StringUtils.implode(strings, 1).trim().equals("") ? StringUtils.implode(strings, 1) : null, itemPrice, AuctionAPI.getTradeTime(), UUID));
			playerInventory.setItemInHand(Item.get(Item.AIR));
			AuctionAPI.AUCTION_COOLDOWN.put(commandSender, nowTime + AuctionAPI.AUCTION_ADD_COOLDOWN);
		} else {
			Player player = (Player)commandSender;
			new SimpleForm("Торговая Площадка", " ").addButton("Торговая Площадка").addButton("Ваши Предметы на Продаже").addButton("Скоро").send(player, (target,form,data)->{
				if (data == -1) return;
				if (data == 0) {
					AuctionAPI.AUCTION_PAGE.put(player, 0);
					AuctionAPI.showAuction(player, true);
				}
				if (data == 1) {
					String playerName = commandSender.getName();
					SellChest sellChest = new SellChest("Ваши Предметы на Продаже");
					for (Map.Entry<String, TradeItem> entry : AuctionAPI.AUCTION.entrySet()) {
						TradeItem tradeItem = entry.getValue();
						if (tradeItem.sellerName.equals(playerName)) {
							Item item = tradeItem.sellItem.clone();
							CompoundTag compoundTag = item.hasCompoundTag() ? item.getNamedTag() : new CompoundTag();
							compoundTag.putString("UUID", tradeItem.UUID);
							item.setNamedTag(compoundTag);
							sellChest.addItem(item.setCustomName("§r§fСтоимость §7- §e" + tradeItem.itemPrice + "\n§r§fДо окончания §7- §6" + (tradeItem.getTime() / 3600) + " §fч§7. §6" + (tradeItem.getTime() / 60 % 60) + " §fмин§7." + (tradeItem.aboutMessage == null ? "" : "\n§r§fОписание §7- §e" + tradeItem.aboutMessage)));
						}
					}
					if (sellChest.isEmpty()) {
						player.sendMessage(AuctionAPI.PREFIX + "§fВы не имеете товаров§7, §fкоторые продаются сейчас§7!");
					} else {
						FakeChestsAPI.openDoubleChestInventory(player, sellChest);
					}
				}
				if (data == 2) {
					/*ShopChest shopChest = new ShopChest("\u041f\u0440\u043e\u0434\u0430\u0436\u0430 \u043f\u0440\u0435\u0434\u043c\u0435\u0442\u043e\u0432");
					shopChest.setItem(0, Item.get(Item.BEDROCK).setCustomName("\u00a7r\u00a7e\u0422\u0435\u0441\u0442\n\n\u00a7r\u00a7e\u2022 \u00a7f\u041d\u0430\u0436\u043c\u0438\u0442\u0435, \u0447\u0442\u043e\u0431\u044b \u043f\u0440\u043e\u0434\u0430\u0442\u044c"));
					FakeChestsAPI.openDoubleChestInventory(player, shopChest);
					player.sendMessage("\u0421\u043a\u043e\u0440\u043e");*/
				}
			});
		}
		return false;
	}
}