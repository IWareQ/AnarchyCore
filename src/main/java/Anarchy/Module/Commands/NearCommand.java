package Anarchy.Module.Commands;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class NearCommand extends Command {
	public static int RADIUS = 70;

	public NearCommand() {
		super("near", "Игроки рядом");
		setPermission("Command.Near");
		commandParameters.clear();
	}

	@Override
	public boolean execute(CommandSender commandSender, String s, String[] strings) {
		if (!commandSender.hasPermission("Command.Near")) {
			return false;
		}

		Player player = (Player) commandSender;
		StringBuilder stringBuilder = new StringBuilder();
		for (Player list: Server.getInstance().getOnlinePlayers().values()) {
			if (list.distance(player)<RADIUS && list.gamemode != 3) {
				stringBuilder.append("§7, §e").append(list.getName());
			}
		}
		commandSender.sendMessage("§l§e| §r§fИгроки в радиусе §a" + RADIUS + " §fблоков §7- §f" + (stringBuilder.length() != 0 ? stringBuilder.toString().substring(4) : "..."));
		return false;
	}
}