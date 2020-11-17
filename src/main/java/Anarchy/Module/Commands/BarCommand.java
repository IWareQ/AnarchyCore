package Anarchy.Module.Commands;

import Anarchy.Manager.Sessions.PlayerSessionManager;
import Anarchy.Task.HotbarTask;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class BarCommand extends Command {
	
	public BarCommand() {
		super("bar", "§r§fУправление ХотБаром");
		this.commandParameters.clear();
	}
	
	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player)sender;
			if (PlayerSessionManager.SCOREBOARD.contains(player.getName())) {
				PlayerSessionManager.SCOREBOARDS.get(player.getName()).hideFor(player);
				PlayerSessionManager.SCOREBOARD.remove(player.getName());
				player.sendMessage("§l§a• §r§fХотбар §6успешно §fотключен§7!");
			} else {
				PlayerSessionManager.SCOREBOARD.add(player.getName());
				HotbarTask.showScoreboard(player);
				player.sendMessage("§l§a• §r§fХотбар §6успешно §fвключен§7!");
			}
		}
		return false;
	}
}