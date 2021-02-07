package ru.jl1mbo.AnarchyCore.SystemProcessorHandler.Permissions.Group.DonateGroup;

import java.util.HashMap;

import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.Permissions.Group.DefaultGroup;

public class TartarusGroup extends DefaultGroup {

	@Override
	public String getGroupId() {
		return "tartarus";
	}

	@Override
	public String getGroupName() {
		return "§eТартар§r";
	}

	@Override
	public Integer getMaxRegions() {
		return 8;
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
		return permissionAllows;
	}
}