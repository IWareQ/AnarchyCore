package Anarchy.Module.Commands.Home;

import Anarchy.Functions.FunctionsAPI;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class SetHomeCommand extends Command {

	public SetHomeCommand() {
		super("sethome", "§r§fУстановить новую точку дома");
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player)sender;
			if (player.getLevel() != FunctionsAPI.MAP) {
				player.sendMessage(HomeAPI.PREFIX + "§fВ этом мире §6запрещено §fустанавливать точки Дома§7!");
				return false;
			}
			if (!HomeAPI.playerIsHome(player.getName())) {
				HomeAPI.setHome(player.getName(), player.getFloorX(), player.getFloorY(), player.getFloorZ());
				player.sendMessage(HomeAPI.PREFIX + "§fНовая точка дома §6упешно §fустановлена§7!");
			} else {
				player.sendMessage(HomeAPI.PREFIX + "§fУ Вас уже имеется точка Дома§7!\n§l§6• §r§fДля удаления точки Дома используйте §7/§6delhome");
			}
		}
		return false;
	}
}