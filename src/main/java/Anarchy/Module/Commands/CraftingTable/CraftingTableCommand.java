package Anarchy.Module.Commands.CraftingTable;

import Anarchy.Manager.FakeChests.FakeChestsAPI;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class CraftingTableCommand extends Command {

	public CraftingTableCommand() {
		super("CraftingTable", "§r§l§fОткрыть верстак", "", new String[] {"craft"});
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
			CraftingTable craftingTable = new CraftingTable("Верстак");
			FakeChestsAPI.openInventory(player, craftingTable);
		}
		return false;
	}
}