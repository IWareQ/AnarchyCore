package Anarchy.Manager.FakeChests.Inventory;

import cn.nukkit.inventory.InventoryType;

public class DefaultHopper extends DefaultChest {
	public DefaultHopper() {
		super(InventoryType.HOPPER, null);
	}

	public DefaultHopper(String title) {
		super(InventoryType.HOPPER, title);
	}
}