package ru.jl1mbo.AnarchyCore.SystemProcessorHandler.Permissions.Group.StaffGroup;

import java.util.HashMap;

import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.Permissions.Group.DefaultGroup;

public class AdministratorGroup extends DefaultGroup {

	@Override
	public String getGroupId() {
		return "admin";
	}

	@Override
	public String getGroupName() {
		return "§4Админ§r";
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
		permissionAllows.put("Command.Near.150", true);
		permissionAllows.put("Command.Bonus", true);
		permissionAllows.put("Command.Spectate", true);
		permissionAllows.put("Command.Inventory", true);
		permissionAllows.put("Command.CheatCheack", true);
		permissionAllows.put("AdminChat", true);
		permissionAllows.put("Command.Mute", true);
		permissionAllows.put("Command.UnMute", true);
		permissionAllows.put("Command.Ban", true);
		permissionAllows.put("Command.UnBan", true);
		return permissionAllows;
	}
}