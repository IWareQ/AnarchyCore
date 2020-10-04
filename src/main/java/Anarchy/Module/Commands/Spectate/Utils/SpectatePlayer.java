package Anarchy.Module.Commands.Spectate.Utils;

import cn.nukkit.Player;
import cn.nukkit.level.Position;

public class SpectatePlayer {
	public String spectateName;
	public Position playerPosition;
	
	public SpectatePlayer(Player player, Player spectatePlayer) {
		this.spectateName = spectatePlayer.getName();
		this.playerPosition = player.getPosition();
	}
}