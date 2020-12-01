package Anarchy.Task;

import Anarchy.AnarchyMain;
import Anarchy.Module.Commands.Spectate.SpectateAPI;
import Anarchy.Module.Commands.Spectate.Utils.SpectatePlayer;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.math.Vector3;
import cn.nukkit.scheduler.Task;

public class SecondTask extends Task {

	@Override()
	public void onRun(int currentTick) {
		for (Player player : Server.getInstance().getOnlinePlayers().values()) {
			if (SpectateAPI.SPECTATE_PLAYERS.containsKey(player.getName())) {
				SpectatePlayer spectatePlayer = SpectateAPI.SPECTATE_PLAYERS.get(player.getName());
				Player target = Server.getInstance().getPlayer(spectatePlayer.getName());
				if ((int)player.distance(target) >= 21) {
					player.sendMessage(AnarchyMain.PREFIX + "§fВы не можете отлететь дальше §620 §fблоков от Наблюдаемого§7!\n§l§6• §r§fДля окончания наблюдения возьмите в руку §6Редстоун§7!");
					player.teleport(new Vector3(target.getFloorX(), target.getFloorY(), target.getFloorZ()));
				}
			}
		}
	}
}