package ru.jl1mbo.AnarchyCore.CommandsHandler.Home.Commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Position;
import ru.jl1mbo.AnarchyCore.CommandsHandler.Home.HomeAPI;
import ru.jl1mbo.AnarchyCore.Manager.WorldSystem.WorldSystemAPI;
import ru.jl1mbo.AnarchyCore.Utils.ConfigUtils;

public class HomeCommand extends Command {

	public HomeCommand() {
		super("home", "§r§fТелепортироватся домой");
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (!HomeAPI.isHome(player.getName())) {
				player.sendMessage(HomeAPI.PREFIX + "§fТочек дома не обнаружено§7!\n§l§6• §r§fДля создания точки Дома используйте §7/§6sethome");
			} else {
				player.sendMessage(HomeAPI.PREFIX + "§fВы успешно телепортированы домой§7!");
				player.teleport(HomeAPI.getHomePosition(player.getName()).add(0.5, 0, 0.5));
			}
		}
		return false;
	}
}