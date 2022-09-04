package ru.iwareq.anarchycore.module.Commands.Teleport.Utils;

import cn.nukkit.Player;

public class TeleportUtils {

	private final Player player;
	private final Player target;
	private final Long time;

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
			TeleportUtils teleportUtils = (TeleportUtils) object;
			return teleportUtils.getPlayer().equals(this.getPlayer()) && teleportUtils.getTarget().equals(this.getTarget());
		}
		return false;
	}
}
