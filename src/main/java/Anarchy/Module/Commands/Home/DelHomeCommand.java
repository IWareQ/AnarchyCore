package Anarchy.Module.Commands.Home;

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
			if (!HomeAPI.playerIsHome(player.getName())) {
				player.sendMessage(HomeAPI.PREFIX + "§fТочек дома не обнаружено§7!\n§l§6• §r§fДля создания точки Дома используйте §7/§6sethome");
			} else {
				player.sendMessage(HomeAPI.PREFIX + "§fТочка дома §6успешно §fудалена§7!");
				HomeAPI.delHome(player.getName());
			}
		}
		return false;
	}
}