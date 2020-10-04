package Anarchy.Module.Commands.CraftingTable;

import Anarchy.Manager.FakeChests.FakeChestsAPI;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class CraftingTableCommand extends Command {

	public CraftingTableCommand() {
		super("CraftingTable", "§r§l§fОткрыть верстак");
		this.setPermission("Command.CraftingTable");
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("§l§7(§3Система§7) §r§fЭту команду можно использовать только в §3Игре");
			return true;
		}
		Player player = (Player)sender;
		if (!player.hasPermission("Command.CraftingTable")) {
			return false;
		}
		CraftingTable craftingTable = new CraftingTable("ВеРсТаК");
		FakeChestsAPI.openInventory(player, craftingTable);
		return false;
	}
}