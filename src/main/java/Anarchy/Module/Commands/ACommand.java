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
	public boolean execute(CommandSender sender, String label, String[] args) {
		Player player = (Player)sender;
		if (!player.hasPermission("Command.A")) {
			return false;
		}
		if (args.length < 1) {
			player.sendMessage("§l§e| §r§fИспользование §7- §e/a §7(§3текст§7)");
			return true;
		}
		String message = StringUtils.implode(args, 0);
		for (Player admin : Server.getInstance().getOnlinePlayers().values()) {
			if (admin.hasPermission("Command.A")) {
				admin.sendMessage("§cⒶ " + player.getDisplayName() + " §8» §f" + message);
			}
		}
		return false;
	}
}