package Anarchy.Module.Commands.Teleport;

import Anarchy.Manager.Functions.FunctionsAPI;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Position;

public class TprCommand extends Command {

	public TprCommand() {
		super("tpr", "Телепортироваться в случайное место", "", new String[] {"rtp"});
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player)sender;
			if (player.getLevel().equals(FunctionsAPI.MAP) || player.getLevel().equals(FunctionsAPI.SPAWN)) {
				player.teleport(FunctionsAPI.randomPos(new Position(0, 0, 0, FunctionsAPI.MAP)));
				player.sendMessage("§l§a| §r§fВас §6успешно §fтелепортировало на рандомное место§7.");
			} else {
				player.sendMessage(TpaCommand.PREFIX + "§fС этого измерения запрещено телепортироваться§7!");
			}
		}
		return false;
	}
}