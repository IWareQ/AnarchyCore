package Anarchy.Module.Commands.Home;

import Anarchy.Functions.FunctionsAPI;
import Anarchy.Module.Commands.Home.Utils.HomeUtils;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Position;

public class HomeCommand extends Command {

	public HomeCommand() {
		super("home", "§r§fТелепортироватся домой");
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player)sender;
			if (!HomeAPI.playerIsHome(player.getName())) {
				player.sendMessage(HomeAPI.PREFIX + "§fТочек дома не обнаружено§7!\n§l§6• §r§fДля создания точки Дома используйте §7/§6sethome");
			} else {
				HomeUtils homeUtils = HomeAPI.getHomeUtils(player.getName());
				player.sendMessage(HomeAPI.PREFIX + "§fВы успешно телепортированы домой§7!");
				player.teleport(new Position(homeUtils.getX() + 0.5, homeUtils.getY(), homeUtils.getZ() + 0.5, FunctionsAPI.MAP));
			}
		}
		return false;
	}
}