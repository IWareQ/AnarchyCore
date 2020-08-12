package Anarchy.Module.Commands;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.math.Vector3;

public class EpCommand extends Command {
	
	public EpCommand() {
		super("ep", "Узнать где Эндер Портал");
		setPermission("Command.Ep");
		commandParameters.clear();
	}
	
	@Override()
	public boolean execute(CommandSender commandSender, String s, String[] strings) {
		if (!commandSender.hasPermission("Command.Ep")) {
			return false;
		}
		Player player = (Player)commandSender;
		Vector3 block = player.getLevel().getBlock(new Vector3(0, 0, 0));
		int x = block.getFloorX();
		int y = block.getFloorY();
		int z = block.getFloorZ();
		player.sendMessage("Корды портала в энд " + x + " " + y + " " + z);
		return false;
	}
}