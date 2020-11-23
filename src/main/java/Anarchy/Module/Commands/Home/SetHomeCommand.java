package Anarchy.Module.Commands.Home;

import Anarchy.Manager.Functions.FunctionsAPI;
import Anarchy.Utils.SQLiteUtils;
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
			Integer homeId = SQLiteUtils.selectInteger("SELECT `Home_ID` FROM `Homes` WHERE UPPER(`Username`) = '" + player.getName().toUpperCase() + "';");
			if (player.getLevel() != FunctionsAPI.MAP) {
				player.sendMessage(HomeCommand.PREFIX + "§fВ этом мире §6запрещено §fустанавливать точки Дома§7!");
			} else if (homeId == -1) {
				player.sendMessage(HomeCommand.PREFIX + "§fНовая точка дома §6упешно §fустановлена§7!");
				SQLiteUtils.query("INSERT INTO `Homes` (`Username`, `X`, `Y`, `Z`) VALUES ('" + player.getName() + "', " + player.getFloorX() + ", " + player.getFloorY() + ", " + player.getFloorZ() + ");");
			} else {
				player.sendMessage(HomeCommand.PREFIX + "§fТочка дома §6успешно §fобновлена§7!");
				SQLiteUtils.query("UPDATE `Homes` SET `X` = '" + player.getFloorX() + "', `Y` = '" + player.getFloorY() + "', `Z` = '" + player.getFloorZ() + "' WHERE UPPER(`Username`) = '" + player.getName().toUpperCase() + "';");
			}
		}
		return false;
	}
}