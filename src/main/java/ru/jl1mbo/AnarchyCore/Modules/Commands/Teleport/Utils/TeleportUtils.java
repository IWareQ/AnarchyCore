package ru.jl1mbo.AnarchyCore.Modules.Commands.Teleport.Utils;

import cn.nukkit.Player;

public class TeleportUtils {
	private Player player;
	private Player target;
	private Long time;

	public TeleportUtils(Player player, Player target) {
		this.player = player;
		this.target = target;
		this.time = System.currentTimeMillis() / 1000L + 30;
	}

	public Player getPlayer() {
		return this.player;
	}

	public Player getTarget() {
		return this.target;
	}

	public boolean isOutdated() {
		return this.time <= System.currentTimeMillis() / 1000L;
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof TeleportUtils) {
			TeleportUtils tpUtils = (TeleportUtils) object;
			return tpUtils.getPlayer().equals(this.getPlayer()) && tpUtils.getTarget().equals(this.getTarget());
		}
		return false;
	}
}
