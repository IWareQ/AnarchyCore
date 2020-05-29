package Anarchy.Module.Commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class ProfileCommand extends Command {
	public ProfileCommand() {
		super("profile", "Открыть Профиль");
		commandParameters.clear();
	}

	@Override
	public boolean execute(CommandSender commandSender, String s, String[] strings) {
		Player player = ((Player) commandSender);
		double ping = player.getPing();
		player.sendMessage("§l§fПинг §7- §a" + ping);
		return true;
	}
}