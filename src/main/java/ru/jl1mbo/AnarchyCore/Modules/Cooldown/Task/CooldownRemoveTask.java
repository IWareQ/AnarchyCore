package ru.jl1mbo.AnarchyCore.Modules.Cooldown.Task;

import cn.nukkit.scheduler.Task;
import ru.jl1mbo.AnarchyCore.Modules.Cooldown.CooldownAPI;

public class CooldownRemoveTask extends Task {

	@Override
	public void onRun(int currentTick) {
		CooldownAPI.getCooldowns().removeIf(cooldown -> {
			return cooldown.isOutdated();
		});
	}
}