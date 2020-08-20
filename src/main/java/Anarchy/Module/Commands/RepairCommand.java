package Anarchy.Module.Commands;

import java.util.HashMap;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.level.Sound;

public class RepairCommand extends Command {
    public static Map<CommandSender, Long> COOLDOWN = new HashMap<>();
    public static int ADD_COOLDOWN = 100;
	
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
		Long cooldownTime = COOLDOWN.get(commandSender);
		Long nowTime = System.currentTimeMillis() / 1000L;
			if (cooldownTime != null && cooldownTime > nowTime) {
				commandSender.sendMessage("§l§7(§3Задержка§7) §r§fСледующее использование будет доступно через §3" + (cooldownTime - nowTime) + " §fсекунд");
				return false;
			}
		PlayerInventory inventory = ((Player)commandSender).getInventory();
		Item item = inventory.getItemInHand();
		if (!item.isArmor() && !item.isTool()) {
			commandSender.sendMessage("§l§e| §r§fЧинить можно только §3Инструменты §fи §3Броню");
			return false;
		}
		Player player = (Player)commandSender;
		item.setDamage(0);
		inventory.setItemInHand(item);
		commandSender.sendMessage("§l§a| §r§fПредмет в руке починен§7!");
		player.getLevel().addSound(player, Sound.RANDOM_ANVIL_USE, 1, 1, player);
		COOLDOWN.put(commandSender, nowTime + ADD_COOLDOWN);
		return false;
	}
}