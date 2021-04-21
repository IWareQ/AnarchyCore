package ru.jl1mbo.AnarchyCore.Modules.Permissions.Group.DonateGroup;

import ru.jl1mbo.AnarchyCore.Modules.Permissions.Group.DefaultGroup;

import java.util.HashMap;

public class HeroGroup extends DefaultGroup {

	@Override
	public String getGroupId() {
		return "hero";
	}

	@Override
	public String getGroupName() {
		return "§eГерой§r";
	}

	@Override
	public Integer getMaxRegions() {
		return 2;
	}

	@Override
	public HashMap<String, Boolean> getPermissionAttachment() {
		HashMap<String, Boolean> permissionAllows = new HashMap<>();
		permissionAllows.put("Command.CraftingTable", true);
		return permissionAllows;
	}

	@Override
	public boolean isAdmin() {
		return false;
	}
}