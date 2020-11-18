package Anarchy.Module.Commands.EnderChest;

import Anarchy.Module.Commands.EnderChest.Utils.EnderChest;
import FakeInventoryAPI.FakeInventoryAPI;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Sound;

public class EnderChestCommand extends Command {

	public EnderChestCommand() {
		super("enderchest", "§r§fОткрыть Сундук края", "", new String[] {"ec"});
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
			EnderChest enderChest = new EnderChest("§r§fСундук края");
			enderChest.setContents(player.getEnderChestInventory().getContents());
			player.getLevel().addSound(player, Sound.RANDOM_ENDERCHESTOPEN, 1, 1, player);
			FakeInventoryAPI.openInventory(player, enderChest);
		}
		return false;
	}
}