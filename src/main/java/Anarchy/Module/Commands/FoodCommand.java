package Anarchy.Module.Commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Sound;

public class FoodCommand extends Command {

	public FoodCommand() {
		super("food", "§r§l§fВосстановить Голод");
		this.setPermission("Command.Food");
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player)sender;
			if (!player.hasPermission("Command.Food")) {
				return false;
			}
			if (player.getGamemode() != 0) {
				player.sendMessage("§l§6• §r§fДля использования перейдите в §6Выживание");
				return false;
			}
			player.getFoodData().setLevel(player.getFoodData().getMaxLevel(), 12.0F);
			player.sendMessage("§l§6• §fУровень Вашего §6Голода §fуспешно пополнен§7!");
			player.getLevel().addSound(player, Sound.RANDOM_EAT, 1, 1, player);
		}
		return false;
	}
}