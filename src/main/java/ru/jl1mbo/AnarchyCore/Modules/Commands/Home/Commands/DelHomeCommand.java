package ru.jl1mbo.AnarchyCore.Modules.Commands.Home.Commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import ru.jl1mbo.AnarchyCore.Modules.Commands.Home.HomeAPI;
import ru.jl1mbo.MySQLUtils.MySQLUtils;

public class DelHomeCommand extends Command {

	public DelHomeCommand() {
		super("delhome", "§rУдалить точку дома");
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (HomeAPI.isHome(player.getName())) {
				MySQLUtils.query("DELETE FROM `Homes` WHERE UPPER (`Name`) = '" + player.getName().toUpperCase() + "'");
				player.sendMessage(HomeAPI.PREFIX + "Точка дома §6успешно §fудалена§7!");
			} else {
				player.sendMessage(HomeAPI.PREFIX + "Точек дома не обнаружено§7!\n§l§6• §rДля создания точки Дома используйте §7/§6sethome");
			}
		}
		return false;
	}
}