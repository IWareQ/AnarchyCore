package Anarchy.Module.Commands.Check;

import Anarchy.Manager.FakeChests.Inventory.DefaultChest;
import cn.nukkit.Player;

public class EnderChest extends DefaultChest {
	
	EnderChest(String title) {
		super(title);
	}
	
	@Override()
	public void onClose(Player player) {
		player.getEnderChestInventory().setContents(getContents());
		super.onClose(player);
	}
}