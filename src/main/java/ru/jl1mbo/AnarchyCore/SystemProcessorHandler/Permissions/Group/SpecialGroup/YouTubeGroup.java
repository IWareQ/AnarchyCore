package ru.jl1mbo.AnarchyCore.SystemProcessorHandler.Permissions.Group.SpecialGroup;

import java.util.HashMap;

import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.Permissions.Group.DefaultGroup;

public class YouTubeGroup extends DefaultGroup {

	@Override
	public String getGroupId() {
		return "youtube";
	}

	@Override
	public String getGroupName() {
		return "§cYou§fTube§r";
	}

	@Override
	public Integer getMaxRegions() {
		return 4;
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
		permissionAllows.put("Command.Repair.All", true);
		permissionAllows.put("Command.EnderChest", true);
		return permissionAllows;
	}
}