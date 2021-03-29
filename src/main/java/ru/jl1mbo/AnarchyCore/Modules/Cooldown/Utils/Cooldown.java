package ru.jl1mbo.AnarchyCore.Modules.Cooldown.Utils;

import cn.nukkit.Player;

public class Cooldown {
	private Player player;
	private String command;
	private long time;

	public Cooldown(Player player, String command, Integer seconds) {
		this.player = player;
		this.command = command;
		this.time = System.currentTimeMillis() / 1000L + seconds;
	}

	public Player getPlayer() {
		return player;
	}

	public String getCommand() {
		return command;
	}

	public long getTime() {
		return time;
	}

	public boolean isOutdated() {
		return time <= System.currentTimeMillis() / 1000L;
	}
}