package ru.jl1mbo.AnarchyCore.Modules.Commands.Home.Commands;

import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Position;
import ru.jl1mbo.AnarchyCore.Manager.WorldSystem.WorldSystemAPI;
import ru.jl1mbo.AnarchyCore.Modules.Commands.Home.HomeAPI;
import ru.jl1mbo.AnarchyCore.Modules.Cooldown.CooldownAPI;

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
				Map<String, String> homeData = HomeAPI.getHomeData(player.getName());
				player.teleport(new Position(Integer.parseInt(homeData.get("X")) + 0.5, Integer.parseInt(homeData.get("Y")), Integer.parseInt(homeData.get("Z")) + 0.5, WorldSystemAPI.getMap()));
				CooldownAPI.addCooldown(player, this.getName(), 20);
			} else {
				player.sendMessage(HomeAPI.PREFIX + "Точек дома не обнаружено§7!\n§l§6• §rДля создания точки Дома используйте §7/§6sethome");
			}
		}
		return false;
	}
}