package ru.iwareq.anarchycore.module.Permissions.Group.SpecialGroup;

import ru.iwareq.anarchycore.module.Permissions.Group.DefaultGroup;

import java.util.HashMap;

public class YouTubeGroup extends DefaultGroup {

	@Override
	public String getGroupId() {
		return "youtube";
	}

	@Override
	public String getGroupName() {
		return "§7YouTube§r";
	}

	@Override
	public Integer getMaxRegions() {
		return 4;
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