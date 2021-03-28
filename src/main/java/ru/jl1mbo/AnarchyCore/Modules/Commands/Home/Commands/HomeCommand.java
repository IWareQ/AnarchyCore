package ru.jl1mbo.AnarchyCore.Modules.Commands.Home.Commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Position;
import ru.jl1mbo.AnarchyCore.Modules.Commands.Home.HomeAPI;
import ru.jl1mbo.AnarchyCore.Utils.SQLiteUtils;

public class HomeCommand extends Command {

	public HomeCommand() {
		super("home", "§rТелепортироваться домой");
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (HomeAPI.isHome(player.getName())) {
				player.sendMessage(HomeAPI.PREFIX + "Вы успешно телепортированы домой§7!");
				player.teleport(HomeAPI.getHomePosition(player.getName()));
			} else {
				player.sendMessage(HomeAPI.PREFIX + "Точек дома не обнаружено§7!\n§l§6• §rДля создания точки Дома используйте §7/§6sethome");
			}
		}
		return false;
	}
}