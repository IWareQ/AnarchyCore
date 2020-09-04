package Anarchy.Module.Commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class CoordinateCommand extends Command {
	
	public CoordinateCommand() {
		super("coordinate", "§l§fВключить§7/§fВыключить координаты");
		setPermission("Command.Day");
		commandParameters.clear();
	}
	
	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		Player player = (Player)sender;
		if (!(sender instanceof Player)) {
			sender.sendMessage("§l§7(§3Система§7) §r§fЭту команду можно использовать только в §3Игре");
			return true;
		}
		if (!player.hasPermission("Command.Coordinate")) {
			return false;
		}
		player.sendMessage("Эта команда скоро будет работать");
		return false;
	}
}