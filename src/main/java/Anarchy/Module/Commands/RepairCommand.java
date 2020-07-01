package Anarchy.Module.Commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.level.Sound;

public class RepairCommand extends Command {
	
	public RepairCommand() {
		super("repair", "Починить предмет в руке");
		setPermission("Command.Repair");
		commandParameters.clear();
	}
	
	@Override()
	public boolean execute(CommandSender commandSender, String s, String[] strings) {
		if (!commandSender.hasPermission("Command.Repair")) {
			return false;
		}
		PlayerInventory inventory = ((Player)commandSender).getInventory();
		Item item = inventory.getItemInHand();
		if (!item.isArmor() && !item.isTool()) {
			commandSender.sendMessage("§l§e| §r§fЧинить можно только §6Инструменты §fи §6Броню");
			return false;
		}
		Player player = (Player)commandSender;
		item.setDamage(0);
		inventory.setItemInHand(item);
		commandSender.sendMessage("§l§a| §r§fПредмет в руке починен§7!");
		player.getLevel().addSound(player, Sound.RANDOM_ANVIL_USE, 1, 1, player);
		return false;
	}
}