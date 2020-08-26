package Anarchy.Module.Commands;

import Anarchy.Manager.Functions.FunctionsAPI;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class DayCommand extends Command {
	
	public DayCommand() {
		super("day", "§l§fСменить §9Ночь §fна §eДень");
		setPermission("Command.Day");
		commandParameters.clear();
	}
	
	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		Player player = (Player)sender;
		String playerName = player.getName();
		if (!(sender instanceof Player)) {
			sender.sendMessage("§l§7(§3Система§7) §r§fЭту команду можно использовать только в §3Игре");
			return true;
		}
		if (!player.hasPermission("Command.Day")) {
			return false;
		}
		
		FunctionsAPI.WORLD.setTime(1000);
		for (Player players: Server.getInstance().getOnlinePlayers().values()) {
				players.sendMessage("§l§6| §r§fИгрок §3" + playerName + " §fустановил§7(§fа§7) §eДень");
		}
		return false;
	}
}