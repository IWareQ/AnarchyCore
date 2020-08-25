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
	public boolean execute(CommandSender sender, String label, String[] args) {
		Player player = (Player)sender;
		if (!player.hasPermission("Command.Repair")) {
			return false;
		}
		Long cooldownTime = COOLDOWN.get(player);
		Long nowTime = System.currentTimeMillis() / 1000L;
			if (cooldownTime != null && cooldownTime > nowTime) {
				player.sendMessage("§l§7(§3Задержка§7) §r§fСледующее использование будет доступно через §3" + (cooldownTime - nowTime) + " §fсек§7.");
				return false;
			}
		PlayerInventory inventory = player.getInventory();
		Item item = inventory.getItemInHand();
		if (!item.isArmor() && !item.isTool()) {
			player.sendMessage("§l§6| §r§fЧинить можно только §3Инструменты §fи §3Броню");
			return false;
		}
		item.setDamage(0);
		inventory.setItemInHand(item);
		player.sendMessage("§l§a| §r§fПредмет в руке починен§7!");
		player.getLevel().addSound(player, Sound.RANDOM_ANVIL_USE, 1, 1, player);
		COOLDOWN.put(player, nowTime + ADD_COOLDOWN);
		return false;
	}
}