package ru.jl1mbo.AnarchyCore.Modules.Commands.Home.Commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import ru.jl1mbo.AnarchyCore.Manager.WorldSystem.WorldSystemAPI;
import ru.jl1mbo.AnarchyCore.Modules.BlockProtection.BlockProtectionAPI;
import ru.jl1mbo.AnarchyCore.Modules.Commands.Home.HomeAPI;
import ru.jl1mbo.MySQLUtils.MySQLUtils;

public class SetHomeCommand extends Command {

	public SetHomeCommand() {
		super("sethome", "§r§fУстановить новую точку дома");
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (player.getLevel() != WorldSystemAPI.getMap()) {
				player.sendMessage(HomeAPI.PREFIX + "В этом мире §6запрещено §fустанавливать точки §6Дома§7!");
				return true;
			}
			if (!BlockProtectionAPI.canInteractHere(player, player.getLocation())) {
				player.sendMessage(HomeAPI.PREFIX + "Запрещено ставить §6точки дома §fв чужих регионах§7!");
				return true;
			}
			MySQLUtils.query("DELETE FROM `Homes` WHERE UPPER (`Name`) = '" + player.getName().toUpperCase() + "'");
			MySQLUtils.query("INSERT INTO `Homes` (`Name`, `X`, `Y`, `Z`) VALUES ('" + player.getName() + "', '" + player.getFloorX() + "', '" + player.getFloorY() + "', '" + player.getFloorZ() + "')");
			player.sendMessage(HomeAPI.PREFIX + "Новая точка дома §6упешно §fустановлена§7!");
		}
		return false;
	}
}