package Anarchy.Module.Commands.Check;

import Anarchy.Manager.FakeChests.Inventory.DoubleDefaultChest;
import cn.nukkit.Player;

public class InventoryChest extends DoubleDefaultChest {
	
	InventoryChest(String title) {
		super(title);
	}
	
	@Override()
	public void onClose(Player who) {
		who.getInventory().setContents(getContents());
		super.onClose(who);
	}
}