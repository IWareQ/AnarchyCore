package Anarchy.Module.Commands.EnderChest;

import Anarchy.Manager.FakeChests.FakeChestsAPI;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class EnderChestCommand extends Command {
	public EnderChestCommand() {
		super("ec", "Эндер сундук");
		setPermission("Command.EnderChest");
		commandParameters.clear();
	}

	@Override
	public boolean execute(CommandSender commandSender, String s, String[] strings) {
		if (!commandSender.hasPermission("Command.EnderChest")) {
			return false;
		}

		Player player = (Player) commandSender;
		EnderChest enderChest = new EnderChest("Эндер Сундук");
		enderChest.setContents(player.getEnderChestInventory().getContents());
		FakeChestsAPI.openInventory((Player) commandSender, enderChest);
		return false;
	}
}