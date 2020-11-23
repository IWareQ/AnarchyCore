package Anarchy.Module.Commands.Home;

import Anarchy.Utils.SQLiteUtils;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class DelHomeCommand extends Command {

	public DelHomeCommand() {
		super("delhome", "§r§fУдалить точку дома");
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player)sender;
			Integer homeId = SQLiteUtils.selectInteger("SELECT `Home_ID` FROM `Homes` WHERE UPPER(Username) = '" + player.getName().toUpperCase() + "';");
			if (homeId == -1) {
				player.sendMessage(HomeCommand.PREFIX + "§fТочки дома не обнаружено§7, §fдля создания используйте §7/§6sethome");
			} else {
				player.sendMessage(HomeCommand.PREFIX + "§fТочка дома успешно §fудалена§7!");
				SQLiteUtils.query("DELETE FROM `Homes` WHERE UPPER(`Username`) = '" + player.getName().toUpperCase() + "';");
			}
		}
		return false;
	}
}