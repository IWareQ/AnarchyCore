package ru.iwareq.anarchycore.task;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.scheduler.Task;
import lombok.extern.log4j.Log4j2;
import ru.iwareq.anarchycore.module.Permissions.PermissionAPI;
import ru.iwareq.anarchycore.module.Permissions.RemoveGroup;

@Log4j2
public class GroupRemoveTask extends Task {

	@Override
	public void onRun(int i) {
		RemoveGroup.USERS.forEach((user, time) -> {
			long left = --time;
			if (left < 1) {
				RemoveGroup.USERS.remove(user);

				PermissionAPI.setGroup(user, "player", -1);

				Player target = Server.getInstance().getPlayerExact(user);
				if (target != null) {
					PermissionAPI.updatePermissions(target);
					PermissionAPI.updateNamedTag(target);
				}
			} else {
				RemoveGroup.USERS.put(user, --time);
			}
		});
	}
}
