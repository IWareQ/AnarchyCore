package Anarchy.Module.Commands.Home;

import Anarchy.Utils.SQLiteUtils;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class SetHomeCommand extends Command {
	
	public SetHomeCommand() {
		super("sethome", "Установить новую точку дома");
		commandParameters.clear();
	}
	
	@Override()
	public boolean execute(CommandSender commandSender, String s, String[] strings) {
		Player player = (Player)commandSender;
		String playerName = player.getName();
		String upperCase = playerName.toUpperCase();
		int x = player.getFloorX();
		int y = player.getFloorY();
		int z = player.getFloorZ();
		Integer homeId = SQLiteUtils.selectInteger("Homes.db", "SELECT `Home_ID` FROM `HOMES` WHERE UPPER(`Username`) = \'" + upperCase + "\';");
		if (homeId == -1) {
			player.sendMessage(HomeCommand.PREFIX + "§fНовая точка дома §3упешно §fустановлена");
			SQLiteUtils.query("Homes.db", "INSERT INTO `HOMES` (`Username`, `X`, `Y`, `Z`, `LEVEL`) VALUES ( \'" + player.getName() + "\', " + x + ", " + y + ", " + z + ", \'" + player.getLevel().getName() + "\');");
		} else {
			player.sendMessage(HomeCommand.PREFIX + "§fТочка дома §3успешно §fобновлена§7!");
			SQLiteUtils.query("Homes.db", "UPDATE `HOMES` SET `X` = \'" + x + "\', `Y` = \'" + y + "\', `Z` = \'" + z + "\' WHERE UPPER(`Username`) = \'" + upperCase + "\';");
		}
		return false;
	}
}