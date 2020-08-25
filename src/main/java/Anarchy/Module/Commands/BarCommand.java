package Anarchy.Module.Commands;

import Anarchy.Manager.Sessions.PlayerSessionManager;
import Anarchy.Task.HotbarTask;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class BarCommand extends Command {
	
	public BarCommand() {
		super("bar", "§l§fВключить§7/§fВыключить ХотБар");
		commandParameters.clear();
	}
	
	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		Player player = (Player)sender;
		String playerName = player.getName();
		if (PlayerSessionManager.SCOREBOARD.contains(playerName)) {
			PlayerSessionManager.SCOREBOARDS.get(playerName).hideFor(player);
			PlayerSessionManager.SCOREBOARD.remove(playerName);
			player.sendMessage("§l§a| §r§fХотбар §3успешно §fотключен");
		} else {
			PlayerSessionManager.SCOREBOARD.add(playerName);
			HotbarTask.showScoreboard(player);
			player.sendMessage("§l§a| §r§fХотбар §3успешно §fвключен");
		}
		return false;
	}
}