package ru.iwareq.anarchycore.module.Permissions.Group.DonateGroup;

import ru.iwareq.anarchycore.module.Permissions.Group.DefaultGroup;

import java.util.HashMap;

public class HellHoundGroup extends DefaultGroup {

	@Override
	public String getGroupId() {
		return "hell_hound";
	}

	@Override
	public String getGroupName() {
		return "§7Цербер§r";
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
