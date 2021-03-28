package ru.jl1mbo.AnarchyCore.Modules.Commands.Teleport.Task;

import cn.nukkit.scheduler.Task;
import ru.jl1mbo.AnarchyCore.Modules.Commands.Teleport.TeleportAPI;

public class TpaRequestTask extends Task {

	@Override
	public void onRun(int currentTick) {
		TeleportAPI.getTpaRequests().removeIf(tpUtils -> {
			if (tpUtils.isOutdated()) {
				return true;
			}
			return false;
		});
	}
}
