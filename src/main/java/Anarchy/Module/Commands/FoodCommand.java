package Anarchy.Module.Commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Sound;

public class FoodCommand extends Command {
	
	public FoodCommand() {
		super("food", "Восстановить Голод");
		setPermission("Command.Food");
		commandParameters.clear();
	}
	
	@Override()
	public boolean execute(CommandSender commandSender, String s, String[] strings) {
		if (!commandSender.hasPermission("Command.Food")) {
			return false;
		}
		Player player = (Player)commandSender;
		if (player.getGamemode() != 0) {
			commandSender.sendMessage("§l§c| §r§fДля использования перейдите в §3Выживание");
			return false;
		}
		player.getFoodData().setLevel(player.getFoodData().getMaxLevel(), 12.0F);
		player.sendMessage("§l§a| §r§fВы §3успешно §fпополнили уровень Вашего §eГолода");
		player.getLevel().addSound(player, Sound.RANDOM_EAT, 1, 1, player);
		return false;
	}
}