package ru.jl1mbo.AnarchyCore.SystemProcessorHandler.EconomyAPI;

import java.util.Arrays;

import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.plugin.PluginManager;
import cn.nukkit.utils.Config;
import ru.jl1mbo.AnarchyCore.Main;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.EconomyAPI.Commands.AddMoneyCommand;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.EconomyAPI.Commands.MoneyCommand;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.EconomyAPI.Commands.PayCommand;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.EconomyAPI.Commands.SeeMoneyCommand;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.EconomyAPI.Commands.SetMoneyCommand;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.EconomyAPI.EventsListener.PlayerJoinListener;

public class EconomyAPI {
	public static String PREFIX = "§l§7(§3Экономика§7) §r";
	public static Config config;

	public static void register() {
		config = new Config(Main.getInstance().getDataFolder() + "/EconomyAPI/Users.yml", Config.YAML);
		PluginManager pluginManager = Server.getInstance().getPluginManager();
		pluginManager.registerEvents(new PlayerJoinListener(), Main.getInstance());
		Server.getInstance().getCommandMap().registerAll("", Arrays.asList(new MoneyCommand(), new PayCommand(), new AddMoneyCommand(), new SeeMoneyCommand(), new SetMoneyCommand()));
	}

	public static boolean isRegister(String playerName) {
		return config.exists(playerName.toLowerCase());
	}

	public static void registerPlayer(String playerName) {
		config.set(playerName.toLowerCase(), 0.0);
		config.save();
		config.reload();
	}

	public static Double myMoney(String playerName) {
		return config.getDouble(playerName.toLowerCase(), 0.0);
	}

	public static void setMoney(String playerName, Double count) {
		config.set(playerName.toLowerCase(), count);
		config.save();
		config.reload();
	}

	public static void addMoney(String playerName, Double count) {
		setMoney(playerName, myMoney(playerName) + count);
	}

	public static void reduceMoney(String playerName, Double count) {
		setMoney(playerName, myMoney(playerName) - count);
	}
}