package Anarchy.Module.Commands.Home;

import Anarchy.Utils.SQLiteUtils;
import Anarchy.Utils.StringUtils;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class DelHomeCommand extends Command {
	public DelHomeCommand() {
		super("delhome", "Удаление дома");
		commandParameters.clear();
	}

	@Override
	public boolean execute(CommandSender commandSender, String s, String[] strings) {
		if (strings.length != 1) {
			commandSender.sendMessage("§l§e| §fИспользование §7- §e/delhome <название>");
			return false;
		}

		if (!StringUtils.isValidString(strings[0])) {
			commandSender.sendMessage(HomeCommand.PREFIX + "§fВы можете использовать только буквы, цифры и нижнее подчеркивание");
			return false;
		}

		if (SQLiteUtils.selectInteger("Homes.db", "SELECT `X` FROM `HOMES` WHERE UPPER(`Home_Name`) = '" + strings[0].toUpperCase() + "' AND `Username` = '" + commandSender.getName() + "';") == -1) {
			commandSender.sendMessage(HomeCommand.PREFIX + "§fТочка дома §c" + strings[0] + " §fне существует!\n§l§e| §r§fСписок всех точек §7- §e/homes");
			return false;
		}

		commandSender.sendMessage(HomeCommand.PREFIX + "§fВы удалили точку дома §a" + strings[0]);
		SQLiteUtils.query("Homes.db", "DELETE FROM `HOMES` WHERE UPPER(`Home_Name`) = '" + strings[0].toUpperCase() + "' AND `Username` = '" + commandSender.getName() + "';");
		return false;
	}
}