package ru.jl1mbo.AnarchyCore.Modules.Permissions.Group.DonateGroup;

import ru.jl1mbo.AnarchyCore.Modules.Permissions.Group.DefaultGroup;

import java.util.HashMap;

public class LordGroup extends DefaultGroup {

	@Override
	public String getGroupId() {
		return "lord";
	}

	@Override
	public String getGroupName() {
		return "§gЛорд§r";
	}

	@Override
	public Integer getMaxRegions() {
		return 3;
	}

	@Override
	public HashMap<String, Boolean> getPermissionAttachment() {
		HashMap<String, Boolean> permissionAllows = new HashMap<>();
		permissionAllows.put("Command.CraftingTable", true);
		permissionAllows.put("Command.Food", true);
		permissionAllows.put("Command.Heal", true);
		permissionAllows.put("Command.ClearInventory", true);
		permissionAllows.put("Command.Day", true);
		return permissionAllows;
	}

	@Override
	public boolean isAdmin() {
		return false;
	}
}