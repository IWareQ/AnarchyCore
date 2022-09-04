package ru.iwareq.anarchycore.module.Cooldown.Utils;

import cn.nukkit.Player;
import ru.iwareq.anarchycore.module.Cooldown.CooldownAPI;

public class CooldownTp {

	private final Player player;
	private final Runnable runnable;
	private final long time;

	public CooldownTp(Player player, Runnable runnable, int seconds) {
		this.player = player;
		this.runnable = runnable;
		this.time = System.currentTimeMillis() / 1000L + seconds;
	}

	public boolean isOutdatedAndExecute() {
		if (this.time <= System.currentTimeMillis() / 1000L) {
			CooldownAPI.removeTask(this.player);
			this.runnable.run();
			return true;
		}

		return false;
	}
}