package ru.iwareq.anarchycore.module.Commands.EnderChest.Inventory;

import cn.nukkit.Player;
import ru.iwareq.anarchycore.manager.FakeInventory.Inventory.DefaultEnderChest;

public class EnderChest extends DefaultEnderChest {

	public EnderChest() {
		super();
	}

	@Override()
	public void onClose(Player player) {
		super.onClose(player);
		player.getEnderChestInventory().setContents(this.getContents());
	}

	@Override
	public void onOpen(Player player) {
		super.onOpen(player);
		this.setContents(player.getEnderChestInventory().getContents());
	}
}