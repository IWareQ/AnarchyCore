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
	public boolean execute(CommandSender commandSender, String s, String[] strings) {
		if (!commandSender.hasPermission("Command.Heal")) {
			return false;
		}
		Player player = (Player)commandSender;
		if (player.getGamemode() != 0) {
			commandSender.sendMessage("§l§a| §r§fДля использования перейдите в §eВыживание");
			return false;
		}
		player.setHealth(20);
		commandSender.sendMessage("§l§a| §r§fВы §6успешно §fпополнили уровень Вашего §eЗдоровья");
		return false;
	}
}