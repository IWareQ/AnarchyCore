package ru.jl1mbo.AnarchyCore.Modules.Commands.Teleport.Task;

import cn.nukkit.scheduler.Task;
import ru.jl1mbo.AnarchyCore.Modules.Commands.Teleport.TeleportAPI;
import ru.jl1mbo.AnarchyCore.Modules.Commands.Teleport.Utils.TeleportUtils;

public class TpaRequestTask extends Task {

	@Override
	public void onRun(int currentTick) {
		TeleportAPI.getTpaRequests().removeIf(TeleportUtils::isOutdated);
	}
}
