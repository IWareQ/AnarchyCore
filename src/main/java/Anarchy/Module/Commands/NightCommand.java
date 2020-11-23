package Anarchy.Module.Commands;

import Anarchy.Functions.FunctionsAPI;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class NightCommand extends Command {
	
	public NightCommand() {
		super("night", "§r§fСменить §eДень §fна §9Ночь");
		this.setPermission("Command.Night");
		this.commandParameters.clear();
	}
	
	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player)sender;
			if (!player.hasPermission("Command.Night")) {
				return false;
			}
			FunctionsAPI.MAP.setTime(14000);
			for (Player players : Server.getInstance().getOnlinePlayers().values()) {
				players.sendMessage("§l§6• §r§fИгрок §6" + player.getName() + " §fустановил§7(§fа§7) §9Ночь");
			}
		}
		return false;
	}
}