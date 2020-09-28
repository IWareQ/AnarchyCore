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
		Player player = (Player)sender;
		player.teleport(FunctionsAPI.randomPos(new Position(0, 0, 0, FunctionsAPI.MAP)));
		player.sendMessage("§l§a| §r§fВас §6успешно §fтелепортировало на рандомное место§7.");
		return false;
	}
}