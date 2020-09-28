package Anarchy.Module.Commands.EnderChest;

import Anarchy.Manager.FakeChests.FakeChestsAPI;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.Listener;

public class EnderChestCommand extends Command implements Listener {
	
	public EnderChestCommand() {
		super("ec", "§r§l§fОткрыть Сундук края");
		this.setPermission("Command.EnderChest");
		this.commandParameters.clear();
	}
	
	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("§l§7(§3Система§7) §r§fЭту команду можно использовать только в §3Игре");
			return true;
		}
		Player player = (Player)sender;
		if (!player.hasPermission("Command.EnderChest")) {
			return false;
		}
		EnderChest enderChest = new EnderChest("Сундук края");
		enderChest.setContents(player.getEnderChestInventory().getContents());
		FakeChestsAPI.openInventory(player, enderChest);
		return false;
	}
}