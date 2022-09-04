package ru.iwareq.anarchycore.module.Commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.item.Item;
import cn.nukkit.nbt.tag.CompoundTag;
import ru.iwareq.anarchycore.module.Cooldown.CooldownAPI;

public class DisenchantCommand extends Command {

	public DisenchantCommand() {
		super("disenchant", "§rУдалить зачарование с предмета в руке");
		this.setPermission("Command.Disenchant");
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (!player.hasPermission(this.getPermission())) {
				return false;
			}
			Item itemInHand = player.getInventory().getItemInHand();
			if (itemInHand.getId() == Item.AIR) {
				player.sendMessage("§l§6• §rДля использования возьмите предмет в руку§7!");
				return true;
			}

			if (itemInHand.getEnchantments().length == 0) {
				player.sendMessage("§l§6• §rЭтот предмет не зачарован§7!");
				return true;
			}

			CompoundTag namedTag = itemInHand.getNamedTag();
			if (namedTag != null) {
				itemInHand.setNamedTag(namedTag.remove("ench"));
			}
			player.getInventory().setItemInHand(itemInHand);
			player.sendMessage("§l§6• §rВсе зачарования убраны§7!");
			CooldownAPI.addCooldown(player, this.getName(), 4 * 60 * 60);
		}
		return false;
	}
}