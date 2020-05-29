package Anarchy.Module.Commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.level.Sound;

public class RepairCommand extends Command {
	public RepairCommand() {
		super("repair", "Починка предмета в руке");
		setPermission("Command.Repair");
		commandParameters.clear();
	}

	@Override
	public boolean execute(CommandSender commandSender, String s, String[] strings) {
		if (!commandSender.hasPermission("Command.Repair")) {
			return false;
		}

		PlayerInventory inventory = ((Player) commandSender).getInventory();
		Item item = inventory.getItemInHand();
		if (!item.isArmor() && !item.isTool()) {
			commandSender.sendMessage("§l§e| §r§fЧинить можно только §9Инструменты §fи §cБроню");
			return false;
		}

		Player player = (Player) commandSender;
		item.setDamage(0);
		inventory.setItemInHand(item);
		commandSender.sendMessage("§l§a| §r§fПредмет в руке починен!");
		player.getLevel().addSound(player, Sound.RANDOM_ANVIL_USE, 1, 1, player);
		return false;
	}
}