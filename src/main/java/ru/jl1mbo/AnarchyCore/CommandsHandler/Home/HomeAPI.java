package ru.jl1mbo.AnarchyCore.CommandsHandler.Home;

import java.util.Arrays;

import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.level.Position;
import cn.nukkit.utils.Config;
import ru.jl1mbo.AnarchyCore.CommandsHandler.Home.Commands.DelHomeCommand;
import ru.jl1mbo.AnarchyCore.CommandsHandler.Home.Commands.HomeCommand;
import ru.jl1mbo.AnarchyCore.CommandsHandler.Home.Commands.SetHomeCommand;
import ru.jl1mbo.AnarchyCore.Manager.WorldSystem.WorldSystemAPI;
import ru.jl1mbo.AnarchyCore.Utils.ConfigUtils;

public class HomeAPI {
	public static String PREFIX = "§l§7(§3Дом§7) §r";

	public static void register() {
		Command[] commands = new Command[] {new SetHomeCommand(), new HomeCommand(), new DelHomeCommand()};
		Server.getInstance().getCommandMap().registerAll("", Arrays.asList(commands));
	}

	public static boolean isHome(String playerName) {
		return ConfigUtils.getHomeConfig().exists(playerName.toLowerCase());
	}

	public static void addHome(String playerName, Integer x, Integer y, Integer z) {
		Config config = ConfigUtils.getHomeConfig();
		config.set(playerName.toLowerCase() + ".X", x);
		config.set(playerName.toLowerCase() + ".Y", y);
		config.set(playerName.toLowerCase() + ".Z", z);
		config.save();
		config.reload();
	}

	public static Position getHomePosition(String playerName) {
		Config config = ConfigUtils.getHomeConfig();
		int x = config.getInt(playerName.toLowerCase() + ".X");
		int y = config.getInt(playerName.toLowerCase() + ".Y");
		int z = config.getInt(playerName.toLowerCase() + ".Z");
		return new Position(x, y, z, WorldSystemAPI.getMap());
	}

	public static void removeHome(String playerName) {
		Config config = ConfigUtils.getHomeConfig();
		config.remove(playerName.toLowerCase());
		config.save();
		config.reload();
	}
}