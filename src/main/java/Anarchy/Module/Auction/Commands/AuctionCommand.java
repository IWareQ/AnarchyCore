package Anarchy.Module.Auction.Commands;

import java.io.File;
import java.io.IOException;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Map;

import Anarchy.AnarchyMain;
import Anarchy.Manager.FakeChests.FakeChestsAPI;
import Anarchy.Module.Auction.AuctionAPI;
import Anarchy.Module.Auction.Utils.TradeItem;
import Anarchy.Module.Auction.Utils.Inventory.SellChest;
import Anarchy.Module.Auction.Utils.Inventory.TakeChest;
import Anarchy.Utils.StringUtils;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Config;
import ru.nukkitx.forms.elements.SimpleForm;

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
			Long cooldownTime = AuctionAPI.AUCTION_COOLDOWN.get(commandSender),
			nowTime = System.currentTimeMillis() / 1000L;
			if (cooldownTime != null && cooldownTime > nowTime) {
				commandSender.sendMessage(AuctionAPI.PREFIX + "§fСледующее использование будет доступно через §c" + (cooldownTime - nowTime) + " §fсек.");
				return false;
			}

			int count = 0;
			String playerName = commandSender.getName();
			File dataFile = new File(AnarchyMain.datapath + "/Auction/PlayerItems/" + commandSender.getName() + ".yml");
			Config config = new Config(dataFile, Config.YAML);
			for (Map.Entry < String, TradeItem > entry: AuctionAPI.AUCTION.entrySet()) {
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
					commandSender.sendMessage("§l§e| §r§fИспользование §7- §e/auc <цена>");
				} else {
					commandSender.sendMessage("§l§e| §r§fИспользование §7- §e/auc <цена> <описание>");
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
			if (itemPrice <= 0 || itemPrice > AuctionAPI.AUCTION_MAX_PRICE) {
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
			Player player = (Player) commandSender;
			new SimpleForm("Аукцион", "Текст").addButton("Аукцион").addButton("Выставленные товары").addButton("Хранилище").send(player, (target, form, data) -> {
				if (data == -1) return;
				if (data == 0) {
					AuctionAPI.AUCTION_PAGE.put(player, 0);
					AuctionAPI.showAuction(player, true);
				}
				if (data == 1) {
					String playerName = commandSender.getName();
					SellChest sellChest = new SellChest("Выставленные товары");
					for (Map.Entry < String, TradeItem > entry: AuctionAPI.AUCTION.entrySet()) {
						TradeItem tradeItem = entry.getValue();
						if (tradeItem.sellerName.equals(playerName)) {
							Item item = tradeItem.sellItem.clone();
							CompoundTag compoundTag = item.hasCompoundTag() ? item.getNamedTag() : new CompoundTag();
							compoundTag.putString("UUID", tradeItem.UUID);
							item.setNamedTag(compoundTag);
							sellChest.addItem(item.setCustomName("§r§fЦена: §e" + tradeItem.itemPrice + "\n§fИстекает через: §c" + (tradeItem.getTime() / 3600) + " §fч. §c" + (tradeItem.getTime() / 60 % 60) + " §fмин." + (tradeItem.aboutMessage == null ? "": "\n§fОписание: §f" + tradeItem.aboutMessage)));
						}
					}

					if (sellChest.isEmpty()) {
						player.sendMessage(AuctionAPI.PREFIX + "§fВы не имеете товаров, которые продаются сейчас!");
					} else {
						FakeChestsAPI.openDoubleChestInventory(player, sellChest);
					}
				}
				if (data == 2) {
					String playerName = commandSender.getName();
					File dataFile = new File(AnarchyMain.datapath + "/Auction/PlayerItems/" + playerName + ".yml");
					Config config = new Config(dataFile, Config.YAML);
					if (!dataFile.exists()) {
						player.sendMessage(AuctionAPI.PREFIX + "§fВ Хранилище пусто!");
						return;
					} else {
						if (config.getAll().isEmpty()) {
							dataFile.delete();
						}
					}

					TakeChest takeChest = new TakeChest("Хранилище", dataFile);
					for (Map.Entry <String, Object> entry: config.getAll().entrySet()) {
						ArrayList <Object> itemData = (ArrayList <Object>) entry.getValue();
						Item item = Item.get((int) itemData.get(0), (int) itemData.get(1), (int) itemData.get(2));
						CompoundTag compoundTag = null;
						if (itemData.size() > 3) {
							try {
								compoundTag = NBTIO.read((byte[]) itemData.get(3), ByteOrder.LITTLE_ENDIAN);
							} catch(IOException e) {
								Server.getInstance().getLogger().alert("error: " + e);
							}
						}
						if (compoundTag == null) {
							compoundTag = new CompoundTag();
						}
						compoundTag.putString("UUID", entry.getKey());
						item.setNamedTag(compoundTag);
						takeChest.addItem(item);
					}
					FakeChestsAPI.openDoubleChestInventory(player, takeChest);
				}
			});
		}
		return false;
	}
}