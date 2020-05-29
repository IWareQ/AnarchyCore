package Anarchy.Module.Commands;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Level;

public class DayCommand extends Command {
	public DayCommand() {
		super("day", "Установка дня");
		setPermission("Command.Day");
		commandParameters.clear();
	}

	@Override
	public boolean execute(CommandSender commandSender, String s, String[] strings) {
		if (!commandSender.hasPermission("Command.Day")) {
			return false;
		}

		Level level = ((Player) commandSender).getLevel();
		level.setTime(1000);
		for (Player player: Server.getInstance().getOnlinePlayers().values()) {
			if (player.level == level) {
				player.sendMessage("§l§e| §r§fИгрок §a" + commandSender.getName() + " §fустановил§7(§fа§7) §eДень");
			}
		}
		return false;
	}
}