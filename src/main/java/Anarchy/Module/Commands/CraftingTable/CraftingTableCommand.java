package Anarchy.Module.Commands.CraftingTable;

import Anarchy.Module.Commands.CraftingTable.Utils.CraftingTable;
import FakeInventoryAPI.FakeInventoryAPI;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class CraftingTableCommand extends Command {

	public CraftingTableCommand() {
		super("craftingtable", "§r§fОткрыть верстак", "", new String[] {"craft"});
		this.setPermission("Command.CraftingTable");
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player)sender;
			if (!player.hasPermission("Command.CraftingTable")) {
				return false;
			}
			CraftingTable craftingTable = new CraftingTable();
			FakeInventoryAPI.openInventory(player, craftingTable);
		}
		return false;
	}
}