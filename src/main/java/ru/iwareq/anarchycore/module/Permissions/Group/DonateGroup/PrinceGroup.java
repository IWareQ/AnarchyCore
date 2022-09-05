package ru.iwareq.anarchycore.module.Permissions.Group.DonateGroup;

import ru.iwareq.anarchycore.module.Permissions.Group.DefaultGroup;

import java.util.HashMap;

public class PrinceGroup extends DefaultGroup {

	@Override
	public String getGroupId() {
		return "prince";
	}

	@Override
	public String getGroupName() {
		return "§6Принц§r";
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
		return permissionAllows;
	}

	@Override
	public boolean isAdmin() {
		return false;
	}
}