package ru.jl1mbo.AnarchyCore.Modules.Commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Sound;
import ru.jl1mbo.AnarchyCore.Modules.Cooldown.CooldownAPI;

public class FoodCommand extends Command {

	public FoodCommand() {
		super("food", "§rВосстановить Голод");
		this.setPermission("Command.Food");
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (!player.hasPermission(this.getPermission())) {
				return false;
			}
			player.getFoodData().setLevel(player.getFoodData().getMaxLevel(), 12.0F);
			player.sendMessage("§l§6• §rУровень Вашего §6голода §fуспешно пополнен§7!");
			player.getLevel().addSound(player, Sound.RANDOM_EAT, 1, 1, player);
			CooldownAPI.addCooldown(player, this.getName(), 360);
		}
		return false;
	}
}