package Anarchy.Module.Commands;

import Anarchy.Manager.Functions.FunctionsAPI;
import Anarchy.Module.Commands.Teleport.TpaCommand;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Position;

public class SpawnCommand extends Command {
	
	public SpawnCommand() {
		super("spawn", "§r§fТелепортироваться на спавн");
		this.commandParameters.clear();
	}
	
	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player)sender;
			if (player.getLevel().equals(FunctionsAPI.MAP)) {
				player.teleport(new Position(-8.5, 51, -3.5, FunctionsAPI.SPAWN));
				player.sendMessage("§l§a• §r§fВы §6успешно §fтелепортировались на спавн§7!");
			} else {
				player.sendMessage(TpaCommand.PREFIX + "§fС этого измерения запрещено телепортироваться§7!");
			}
		}
		return false;
	}
}