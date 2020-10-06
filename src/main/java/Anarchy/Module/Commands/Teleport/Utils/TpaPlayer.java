package Anarchy.Module.Commands.Teleport.Utils;

import cn.nukkit.Player;

public class TpaPlayer {
	private final Player player;
	private final Player target;
	private final Long time;
	
	public TpaPlayer(Player player, Player target, Long time) {
		this.player = player;
		this.target = target;
		this.time = time;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public Player getTarget() {
		return target;
	}
	
	public Long getTime() {
		return time;
	}
}