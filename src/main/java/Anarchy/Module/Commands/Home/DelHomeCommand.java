package Anarchy.Module.Commands.Home;

import Anarchy.Utils.SQLiteUtils;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class DelHomeCommand extends Command {
	
	public DelHomeCommand() {
		super("delhome", "Удалить точку дома");
		this.commandParameters.clear();
	}
	
	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		Player player = (Player)sender;
		String playerName = player.getName();
		String upperCase = playerName.toUpperCase();
		Integer homeId = SQLiteUtils.selectInteger("Homes.db", "SELECT `Home_ID` FROM `HOMES` WHERE UPPER(`Username`) = \'" + upperCase + "\';");
		if (!(sender instanceof Player)) {
			sender.sendMessage("§l§7(§3Система§7) §r§fЭту команду можно использовать только в §3Игре");
			return true;
		}
		if (homeId == -1) {
			player.sendMessage(HomeCommand.PREFIX + "§fТочки дома не обнаружено§7, §fдля создания используйте §7/§3sethome");
		} else {
			player.sendMessage(HomeCommand.PREFIX + "§fТочка дома §3успешно §fудалена§7!");
			SQLiteUtils.query("Homes.db", "DELETE FROM `HOMES` WHERE UPPER(`Username`) = \'" + upperCase + "\';");
		}
		return false;
	}
}