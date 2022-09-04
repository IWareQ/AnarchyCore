package ru.iwareq.anarchycore.module.Auction.Utils.Inventory;

import cn.nukkit.item.Item;
import ru.iwareq.anarchycore.manager.FakeInventory.Inventory.DefaultDoubleChest;

public class AuctionStorageChest extends DefaultDoubleChest {

	public AuctionStorageChest(String title) {
		super(title);

		this.setItem(49, Item.get(Item.COMMAND_BLOCK_MINECART)
				.setCustomName("§r§6Назад")
				.setLore("\n§r§l§6• §rНажмите§7, §fчтобы вернуться назад§7!"));
	}
}