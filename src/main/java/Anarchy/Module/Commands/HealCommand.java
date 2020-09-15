package Anarchy.Module.Commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class HealCommand extends Command {
	
	public HealCommand() {
		super("heal", "Восстановить Здоровье");
		this.setPermission("Command.Heal");
		this.commandParameters.clear();
	}
	
	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		Player player = (Player)sender;
		if (!(sender instanceof Player)) {
			sender.sendMessage("§l§7(§3Система§7) §r§fЭту команду можно использовать только в §3Игре");
			return true;
		}
		if (!player.hasPermission("Command.Heal")) {
			return false;
		}
		if (player.getGamemode() != 0) {
			player.sendMessage("§l§6| §r§fДля использования перейдите в §3Выживание");
			return false;
		}
		player.setHealth(20);
		player.sendMessage("§l§a| §r§fВы §3успешно §fпополнили уровень Вашего §6Здоровья");
		return false;
	}
}