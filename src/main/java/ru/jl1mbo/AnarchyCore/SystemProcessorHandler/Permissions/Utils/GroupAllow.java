package ru.jl1mbo.AnarchyCore.SystemProcessorHandler.Permissions.Utils;

public class GroupAllow {
	private final int MAX_REGIONS;

	public GroupAllow(Integer max_regions) {
		this.MAX_REGIONS = max_regions;
	}

	public Integer getMaxRegions() {
		return MAX_REGIONS;
	}
}