package Anarchy.Module.Commands;

import Anarchy.Functions.FunctionsAPI;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class DayCommand extends Command {

	public DayCommand() {
		super("day", "§r§fСменить §9Ночь §fна §eДень");
		this.setPermission("Command.Day");
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player)sender;
			if (!player.hasPermission("Command.Day")) {
				return false;
			}
			FunctionsAPI.MAP.setTime(1000);
			for (Player players : Server.getInstance().getOnlinePlayers().values()) {
				players.sendMessage("§l§6• §r§fИгрок §6" + player.getName() + " §fустановил§7(§fа§7) §eДень");
			}
		}
		return false;
	}
}