package Anarchy.Module.Commands.EnderChest;

import Anarchy.Manager.FakeChests.FakeChestsAPI;
import Anarchy.Module.Commands.EnderChest.Utils.EnderChest;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Sound;

public class EnderChestCommand extends Command {

	public EnderChestCommand() {
		super("ec", "§r§l§fОткрыть Сундук края");
		this.setPermission("Command.EnderChest");
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player)sender;
			if (!player.hasPermission("Command.EnderChest")) {
				return false;
			}
			EnderChest enderChest = new EnderChest("Сундук края");
			enderChest.setContents(player.getEnderChestInventory().getContents());
			player.getLevel().addSound(player, Sound.RANDOM_ENDERCHESTOPEN, 1, 1, player);
			FakeChestsAPI.openInventory(player, enderChest);
		}
		return false;
	}
}