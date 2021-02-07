package ru.jl1mbo.AnarchyCore.SystemProcessorHandler.Permissions.Group.DonateGroup;

import java.util.HashMap;

import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.Permissions.Group.DefaultGroup;

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
}