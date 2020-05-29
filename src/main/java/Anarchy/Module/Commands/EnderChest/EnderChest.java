package Anarchy.Module.Commands.EnderChest;

import Anarchy.Manager.FakeChests.Inventory.DefaultChest;
import cn.nukkit.Player;

public class EnderChest extends DefaultChest {
	EnderChest(String title) {
		super(title);
	}

	@Override
	public void onClose(Player who) {
		who.getEnderChestInventory().setContents(getContents());
		super.onClose(who);
	}
}