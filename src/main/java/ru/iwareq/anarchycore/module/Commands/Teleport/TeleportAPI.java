package ru.iwareq.anarchycore.module.Commands.Teleport;

import ru.iwareq.anarchycore.module.Commands.Teleport.Utils.TeleportUtils;

import java.util.HashSet;

public class TeleportAPI {

	public static final String PREFIX = "§l§7(§3Телепорт§7) §r";

	private static final HashSet<TeleportUtils> TPA_REQUEST = new HashSet<>();

	public static HashSet<TeleportUtils> getTpaRequests() {
		return TPA_REQUEST;
	}
}
