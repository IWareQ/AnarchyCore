package ru.jl1mbo.AnarchyCore.Modules.Permissions.Group.StaffGroup;

import java.util.HashMap;

import ru.jl1mbo.AnarchyCore.Modules.Permissions.Group.DefaultGroup;

public class ModeratorGroup extends DefaultGroup {

	@Override
	public String getGroupId() {
		return "moderator";
	}

	@Override
	public String getGroupName() {
		return "§aМодератор§r";
	}

	@Override
	public Integer getMaxRegions() {
		return 6;
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
		permissionAllows.put("Command.NightVision", true);
		permissionAllows.put("Command.Near", true);
		permissionAllows.put("Command.Spectate", true);
		permissionAllows.put("Command.SeeInventory", true);
		permissionAllows.put("Command.ClearChat", true);
		permissionAllows.put("Command.CheatCheacker", true);
		permissionAllows.put("Command.Mute", true);
		permissionAllows.put("Command.UnMute", true);
		permissionAllows.put("Command.Ban", true);
		permissionAllows.put("Command.AdminPanel", true);
		return permissionAllows;
	}

	@Override
	public boolean isAdmin() {
		return true;
	}
}