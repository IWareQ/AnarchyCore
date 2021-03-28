package ru.jl1mbo.AnarchyCore.Modules.Commands;

import java.util.HashMap;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Sound;

public class FoodCommand extends Command {
	private static HashMap<Player, Long> COOLDOWN = new HashMap<>();

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
			Long cooldownTime = COOLDOWN.get(player);
			long nowTime = System.currentTimeMillis() / 1000;
			if (cooldownTime != null && cooldownTime > nowTime) {
				player.sendMessage("§l§7(§3Задержка§7) §rСледующее использование будет доступно через §6" + (cooldownTime - nowTime) + " §fсек§7.");
				return true;
			}
			if (player.getGamemode() != 0) {
				player.sendMessage("§l§6• §rДля использования перейдите в §6Выживание§7!");
				return true;
			}
			COOLDOWN.put(player, nowTime + 300);
			player.getFoodData().setLevel(player.getFoodData().getMaxLevel(), 12.0F);
			player.sendMessage("§l§6• §rУровень Вашего §6Голода §fуспешно пополнен§7!");
			player.getLevel().addSound(player, Sound.RANDOM_EAT, 1, 1, player);
		}
		return false;
	}
}