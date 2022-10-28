package ru.iwareq.anarchycore.module.title.cases;

import cn.nukkit.Player;
import cn.nukkit.event.Listener;
import cn.nukkit.scheduler.Task;

public class TimeoutCaseTask extends Task implements Listener {

	private static final int START_SECONDS = 10;

	private final CasesListener listener;
	private final Player player;

	private int seconds = START_SECONDS;

	public TimeoutCaseTask(CasesListener listener, Player player) {
		this.listener = listener;
		this.player = player;
	}

	@Override()
	public void onRun(int currentTick) {
		this.seconds--;

		if (this.seconds <= 0) {
			this.listener.stopOpenCase(this.player);

			this.player.setImmobile(false);
			this.player.sendMessage("Вы слишком долго делали выбор");
		}
	}
}
