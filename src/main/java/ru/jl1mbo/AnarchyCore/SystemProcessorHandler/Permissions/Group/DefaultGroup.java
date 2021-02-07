package ru.jl1mbo.AnarchyCore.SystemProcessorHandler.Permissions.Group;

import java.util.HashMap;

public abstract class DefaultGroup {

	public abstract String getGroupId();

	public abstract String getGroupName();

	public abstract Integer getMaxRegions();

	public abstract HashMap<String, Boolean> getPermissionAttachment();
}