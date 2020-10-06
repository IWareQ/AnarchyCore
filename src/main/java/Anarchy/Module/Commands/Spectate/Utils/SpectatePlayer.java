package Anarchy.Module.Commands.Spectate.Utils;

import cn.nukkit.Player;
import cn.nukkit.level.Position;

public class SpectatePlayer {
	public String spectateName;
	public Position playerPosition;

	public SpectatePlayer(Player player, Player target) {
		this.spectateName = target.getName();
		this.playerPosition = player.getPosition();
	}
	public String getSpectateName() {
		return spectateName;
	}
	public Position getPosition() {
		return playerPosition;
	}
}