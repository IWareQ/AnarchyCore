package Anarchy.Module.Commands;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Level;

public class DayCommand extends Command {
	
	public DayCommand() {
		super("day", "Сменить Ночь на День");
		setPermission("Command.Day");
		commandParameters.clear();
	}
	
	@Override()
	public boolean execute(CommandSender commandSender, String s, String[] strings) {
		if (!commandSender.hasPermission("Command.Day")) {
			return false;
		}
		Level level = ((Player)commandSender).getLevel();
		level.setTime(1000);
		for (Player player : Server.getInstance().getOnlinePlayers().values()) {
			if (player.level == level) {
				player.sendMessage("§l§e| §r§fИгрок §e" + commandSender.getName() + " §fустановил§7(§fа§7) §eДень");
			}
		}
		return false;
	}
}