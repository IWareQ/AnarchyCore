package Survival.Module.Commands.EnderChest.Utils;

import Survival.Manager.FakeChests.Inventory.DefaultChest;
import cn.nukkit.Player;
import cn.nukkit.level.Sound;

public class EnderChest extends DefaultChest {
	
	public EnderChest(String title) {
		super(title);
	}
	
	@Override()
	public void onClose(Player player) {
		player.getEnderChestInventory().setContents(getContents());
		player.getLevel().addSound(player, Sound.RANDOM_ENDERCHESTCLOSED, 1, 1, player);
		super.onClose(player);
	}
}