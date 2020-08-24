package Anarchy.Module.Commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class HealCommand extends Command {
	
	public HealCommand() {
		super("heal", "Восстановить Здоровье");
		setPermission("Command.Heal");
		commandParameters.clear();
	}
	
	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		Player player = (Player)sender;
		if (!(player instanceof Player)) {
			player.sendMessage("§fЭту команду можно использовать только в игре");
			return false;
		}
		if (!player.hasPermission("Command.Heal")) {
			return false;
		}
		if (player.getGamemode() != 0) {
			player.sendMessage("§l§a| §r§fДля использования перейдите в §eВыживание");
			return false;
		}
		player.setHealth(20);
		player.sendMessage("§l§a| §r§fВы §3успешно §fпополнили уровень Вашего §eЗдоровья");
		return false;
	}
}