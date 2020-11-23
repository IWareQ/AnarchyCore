package Anarchy.Module.Commands;

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
			if (HotbarTask.SCOREBOARD.contains(player.getName())) {
				HotbarTask.SCOREBOARDS.get(player.getName()).hideFor(player);
				HotbarTask.SCOREBOARD.remove(player.getName());
				player.sendMessage("§l§a• §r§fХотбар §6успешно §fотключен§7!");
			} else {
				HotbarTask.SCOREBOARD.add(player.getName());
				HotbarTask.showScoreboard(player);
				player.sendMessage("§l§a• §r§fХотбар §6успешно §fвключен§7!");
			}
		}
		return false;
	}
}