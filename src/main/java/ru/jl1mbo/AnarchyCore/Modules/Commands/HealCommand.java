package ru.jl1mbo.AnarchyCore.Modules.Commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

import java.util.HashMap;

public class HealCommand extends Command {
	private static final HashMap<Player, Long> COOLDOWN = new HashMap<>();

	public HealCommand() {
		super("heal", "§rВосстановить Здоровье");
		this.setPermission("Command.Heal");
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
			player.setHealth(20);
			COOLDOWN.put(player, nowTime + 600);
			player.sendMessage("§l§6• §rУровень Вашего §6Здоровья §fуспешно пополнен§7!");
		}
		return false;
	}
}