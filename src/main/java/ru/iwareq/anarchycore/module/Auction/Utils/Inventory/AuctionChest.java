package ru.iwareq.anarchycore.module.Auction.Utils.Inventory;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import ru.iwareq.anarchycore.manager.FakeInventory.Inventory.DefaultDoubleChest;
import ru.iwareq.anarchycore.module.Auction.Auction;

public class AuctionChest extends DefaultDoubleChest {

	private final int page;

	public AuctionChest(int page) {
		super("§l§fТорговая площадка §7(§6" + page + "§7/§6" + Auction.getPagesCount() + "§7)");

		this.page = page;
	}

	@Override
	public void onOpen(Player player) {
		super.onOpen(player);

		this.setItem(48, Item.get(Item.MINECART_WITH_CHEST)
				.setCustomName("§r§6Хранилище")
				.setLore("\n§r§l§6• §rНажмите§7, §fчтобы перейти§7!"));


		this.setItem(50, Item.get(Item.SIGN)
				.setCustomName("§r§6Справка")
				.setLore("\n§rЭто торговая площадка§7, §fкоторая создана\nдля покупки и продажи предметов§7." +
						"\n\n§fТорговая площадка также является\nотличным способом заработать §6Монет§7, §fпродавая\nфермерские товары§7, §fкоторые могут\nзаинтересовать других Игроков§7." +
						"\n\n§rЧтобы выставить предмет на продажу§7,\n§fвозьмите его в руку и введите\n§6/auc §7(§6цена§7)"));

		if (page != 1) {
			this.setItem(45, Item.get(Item.PAPER)
					.setCustomName("§r§6Листнуть назад")
					.setLore("\n§r§l§6• §rНажмите§7, §fчтобы перейти§7!"));

			this.setItem(53, Item.get(Item.PAPER)
					.setCustomName("§r§6Листнуть вперед")
					.setLore("\n§r§l§6• §rНажмите§7, §fчтобы перейти§7!"));
		}
	}

	@Override
	public void onClose(Player player) {
		super.onClose(player);

		Auction.PLAYER_AUCTION.remove(player);
	}
}