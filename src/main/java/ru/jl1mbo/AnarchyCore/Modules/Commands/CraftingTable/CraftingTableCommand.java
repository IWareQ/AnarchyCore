package ru.jl1mbo.AnarchyCore.Modules.Commands.CraftingTable;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import ru.jl1mbo.AnarchyCore.Manager.FakeInventory.FakeInventoryAPI;
import ru.jl1mbo.AnarchyCore.Modules.Commands.CraftingTable.Inventory.CraftingTable;

public class CraftingTableCommand extends Command {

	public CraftingTableCommand() {
		super("craftingtable", "§rОткрыть верстак", "", new String[] {"craft"});
		this.setPermission("Command.CraftingTable");
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (!player.hasPermission(this.getPermission())) {
				return false;
			}
			FakeInventoryAPI.openInventory(player, new CraftingTable());
		}
		return false;
	}
}