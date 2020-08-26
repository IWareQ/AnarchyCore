package Anarchy.Module.Commands.Home;

import Anarchy.Manager.Functions.FunctionsAPI;
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
	public boolean execute(CommandSender sender, String label, String[] args) {
		Player player = (Player)sender;
		String playerName = player.getName();
		String upperCase = playerName.toUpperCase();
		int x = player.getFloorX();
		int y = player.getFloorY();
		int z = player.getFloorZ();
		Integer homeId = SQLiteUtils.selectInteger("Homes.db", "SELECT `Home_ID` FROM `HOMES` WHERE UPPER(`Username`) = \'" + upperCase + "\';");
		if (!(sender instanceof Player)) {
			sender.sendMessage("§l§7(§3Система§7) §r§fЭту команду можно использовать только в §3Игре");
			return true;
		}
		if (player.getLevel() != FunctionsAPI.WORLD) {
			player.sendMessage(HomeCommand.PREFIX + "§fВ этом мире §3запрещено §fставить точку Дома§7!");
		} else if (homeId == -1) {
			player.sendMessage(HomeCommand.PREFIX + "§fНовая точка дома §3упешно §fустановлена");
			SQLiteUtils.query("Homes.db", "INSERT INTO `HOMES` (`Username`, `X`, `Y`, `Z`) VALUES ( \'" + player.getName() + "\', " + x + ", " + y + ", " + z + ");");
		} else {
			player.sendMessage(HomeCommand.PREFIX + "§fТочка дома §3успешно §fобновлена§7!");
			SQLiteUtils.query("Homes.db", "UPDATE `HOMES` SET `X` = \'" + x + "\', `Y` = \'" + y + "\', `Z` = \'" + z + "\' WHERE UPPER(`Username`) = \'" + upperCase + "\';");
		}
		return false;
	}
}