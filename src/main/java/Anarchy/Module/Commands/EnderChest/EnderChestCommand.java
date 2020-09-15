package Anarchy.Module.Commands.EnderChest;

import Anarchy.Manager.FakeChests.FakeChestsAPI;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class EnderChestCommand extends Command {
	
	public EnderChestCommand() {
		super("ec", "§l§fОткрыть Эндер Сундук");
		this.setPermission("Command.EnderChest");
		this.commandParameters.clear();
	}
	
	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		Player player = (Player)sender;
		if (!(sender instanceof Player)) {
			sender.sendMessage("§l§7(§3Система§7) §r§fЭту команду можно использовать только в §3Игре");
			return true;
		}
		if (!player.hasPermission("Command.EnderChest")) {
			return false;
		}
		EnderChest enderChest = new EnderChest("§l§fЭндер Сундук");
		enderChest.setContents(player.getEnderChestInventory().getContents());
		FakeChestsAPI.openInventory(player, enderChest);
		return false;
	}
}