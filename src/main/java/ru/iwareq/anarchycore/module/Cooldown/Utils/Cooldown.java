package ru.iwareq.anarchycore.module.Cooldown.Utils;

import cn.nukkit.Player;

public class Cooldown {

	private final Player player;
	private final String command;
	private final long time;

	public Cooldown(Player player, String command, int seconds) {
		this.player = player;
		this.command = command;
		this.time = System.currentTimeMillis() / 1000L + seconds;
	}

	public Player getPlayer() {
		return this.player;
	}

	public String getCommand() {
		return this.command;
	}

	public long getTime() {
		return this.time - System.currentTimeMillis() / 1000L;
	}

	public boolean isOutdated() {
		return this.time <= System.currentTimeMillis() / 1000L;
	}
}