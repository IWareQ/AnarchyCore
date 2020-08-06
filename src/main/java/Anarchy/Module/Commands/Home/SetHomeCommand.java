package Anarchy.Module.Commands.Home;

import java.util.HashMap;
import java.util.Map;
import Anarchy.Module.Permissions.PermissionsAPI;
import Anarchy.Module.Permissions.Utils.GroupAllow;
import Anarchy.Utils.SQLiteUtils;
import Anarchy.Utils.StringUtils;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class SetHomeCommand extends Command {
	public static Map<Player, String> HOME_SET = new HashMap<>();
	
	public SetHomeCommand() {
		super("sethome", "Установить новую точку дома");
		commandParameters.clear();
	}
	
	@Override()
	public boolean execute(CommandSender commandSender, String s, String[] strings) {
		Player player = (Player)commandSender;
		if (strings.length != 1) {
			commandSender.sendMessage("§l§e| §r§fИспользование §7- §e/sethome §7(§3название§7)");
			return false;
		}
		if (!StringUtils.isValidString(strings[0])) {
			commandSender.sendMessage(HomeCommand.PREFIX + "§fВы можете использовать только §3буквы§7, §3цифры §fи §3нижнее подчеркивание");
			return false;
		}
		GroupAllow groupAllow = PermissionsAPI.getGroupAllows(PermissionsAPI.getGroup(player));
		if (groupAllow != null) {
			if (SQLiteUtils.selectInteger("Homes.db", "SELECT COUNT(*) as COUNT FROM `HOMES` WHERE `Username` = \'" + commandSender.getName() + "\';") >= groupAllow.MAX_HOMES) {
				commandSender.sendMessage(HomeCommand.PREFIX + "§fМаксимальное количество точек дома §7- §c" + groupAllow.MAX_HOMES + "\n§l§e| §r§fУдалите §e1 §fиз старых точек дома§7!");
				return false;
			}
		}
		if (SQLiteUtils.selectInteger("Homes.db", "SELECT `X` FROM `HOMES` WHERE UPPER(`Home_Name`) = \'" + strings[0].toUpperCase() + "\' AND `Username` = \'" + commandSender.getName() + "\';") != -1) {
			commandSender.sendMessage(HomeCommand.PREFIX + "§fТочка дома §e" + strings[0] + " §7- §fуже существует§7!\n§l§e| §r§fПридумайте другое название§7!");
			return false;
		}
		commandSender.sendMessage(HomeCommand.PREFIX + "§fЧтобы установить точку дома §e" + strings[0] + " §7- §fнажмите на кровать§7!");
		HOME_SET.put((Player)commandSender, strings[0]);
		return false;
	}
}