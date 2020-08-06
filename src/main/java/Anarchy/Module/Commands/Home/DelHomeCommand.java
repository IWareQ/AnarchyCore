package Anarchy.Module.Commands.Home;

import Anarchy.Utils.SQLiteUtils;
import Anarchy.Utils.StringUtils;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class DelHomeCommand extends Command {
	
	public DelHomeCommand() {
		super("delhome", "Удалить точку дома");
		commandParameters.clear();
	}
	
	@Override()
	public boolean execute(CommandSender commandSender, String s, String[] strings) {
		if (strings.length != 1) {
			commandSender.sendMessage("§l§e| §r§fИспользование §7- §e/delhome §7(§3название§7)");
			return false;
		}
		if (!StringUtils.isValidString(strings[0])) {
			commandSender.sendMessage(HomeCommand.PREFIX + "§fВы можете использовать только §3буквы§7, §3цифры §fи §3нижнее подчеркивание");
			return false;
		}
		if (SQLiteUtils.selectInteger("Homes.db", "SELECT `X` FROM `HOMES` WHERE UPPER(`Home_Name`) = \'" + strings[0].toUpperCase() + "\' AND `Username` = \'" + commandSender.getName() + "\';") == -1) {
			commandSender.sendMessage(HomeCommand.PREFIX + "§fТочки дома §e" + strings[0] + " §fне существует§7!\n§l§e| §r§fСписок всех точек §7- §e/homes");
			return false;
		}
		commandSender.sendMessage(HomeCommand.PREFIX + "§fТочка дома §e" + strings[0] + " §3успешно §fудалена");
		SQLiteUtils.query("Homes.db", "DELETE FROM `HOMES` WHERE UPPER(`Home_Name`) = \'" + strings[0].toUpperCase() + "\' AND `Username` = \'" + commandSender.getName() + "\';");
		return false;
	}
}