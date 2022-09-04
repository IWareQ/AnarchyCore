package ru.iwareq.anarchycore.module.Commands.EnderChest;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import ru.iwareq.anarchycore.manager.FakeInventory.FakeInventoryAPI;
import ru.iwareq.anarchycore.module.Commands.EnderChest.Inventory.EnderChest;

public class EnderChestCommand extends Command {

	public EnderChestCommand() {
		super("enderchest", "§rОткрыть Сундук края", "", new String[]{"ec"});
		this.setPermission("Command.EnderChest");
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (!player.hasPermission(this.getPermission())) {
				return false;
			}
			if (player.getFloorY() <= 256) {
				FakeInventoryAPI.openInventory(player, new EnderChest());
			}
		}
		return false;
	}
}