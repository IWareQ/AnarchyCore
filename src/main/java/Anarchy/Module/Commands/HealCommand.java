package Anarchy.Module.Commands;

import Anarchy.Manager.Sessions.PlayerSessionManager;
import Anarchy.Task.HotbarTask;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class HealCommand extends Command {
	public HealCommand() {
		super("heal", "Восполнение здоровья");
		setPermission("Command.Heal");
		commandParameters.clear();
	}

	@Override
	public boolean execute(CommandSender commandSender, String s, String[] strings) {
		if (!commandSender.hasPermission("Command.Heal")) {
			return false;
		}

		if (((Player) commandSender).getGamemode() != 0) {
			commandSender.sendMessage("§l§a| §r§fДля использования перейдите в §2Выживание");
			return false;
		}

		((Player) commandSender).setHealth(20);
		commandSender.sendMessage("§l§a| §r§fВы пополнили уровень Вашего здоровья");
		return false;
	}
}