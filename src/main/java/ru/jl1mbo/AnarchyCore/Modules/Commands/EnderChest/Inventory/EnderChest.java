package ru.jl1mbo.AnarchyCore.Modules.Commands.EnderChest.Inventory;

import cn.nukkit.Player;
import ru.jl1mbo.AnarchyCore.Manager.FakeInventory.Inventory.DefaultEnderChest;

public class EnderChest extends DefaultEnderChest {

	public EnderChest() {
		super();
	}
	
	@Override()
	public void onClose(Player player) {
		super.onClose(player);
		player.getEnderChestInventory().setContents(this.getContents());
	}
}