package Anarchy.Module.Commands;

import Anarchy.Manager.Sessions.PlayerSessionManager;
import Anarchy.Task.HotbarTask;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class BarCommand extends Command {
	
	public BarCommand() {
		super("bar", "Включить/Выключить ХотБар");
		commandParameters.clear();
	}
	
	@Override()
	public boolean execute(CommandSender commandSender, String s, String[] strings) {
		String playerName = commandSender.getName();
		if (PlayerSessionManager.SCOREBOARD.contains(playerName)) {
			PlayerSessionManager.SCOREBOARDS.get(playerName).hideFor((Player)commandSender);
			PlayerSessionManager.SCOREBOARD.remove(playerName);
			commandSender.sendMessage("§l§c| §r§fХотбар §3успешно §fотключен");
		} else {
			PlayerSessionManager.SCOREBOARD.add(playerName);
			HotbarTask.showScoreboard((Player)commandSender);
			commandSender.sendMessage("§l§a| §r§fХотбар §3успешно §fвключен");
		}
		return false;
	}
}