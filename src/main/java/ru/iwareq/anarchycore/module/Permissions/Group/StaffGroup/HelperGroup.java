package ru.iwareq.anarchycore.module.Permissions.Group.StaffGroup;

import ru.iwareq.anarchycore.module.Permissions.Group.DefaultGroup;

import java.util.HashMap;

public class HelperGroup extends DefaultGroup {

	@Override
	public String getGroupId() {
		return "helper";
	}

	@Override
	public String getGroupName() {
		return "§7Helper§r";
	}

	@Override
	public Integer getMaxRegions() {
		return 5;
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