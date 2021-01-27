package AnarchySystem.Components.Commands.EnderChest.Inventory;

import AnarchySystem.Manager.FakeInventory.Inventory.DefaultEnderChest;
import cn.nukkit.Player;

public class EnderChest extends DefaultEnderChest {
	public EnderChest() {
		super();
	}

	@Override
	public void onClose(Player player) {
		player.getEnderChestInventory().setContents(this.getContents());
		super.onClose(player);
	}
}