package Anarchy.Module.Commands;

import Anarchy.Manager.Sessions.PlayerSessionManager;
import Anarchy.Task.HotbarTask;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class BarCommand extends Command {
	public BarCommand() {
		super("bar", "Настройки хотбара");
		commandParameters.clear();
	}

	@Override
	public boolean execute(CommandSender commandSender, String s, String[] strings) {
		String senderName = commandSender.getName();
		if (PlayerSessionManager.SCOREBOARD.contains(senderName)) {
			PlayerSessionManager.SCOREBOARDS.get(senderName).hideFor((Player) commandSender);
			PlayerSessionManager.SCOREBOARD.remove(senderName);
			commandSender.sendMessage("§l§a| §r§fХотбар успешно §cВыключен");
		} else {
			PlayerSessionManager.SCOREBOARD.add(senderName);
			HotbarTask.showScoreboard((Player) commandSender);
			commandSender.sendMessage("§l§a| §r§fХотбар успешно §cВключен");
		}
		return false;
	}
}