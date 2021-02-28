package ru.jl1mbo.AnarchyCore.SystemProcessorHandler.TeleportSystem;

import java.util.Arrays;
import java.util.HashSet;

import cn.nukkit.Server;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.TeleportSystem.Commands.RtpCommand;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.TeleportSystem.Commands.TpaCommand;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.TeleportSystem.Commands.TpcCommand;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.TeleportSystem.Task.TpaRequestTask;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.TeleportSystem.Utils.TeleportUtils;

public class TeleportAPI {
	public static String PREFIX = "§l§7(§3Телепорт§7) §r";
	private static final HashSet<TeleportUtils> TPA_REQUEST = new HashSet<>();

	public static void register() {
		Server.getInstance().getScheduler().scheduleRepeatingTask(new TpaRequestTask(), 20);
		Server.getInstance().getCommandMap().registerAll("", Arrays.asList(new TpaCommand(), new TpcCommand(), new RtpCommand()));
	}

	public static HashSet<TeleportUtils> getTpaRequest() {
		return TPA_REQUEST;
	}
}
