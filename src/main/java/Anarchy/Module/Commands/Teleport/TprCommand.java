package Anarchy.Module.Commands.Teleport;

import java.util.concurrent.ThreadLocalRandom;

import Anarchy.Manager.Functions.FunctionsAPI;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.math.Vector3;

public class TprCommand extends Command {
	public TprCommand() {
		super("tpr", "Телепортация в случайное место", "", new String[] {
			"rtp"
		});
		commandParameters.clear();
	}

	@Override
	public boolean execute(CommandSender commandSender, String s, String[] strings) {
		Player player = (Player) commandSender;
		Vector3 teleportPosition = new Vector3(ThreadLocalRandom.current().nextInt(FunctionsAPI.RANDOM_TP[0], FunctionsAPI.RANDOM_TP[1]), 256, ThreadLocalRandom.current().nextInt(FunctionsAPI.RANDOM_TP[2], FunctionsAPI.RANDOM_TP[3]));
		player.teleport(player.getLevel().getSafeSpawn(teleportPosition));
		commandSender.sendMessage("§l§a| §r§fВы телепортированы в случайное место");
		return false;
	}
}