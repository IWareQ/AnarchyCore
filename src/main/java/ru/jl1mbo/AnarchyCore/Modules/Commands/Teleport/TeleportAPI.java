package ru.jl1mbo.AnarchyCore.Modules.Commands.Teleport;

import java.util.HashSet;

import ru.jl1mbo.AnarchyCore.Modules.Commands.Teleport.Utils.TeleportUtils;

public class TeleportAPI {
	public static String PREFIX = "§l§7(§3Телепорт§7) §r";
	private static final HashSet<TeleportUtils> TPA_REQUEST = new HashSet<>();

	public static HashSet<TeleportUtils> getTpaRequests() {
		return TPA_REQUEST;
	}
}
