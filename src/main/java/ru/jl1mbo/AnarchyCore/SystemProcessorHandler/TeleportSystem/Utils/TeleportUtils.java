package ru.jl1mbo.AnarchyCore.SystemProcessorHandler.TeleportSystem.Utils;

import cn.nukkit.Player;

public class TeleportUtils {
	private Player player;
	private Player target;
	public int time = 30;

	public TeleportUtils(Player player, Player target) {
		this.player = player;
		this.target = target;
	}

	public Player getPlayer() {
		return player;
	}

	public Player getTarget() {
		return target;
	}

	public boolean isOutdated() {
		return time < 0;
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof TeleportUtils) {
			TeleportUtils teleportUtils = (TeleportUtils) object;
			return teleportUtils.player.equals(player) && teleportUtils.target.equals(target);
		}
		return false;
	}
}
