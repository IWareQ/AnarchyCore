package Anarchy.Module.Commands.Check;

import Anarchy.Manager.FakeChests.Inventory.DoubleDefaultChest;
import cn.nukkit.Player;

public class InventoryChest extends DoubleDefaultChest {
	
	InventoryChest(String title) {
		super(title);
	}
	
	@Override()
	public void onClose(Player player) {
		player.getInventory().setContents(getContents());
		super.onClose(player);
	}
}