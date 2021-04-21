package ru.jl1mbo.AnarchyCore.Modules.AdminSystem.Task;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Position;
import cn.nukkit.scheduler.Task;
import ru.jl1mbo.AnarchyCore.Modules.AdminSystem.AdminAPI;

import java.util.Map;

public class SpectateTask extends Task {

	@Override
	public void onRun(int currentTick) {
		for (Player player : Server.getInstance().getOnlinePlayers().values()) {
			if (AdminAPI.isSpectate(player.getName())) {
				Map<String, String> spectateData = AdminAPI.getSpectateData(player.getName());
				Player target = Server.getInstance().getPlayer(spectateData.get("Target"));
				if (target != null) {
					if (player.distance(target) >= 35) {
						player.sendMessage(AdminAPI.PREFIX + "Наблюдаемый слишком §6далеко§7, §fтелепортируем§7!");
						player.teleport(new Position(target.getFloorX() + 0.5, target.getFloorY(), target.getFloorZ() + 0.5));
					}
				} else {
					AdminAPI.removeSpectate(player);
				}
			}
		}
	}
}