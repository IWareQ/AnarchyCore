package ru.jl1mbo.AnarchyCore.GameHandler.Achievements.Task;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.scheduler.Task;
import ru.jl1mbo.AnarchyCore.GameHandler.Achievements.AchievementsAPI;

public class AmmunitionTask extends Task {

	@Override
	public void onRun(int tick) {
		for (Player player : Server.getInstance().getOnlinePlayers().values()) {
			if (player.getInventory().getHelmet().getId() != Item.AIR && player.getInventory().getChestplate().getId() != Item.AIR && player.getInventory().getLeggings().getId() != Item.AIR
					&& player.getInventory().getBoots().getId() != Item.AIR) {
				AchievementsAPI.addPoints(player, "ammunition", 1);
			}
		}
	}
}