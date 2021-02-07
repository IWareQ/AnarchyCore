package ru.jl1mbo.AnarchyCore.CommandsHandler;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemArmor;
import cn.nukkit.item.ItemTool;
import cn.nukkit.level.Sound;

import java.util.Map;

public class RepairCommand extends Command {

	public RepairCommand() {
		super("repair", "§r§fПочинка ппредметов");
		this.setPermission("Command.Repair");
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (!player.hasPermission("Command.Repair")) {
				return false;
			}
			PlayerInventory inventory = player.getInventory();
			Item item = inventory.getItemInHand();
			if (args.length == 0) {
				if (!(item instanceof ItemTool) && !(item instanceof ItemArmor)) {
					player.sendMessage("§l§6• §r§fЧинить можно только §6Инструменты §fи §6Броню§7!");
					return false;
				}
				if (player.getExperienceLevel() >= 2) {
					item.setDamage(0);
					inventory.setItemInHand(item);
					player.sendExperienceLevel(player.getExperienceLevel() - 2);
					player.sendMessage("§l§a• §r§fПредмет в руке починен§7!");
					player.getLevel().addSound(player, Sound.RANDOM_ANVIL_USE, 1, 1, player);
				} else {
					player.sendMessage("§l§c• §r§fВам не хватает §6уровней для починки предмета§7!");
				}
			}
			if (args.length == 1 && args[0].equals("all")) {
				Map<Integer, Item> contents = player.getInventory().getContents();
				if (player.getExperienceLevel() >= 10) {
					for (Item items : contents.values()) {
						if (items instanceof ItemTool || items instanceof ItemArmor) {
							player.sendExperienceLevel(player.getExperienceLevel() - 10);
							items.setDamage(0);
							player.getInventory().setContents(contents);
						}
					}
					player.sendMessage("§l§a• §r§fВсе предметы в §6Инвентаре §fпочинены§7!");
					player.getLevel().addSound(player, Sound.RANDOM_ANVIL_USE, 1, 1, player);
				} else {
					player.sendMessage("§l§c• §r§fВам не хватает §6уровней для починки предмета§7!");
				}
			}
		}
		return false;
	}
}