package Anarchy.Module.Commands;

import java.util.HashMap;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemArmor;
import cn.nukkit.item.ItemTool;
import cn.nukkit.level.Sound;

public class RepairCommand extends Command {
	public static Map<Player, Long> COOLDOWN = new HashMap<>();
	public static int ADD_COOLDOWN = 60;
	
	public RepairCommand() {
		super("repair", "\u00a7l\u00a7f\u041f\u043e\u0447\u0438\u043d\u0438\u0442\u044c \u043f\u0440\u0435\u0434\u043c\u0435\u0442 \u0432 \u0440\u0443\u043a\u0435\u00a77/\u00a7f\u0438\u043d\u0432\u0435\u043d\u0442\u043e\u0440\u0435");
		setPermission("Command.Repair");
	}
	
	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		Player player = (Player)sender;
		if (!(sender instanceof Player)) {
			sender.sendMessage("\u00a7l\u00a77(\u00a73\u0421\u0438\u0441\u0442\u0435\u043c\u0430\u00a77) \u00a7r\u00a7f\u042d\u0442\u0443 \u043a\u043e\u043c\u0430\u043d\u0434\u0443 \u043c\u043e\u0436\u043d\u043e \u0438\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u0442\u044c \u0442\u043e\u043b\u044c\u043a\u043e \u0432 \u00a73\u0418\u0433\u0440\u0435");
			return true;
		}
		if (!player.hasPermission("Command.Repair")) {
			return false;
		}
		Long cooldownTime = COOLDOWN.get(player);
		Long nowTime = System.currentTimeMillis() / 1000L;
		if (cooldownTime != null && cooldownTime > nowTime) {
			player.sendMessage("\u00a7l\u00a77(\u00a73\u0417\u0430\u0434\u0435\u0440\u0436\u043a\u0430\u00a77) \u00a7r\u00a7f\u0421\u043b\u0435\u0434\u0443\u044e\u0449\u0435\u0435 \u0438\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u0435 \u0431\u0443\u0434\u0435\u0442 \u0434\u043e\u0441\u0442\u0443\u043f\u043d\u043e \u0447\u0435\u0440\u0435\u0437 \u00a76" + (cooldownTime - nowTime) + " \u00a7f\u0441\u0435\u043a\u00a77.");
			return false;
		}
		PlayerInventory inventory = player.getInventory();
		Item item = inventory.getItemInHand();
		if (!(item instanceof ItemTool) && !(item instanceof ItemArmor)) {
			player.sendMessage("\u00a7l\u00a76| \u00a7r\u00a7f\u0427\u0438\u043d\u0438\u0442\u044c \u043c\u043e\u0436\u043d\u043e \u0442\u043e\u043b\u044c\u043a\u043e \u00a73\u0418\u043d\u0441\u0442\u0440\u0443\u043c\u0435\u043d\u0442\u044b \u00a7f\u0438 \u00a73\u0411\u0440\u043e\u043d\u044e");
			return false;
		}
		item.setDamage(0);
		inventory.setItemInHand(item);
		player.sendMessage("\u00a7l\u00a7a| \u00a7r\u00a7f\u041f\u0440\u0435\u0434\u043c\u0435\u0442 \u0432 \u0440\u0443\u043a\u0435 \u043f\u043e\u0447\u0438\u043d\u0435\u043d\u00a77!");
		player.getLevel().addSound(player, Sound.RANDOM_ANVIL_USE, 1, 1, player);
		COOLDOWN.put(player, nowTime + ADD_COOLDOWN);
		if (args[0] == "all") {
			Map<Integer, Item> contents = player.getInventory().getContents();
			for (Item items : contents.values()) {
				if (items instanceof ItemTool || items instanceof ItemArmor) {
					items.setDamage(0);
				}
			}
			player.getInventory().setContents(contents);
			player.sendMessage("\u00a7l\u00a7a| \u00a7r\u00a7f\u0412\u0441\u0435 \u043f\u0440\u0435\u0434\u043c\u0435\u0442\u044b \u0432 \u0418\u043d\u0432\u0435\u043d\u0442\u043e\u0440\u0435 \u0443\u0441\u043f\u0435\u0448\u043d\u043e \u043f\u043e\u0447\u0438\u043d\u0435\u043d\u044b\u00a77!");
			player.getLevel().addSound(player, Sound.RANDOM_ANVIL_USE, 1, 1, player);
			COOLDOWN.put(player, nowTime + ADD_COOLDOWN);
		} else {
			player.sendMessage("\u00a7l\u00a76| \u00a7r\u00a7f\u0418\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u0435 \u00a77- \u00a76/repair \u00a77(\u00a73all\u00a77)");
		}
		return false;
	}
}