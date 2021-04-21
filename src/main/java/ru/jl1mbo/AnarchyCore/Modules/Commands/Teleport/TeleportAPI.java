package ru.jl1mbo.AnarchyCore.Modules.Commands.Teleport;

import ru.jl1mbo.AnarchyCore.Modules.Commands.Teleport.Utils.TeleportUtils;

import java.util.HashSet;

public class TeleportAPI {

	private static final HashSet<TeleportUtils> TPA_REQUEST = new HashSet<>();
	public static String PREFIX = "§l§7(§3Телепорт§7) §r";

	public static HashSet<TeleportUtils> getTpaRequests() {
		return TPA_REQUEST;
	}
}
