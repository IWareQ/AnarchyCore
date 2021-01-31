package ru.jl1mbo.AnarchyCore.GameHandler.Achievements.Task;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.item.Item;
import cn.nukkit.scheduler.Task;

public class AmmunitionTask extends Task {

	@Override
	public void onRun(int currentTick) {
		for (Player player : Server.getInstance().getOnlinePlayers().values()) {
			if (player.getInventory().getHelmet().getId() != Item.AIR && player.getInventory().getChestplate() != Item.get(Item.AIR) && player.getInventory().getLeggings() != Item.get(Item.AIR)  && player.getInventory().getBoots() != Item.get(Item.AIR)) {
				player.sendPopup("test: " + player.getInventory().getHelmet().getId());
			}
		}
	}
}