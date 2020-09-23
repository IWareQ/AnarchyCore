package Anarchy.Module.Commands.Teleport;

import Anarchy.Manager.Functions.FunctionsAPI;
import Anarchy.Utils.RandomUtils;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Position;

public class TprCommand extends Command {
	
	public TprCommand() {
		super("tpr", "Телепортироваться в случайное место", "", new String[]{"rtp"});
		this.commandParameters.clear();
	}
	
	@Override()
	public boolean execute(CommandSender commandSender, String s, String[] strings) {
		Player player = (Player)commandSender;
		int x = RandomUtils.rand(FunctionsAPI.RANDOM_TP[0], FunctionsAPI.RANDOM_TP[1]);
		int z = RandomUtils.rand(FunctionsAPI.RANDOM_TP[2], FunctionsAPI.RANDOM_TP[3]);
		player.teleport(new Position(x, 67, z, FunctionsAPI.MAP));
		player.sendMessage("§l§a| §r§fВас §6успешно §fтелепортировало на рандомное место§7.");
		return false;
	}
}