package ru.iwareq.anarchycore.module.Commands.Teleport.Task;

import cn.nukkit.scheduler.Task;
import ru.iwareq.anarchycore.module.Commands.Teleport.Utils.TeleportUtils;
import ru.iwareq.anarchycore.module.Commands.Teleport.TeleportAPI;

public class TpaRequestTask extends Task {

	@Override
	public void onRun(int currentTick) {
		TeleportAPI.getTpaRequests().removeIf(TeleportUtils::isOutdated);
	}
}
