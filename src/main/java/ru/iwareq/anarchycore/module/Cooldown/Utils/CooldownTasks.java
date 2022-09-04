package ru.iwareq.anarchycore.module.Cooldown.Utils;

import cn.nukkit.Player;

public class CooldownTasks {

	private final Player player;
	private final Runnable runnable;
	private final long time;

	public CooldownTasks(Player player, Runnable runnable, int seconds) {
		this.player = player;
		this.runnable = runnable;
		this.time = System.currentTimeMillis() / 1000L + seconds;
	}

	public Player getPlayer() {
		return this.player;
	}

	public void execute() {
		this.runnable.run();
	}

	public long getTime() {
		return this.time;
	}

	public boolean isOutdated() {
		return this.time <= System.currentTimeMillis() / 1000L;
	}
}