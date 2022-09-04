package ru.iwareq.anarchycore.module.Cooldown.Task;

import cn.nukkit.scheduler.Task;
import ru.iwareq.anarchycore.module.Cooldown.CooldownAPI;
import ru.iwareq.anarchycore.module.Cooldown.Utils.Cooldown;
import ru.iwareq.anarchycore.module.Cooldown.Utils.CooldownTp;

public class CooldownRemoveTask extends Task {

	@Override
	public void onRun(int currentTick) {
		CooldownAPI.getCooldowns().removeIf(Cooldown::isOutdated);

		CooldownAPI.getCooldownsTp().values().forEach(CooldownTp::isOutdatedAndExecute);
	}
}