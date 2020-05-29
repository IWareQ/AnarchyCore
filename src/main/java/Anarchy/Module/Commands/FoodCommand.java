package Anarchy.Module.Commands;

import Anarchy.Manager.Sessions.PlayerSessionManager;
import Anarchy.Task.HotbarTask;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Sound;

public class FoodCommand extends Command {
	public FoodCommand() {
		super("food", "Восполнение голода");
		setPermission("Command.Food");
		commandParameters.clear();
	}

	@Override
	public boolean execute(CommandSender commandSender, String s, String[] strings) {
		if (!commandSender.hasPermission("Command.Food")) {
			return false;
		}
		if (((Player) commandSender).getGamemode() != 0) {
			commandSender.sendMessage("§l§c| §r§fДля использования перейдите в §2Выживание");
			return false;
		}
		Player player = ((Player) commandSender);
		player.getFoodData().setLevel(player.getFoodData().getMaxLevel(), 12f);
		commandSender.sendMessage("§l§a| §r§fВы пополнили уровень Вашего голода");
		player.getLevel().addSound(player, Sound.RANDOM_EAT, 1, 1, player);
		return false;
	}
}