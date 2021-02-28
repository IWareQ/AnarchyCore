package ru.jl1mbo.AnarchyCore.SystemProcessorHandler.TeleportSystem.Task;

import cn.nukkit.scheduler.Task;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.TeleportSystem.TeleportAPI;

public class TpaRequestTask extends Task {

	@Override
	public void onRun(int tick) {
		TeleportAPI.getTpaRequest().removeIf(teleportUtils -> {
			--teleportUtils.time;
			if (!teleportUtils.isOutdated()) {
				return false;
			}
			return true;
		});
	}
}
