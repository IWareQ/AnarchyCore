package ru.iwareq.anarchycore.module.Commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.level.Sound;
import ru.iwareq.anarchycore.module.Cooldown.CooldownAPI;

import java.util.Map;

public class RepairCommand extends Command {

	public RepairCommand() {
		super("repair", "§rПочинка предметов");
		this.setPermission("Command.Repair");
		this.commandParameters.clear();
	}

	private static double convert(Player player) {
		return convertLevelToExperience(player.getExperienceLevel()) + player.getExperience();
	}

	private static double convertLevelToExperience(double level) {
		double experience = 0;
		if (level <= 16) {
			experience = (level * level) + 6 * level;
		} else if (level >= 17 && level <= 31) {
			experience = 2.5 * (level * level) - 40.5 * level + 360;
		} else if (level >= 32) {
			experience = 4.5 * (level * level) - 162.5 * level + 2220;
		}
		return experience;
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (!player.hasPermission(this.getPermission())) {
				return false;
			}
			PlayerInventory inventory = player.getInventory();
			Item item = inventory.getItemInHand();
			if (args.length == 0) {
				if (!item.isTool() && !item.isArmor()) {
					player.sendMessage("§l§6• §rЧинить можно только §6Инструменты §fи §6Броню§7!");
					return true;
				}
				if (convert(player) >= item.getDamage() / 5) {
					double xpPlayer = convert(player);
					player.setExperience(0, 0);
					player.addExperience((int) xpPlayer - item.getDamage() / 5);
					item.setDamage(0);
					inventory.setItemInHand(item);
					player.sendMessage("§l§6• §rПредмет в руке починен§7!");
					player.getLevel().addSound(player, Sound.RANDOM_ANVIL_USE, 1, 1, player);
					CooldownAPI.addCooldown(player, this.getName(), 60);
				} else {
					player.sendMessage("§l§6• §rВам не хватает §6опыта §fдля починки предмета§7!");
				}
			}
			if (args.length == 1 && args[0].equals("all")) {
				Map<Integer, Item> contents = inventory.getContents();
				int exp = 0;
				for (Item items : contents.values()) {
					if (items.isTool() || items.isArmor()) {
						exp += items.getDamage() / 10;
					}
				}
				if (convert(player) >= exp) {
					for (Item items : contents.values()) {
						if (items.isTool() || items.isArmor()) {
							double xpPlayer = convert(player);
							player.setExperience(0, 0);
							player.addExperience((int) xpPlayer - exp);
							items.setDamage(0);
							player.getInventory().setContents(contents);
							exp = 0;
						}
					}
					player.sendMessage("§l§6• §rВсе предметы в §6Инвентаре §fпочинены§7!");
					player.getLevel().addSound(player, Sound.RANDOM_ANVIL_USE, 1, 1, player);
					CooldownAPI.addCooldown(player, this.getName(), 180);
				} else {
					player.sendMessage("§l§6• §rВам не хватает §6опыта §fдля починки предметов§7!");
				}
			}
		}
		return false;
	}
}