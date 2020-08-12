package Anarchy.Module.Commands.Home;

import Anarchy.Utils.SQLiteUtils;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class DelHomeCommand extends Command {
	
	public DelHomeCommand() {
		super("delhome", "Удалить точку дома");
		commandParameters.clear();
	}
	
	@Override()
	public boolean execute(CommandSender commandSender, String s, String[] strings) {
		Player player = (Player)commandSender;
		String playerName = player.getName();
		String upperCase = playerName.toUpperCase();
		Integer homeId = SQLiteUtils.selectInteger("Homes.db", "SELECT `Home_ID` FROM `HOMES` WHERE UPPER(`Username`) = \'" + upperCase + "\';");
		if (homeId == -1) {
			player.sendMessage(HomeCommand.PREFIX + "§fВы еще не создали точку дома§7, §fдля создания используйте §7/§3sethome");
		} else {
			commandSender.sendMessage(HomeCommand.PREFIX + "§fТочка дома §3успешно §fудалена");
			SQLiteUtils.query("Homes.db", "DELETE FROM `HOMES` WHERE UPPER(`Username`) = \'" + upperCase + "\';");
		}
		return false;
	}
}