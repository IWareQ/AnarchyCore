package Anarchy.Module.Commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class HealCommand extends Command {

	public HealCommand() {
		super("heal", "§r§fВосстановить Здоровье");
		this.setPermission("Command.Heal");
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player)sender;
			if (!player.hasPermission("Command.Heal")) {
				return false;
			}
			if (player.getGamemode() != 0) {
				player.sendMessage("§l§6• §r§fДля использования перейдите в §6Выживание");
				return false;
			}
			player.setHealth(20);
			player.sendMessage("§l§6• §fУровень Вашего §6Здоровья §fуспешно пополнен§7!");
		}
		return false;
	}
}