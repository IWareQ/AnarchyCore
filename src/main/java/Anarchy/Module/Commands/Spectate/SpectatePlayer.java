package Anarchy.Module.Commands.Spectate;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.level.Position;

import java.util.Map;

public class SpectatePlayer {
	public String spectateName;
	public Position playerPosition;
	public Map<Integer, Item> playerInventory;

	public SpectatePlayer(Player player, Player spectatePlayer) {
		this.spectateName = spectatePlayer.getName();
		this.playerPosition = player.getPosition();
		this.playerInventory = player.getInventory().getContents();
	}
}