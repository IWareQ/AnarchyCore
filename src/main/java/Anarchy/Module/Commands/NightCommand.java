package Anarchy.Module.Commands;

import Anarchy.Manager.Functions.FunctionsAPI;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class NightCommand extends Command {
	
	public NightCommand() {
		super("night", "§fСменить День на Ночь");
		this.setPermission("Command.Night");
		this.commandParameters.clear();
	}
	
	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		Player player = (Player)sender;
		String playerName = player.getName();
		if (!(sender instanceof Player)) {
			sender.sendMessage("§l§7(§3Система§7) §r§fЭту команду можно использовать только в §3Игре");
			return true;
		}
		if (!player.hasPermission("Command.Night")) {
			return false;
		}
		FunctionsAPI.MAP.setTime(14000);
		for (Player players : Server.getInstance().getOnlinePlayers().values()) {
			players.sendMessage("§l§6| §r§fИгрок §3" + playerName + " §fустановил§7(§fа§7) §9Ночь");
		}
		return false;
	}
}