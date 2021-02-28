package ru.jl1mbo.AnarchyCore.SystemProcessorHandler.Spectate.Task;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Position;
import cn.nukkit.scheduler.Task;
import cn.nukkit.utils.Config;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.Spectate.SpectateAPI;

public class SpectateTask extends Task {
	private static Config config = SpectateAPI.config;

	@Override()
	public void onRun(int tick) {
		Server.getInstance().getOnlinePlayers().values().forEach(player -> {
			if (SpectateAPI.isSpectate(player.getName())) {
				Player target = Server.getInstance().getPlayer(config.getString(player.getName().toLowerCase() + ".Spectate"));
				if (target != null) {
					if (player.distance(target) >= 35) {
						player.sendMessage(SpectateAPI.PREFIX + "Наблюдаемый слишком §6далеко§7, §fтелепортируем§7!");
						player.teleport(new Position(target.getFloorX() + 0.5, target.getFloorY(), target.getFloorZ() + 0.5));
					}
				} else {
					SpectateAPI.removeSpectate(player);
				}
			}
		});
	}
}