package Anarchy.Module.Commands;

import Anarchy.Utils.StringUtils;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class ACommand extends Command {
	
	public ACommand() {
		super("a", "Админ-Чат");
		setPermission("Command.A");
		commandParameters.clear();
	}
	
	@Override()
	public boolean execute(CommandSender commandSender, String s, String[] args) {
		if (!commandSender.hasPermission("Command.A")) {
			return false;
		}
		if (args.length < 1) {
			commandSender.sendMessage("§l§e| §r§fИспользование §7- §e/a §7(§6текст§7)");
			return true;
		}
		Player player = (Player)commandSender;
		String message = StringUtils.implode(args, 0);
		for (Player admin : Server.getInstance().getOnlinePlayers().values()) {
			if (admin.hasPermission("Admin.Chat")) {
				admin.sendMessage("§cⒶ " + player.getDisplayName() + " §8» §f" + message);
			}
		}
		return false;
	}
}