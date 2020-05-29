package Anarchy.Module.Commands;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Level;

public class NightCommand extends Command {
	public NightCommand() {
		super("night", "Установка ночи");
		setPermission("Command.Night");
		commandParameters.clear();
	}

	@Override
	public boolean execute(CommandSender commandSender, String s, String[] strings) {
		if (!commandSender.hasPermission("Command.Night")) {
			return false;
		}

		Level level = ((Player) commandSender).getLevel();
		level.setTime(14000);
		for (Player player: Server.getInstance().getOnlinePlayers().values()) {
			if (player.level == level) {
				player.sendMessage("§l§e| §r§fИгрок §a" + commandSender.getName() + " §fустановил§7(§fа§7) §9Ночь");
			}
		}
		return false;
	}
}