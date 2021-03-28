package ru.jl1mbo.AnarchyCore.Modules.Permissions.Group;

import java.util.HashMap;

public class PlayerGroup extends DefaultGroup {

	@Override
	public String getGroupId() {
		return "player";
	}

	@Override
	public String getGroupName() {
		return "§7Игрок§r";
	}

	@Override
	public Integer getMaxRegions() {
		return 2;
	}

	@Override
	public HashMap<String, Boolean> getPermissionAttachment() {
		HashMap<String, Boolean> permissionAllows = new HashMap<>();
		return permissionAllows;
	}

	@Override
	public boolean isAdmin() {
		return false;
	}
}