package ru.iwareq.anarchycore.module.Permissions.Group.DonateGroup;

import ru.iwareq.anarchycore.module.Permissions.Group.DefaultGroup;

import java.util.HashMap;

public class GuardianGroup extends DefaultGroup {

	@Override
	public String getGroupId() {
		return "guardian";
	}

	@Override
	public String getGroupName() {
		return "§3Страж§r";
	}

	@Override
	public Integer getMaxRegions() {
		return 2;
	}

	@Override
	public HashMap<String, Boolean> getPermissionAttachment() {
		HashMap<String, Boolean> permissionAllows = new HashMap<>();
		permissionAllows.put("Command.CraftingTable", true);
		permissionAllows.put("Command.Food", true);
		return permissionAllows;
	}

	@Override
	public boolean isAdmin() {
		return false;
	}
}