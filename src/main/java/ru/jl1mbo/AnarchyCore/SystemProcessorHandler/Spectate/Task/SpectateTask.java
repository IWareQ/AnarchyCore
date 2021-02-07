package ru.jl1mbo.AnarchyCore.SystemProcessorHandler.Spectate.Task;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Position;
import cn.nukkit.scheduler.Task;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.Spectate.SpectateAPI;

public class SpectateTask extends Task {

	@Override()
	public void onRun(int currentTick) {
		for (Player player : Server.getInstance().getOnlinePlayers().values()) {
			if (SpectateAPI.isSpectate(player.getName())) {
				Player target = Server.getInstance().getPlayer(SpectateAPI.config.getString(player.getName().toLowerCase() + ".Spectate"));
				if (target != null) {
					if (player.distance(target) >= 35) {
						player.sendMessage(SpectateAPI.PREFIX +
										   "§fВы не можете отлететь дальше §635 §fблоков от Наблюдаемого§7!\n§l§6• §r§fДля окончания наблюдения возьмите в руку §6Редстоун§7!");
						player.teleport(new Position(target.getFloorX(), target.getFloorY(), target.getFloorZ()));
					}
				} else {
					SpectateAPI.removeSpectate(player);
				}
			}
		}
	}
}