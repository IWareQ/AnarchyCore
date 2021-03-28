package ru.jl1mbo.AnarchyCore.Modules.Commands.EnderChest;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import ru.jl1mbo.AnarchyCore.Manager.FakeInventory.FakeInventoryAPI;
import ru.jl1mbo.AnarchyCore.Modules.Commands.EnderChest.Inventory.EnderChest;

public class EnderChestCommand extends Command {

	public EnderChestCommand() {
		super("enderchest", "§rОткрыть Сундук края", "", new String[] {"ec"});
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
			EnderChest enderChest = new EnderChest();
			enderChest.setContents(player.getEnderChestInventory().getContents());
			FakeInventoryAPI.openInventory(player, enderChest);
		}
		return false;
	}
}