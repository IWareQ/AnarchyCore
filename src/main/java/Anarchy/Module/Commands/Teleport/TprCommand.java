package Anarchy.Module.Commands.Teleport;

import java.util.concurrent.ThreadLocalRandom;

import Anarchy.Manager.Functions.FunctionsAPI;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Level;
import cn.nukkit.math.Vector3;

public class TprCommand extends Command {
	
	public TprCommand() {
		super("tpr", "Телепортироваться в случайное место", "", new String[]{"rtp"});
		commandParameters.clear();
	}
	
	@Override()
	public boolean execute(CommandSender commandSender, String s, String[] strings) {
		Player player = (Player)commandSender;
		Level level = Server.getInstance().getLevelByName("world");
		Vector3 teleportPosition = new Vector3(ThreadLocalRandom.current().nextInt(FunctionsAPI.RANDOM_TP[0], FunctionsAPI.RANDOM_TP[1]), 256, ThreadLocalRandom.current().nextInt(FunctionsAPI.RANDOM_TP[2], FunctionsAPI.RANDOM_TP[3]));
		level.loadChunk(ThreadLocalRandom.current().nextInt(FunctionsAPI.RANDOM_TP[0], FunctionsAPI.RANDOM_TP[1]) >> 4, ThreadLocalRandom.current().nextInt(FunctionsAPI.RANDOM_TP[2], FunctionsAPI.RANDOM_TP[3]) >> 4);
		player.teleport(player.getLevel().getSafeSpawn(teleportPosition));
		commandSender.sendMessage("§l§a| §r§fВы телепортированы в случайное место");
		return false;
	}
}