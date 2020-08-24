package Anarchy.Module.Commands.EnderChest;

import Anarchy.Manager.FakeChests.FakeChestsAPI;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class EnderChestCommand extends Command {
	
	public EnderChestCommand() {
		super("ec", "Открыть Эндер Сундук");
		setPermission("Command.EnderChest");
		commandParameters.clear();
	}
	
	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		Player player = (Player)sender;
		if (!player.hasPermission("Command.EnderChest")) {
			return false;
		}
		EnderChest enderChest = new EnderChest("Эндер Сундук");
		enderChest.setContents(player.getEnderChestInventory().getContents());
		FakeChestsAPI.openInventory(player, enderChest);
		return false;
	}
}