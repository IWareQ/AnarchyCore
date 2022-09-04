package ru.iwareq.anarchycore.module.Permissions.Group;

import java.util.Map;

public abstract class DefaultGroup {

	public abstract String getGroupId();

	public abstract String getGroupName();

	public abstract Integer getMaxRegions();

	public abstract Map<String, Boolean> getPermissionAttachment();

	public abstract boolean isAdmin();
}