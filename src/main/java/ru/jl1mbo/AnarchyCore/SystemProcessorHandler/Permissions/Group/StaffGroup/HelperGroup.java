package ru.jl1mbo.AnarchyCore.SystemProcessorHandler.Permissions.Group.StaffGroup;

import java.util.HashMap;

import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.Permissions.Group.DefaultGroup;

public class HelperGroup extends DefaultGroup {

	@Override
	public String getGroupId() {
		return "helper";
	}

	@Override
	public String getGroupName() {
		return "§bПомощьник§r";
	}

	@Override
	public Integer getMaxRegions() {
		return 5;
	}

	@Override
	public HashMap<String, Boolean> getPermissionAttachment() {
		HashMap<String, Boolean> permissionAllows = new HashMap<>();
		permissionAllows.put("Command.CraftingTable", true);
		permissionAllows.put("Command.Food", true);
		permissionAllows.put("Command.Heal", true);
		permissionAllows.put("Command.ClearInventory", true);
		permissionAllows.put("Command.Day", true);
		permissionAllows.put("Command.Night", true);
		permissionAllows.put("Command.Repair", true);
		permissionAllows.put("Command.ClearChat", true);
		permissionAllows.put("AdminChat", true);
		permissionAllows.put("Command.Mute", true);
		permissionAllows.put("Command.UnMute", true);
		return permissionAllows;
	}
}