package ru.iwareq.anarchycore.module.Commands.Home.Commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import ru.iwareq.anarchycore.manager.WorldSystem.WorldSystemAPI;
import ru.iwareq.anarchycore.module.BlockProtection.BlockProtectionAPI;
import ru.iwareq.anarchycore.module.Commands.Home.HomeAPI;

public class SetHomeCommand extends Command {

	public SetHomeCommand() {
		super("sethome", "§r§fУстановить новую точку дома");
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (player.getLevel() != WorldSystemAPI.Map) {
				player.sendMessage(HomeAPI.PREFIX + "В этом мире §6запрещено §fустанавливать точки §6Дома§7!");
				return true;
			}

			if (!BlockProtectionAPI.canInteractHere(player, player.getLocation())) {
				player.sendMessage(HomeAPI.PREFIX + "Запрещено ставить §6точки дома §fв чужих регионах§7!");
				return true;
			}

			HomeAPI.deleteHome(player.getName());
			HomeAPI.createHome(player.getName(), player.getPosition());
			player.sendMessage(HomeAPI.PREFIX + "Новая точка дома §6упешно §fустановлена§7!");
		}

		return false;
	}
}
