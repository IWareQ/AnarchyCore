package ru.iwareq.anarchycore.module.Permissions.Group.StaffGroup;

import ru.iwareq.anarchycore.module.Permissions.Group.DefaultGroup;

import java.util.HashMap;

public class AdminGroup extends DefaultGroup {

	@Override
	public String getGroupId() {
		return "admin";
	}

	@Override
	public String getGroupName() {
		return "§7Admin§r";
	}

	@Override
	public Integer getMaxRegions() {
		return 6;
	}

	@Override
	public HashMap<String, Boolean> getPermissionAttachment() {
		HashMap<String, Boolean> permissionAllows = new HashMap<>();
		permissionAllows.put("Command.CraftingTable", true);
		return permissionAllows;
	}

	@Override
	public boolean isAdmin() {
		return true;
	}
}