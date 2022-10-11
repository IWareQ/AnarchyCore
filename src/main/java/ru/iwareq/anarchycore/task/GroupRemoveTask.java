package ru.iwareq.anarchycore.task;

import cn.nukkit.scheduler.Task;
import lombok.extern.log4j.Log4j2;
import ru.iwareq.anarchycore.module.Permissions.RemoveGroup;

@Log4j2
public class GroupRemoveTask extends Task {

	@Override
	public void onRun(int i) {
		RemoveGroup.USERS.forEach((user, time) -> {
			RemoveGroup.USERS.put(user, --time);
		});
	}
}
