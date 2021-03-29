package ru.jl1mbo.AnarchyCore.Modules.Commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import ru.jl1mbo.AnarchyCore.Modules.Cooldown.CooldownAPI;

public class HealCommand extends Command {

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
			if (player.getGamemode() != 0) {
				player.sendMessage("§l§6• §rДля использования перейдите в §6Выживание§7!");
				return true;
			}
			player.setHealth(player.getMaxHealth());
			player.sendMessage("§l§6• §rУровень Вашего §6Здоровья §fуспешно пополнен§7!");
			CooldownAPI.addCooldown(player, this.getName(), 480);
		}
		return false;
	}
}