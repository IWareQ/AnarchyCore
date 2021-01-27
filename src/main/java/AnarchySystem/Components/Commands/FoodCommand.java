package AnarchySystem.Components.Commands;

import java.util.HashMap;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Sound;
import cn.nukkit.network.protocol.PlaySoundPacket;

public class FoodCommand extends Command {
	private static HashMap<Player, Long> COOLDOWN = new HashMap<>();

	public FoodCommand() {
		super("food", "§r§fВосстановить Голод");
		this.setPermission("Command.Food");
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (!player.hasPermission("Command.Food")) {
				return false;
			}
			Long cooldownTime = COOLDOWN.get(player);
			long nowTime = System.currentTimeMillis() / 1000;
			if (cooldownTime != null && cooldownTime > nowTime) {
				player.sendMessage("§l§7(§3Задержка§7) §r§fСледующее использование будет доступно через §6" + (cooldownTime - nowTime) + " §fсек§7.");
				return false;
			}
			if (player.getGamemode() != 0) {
				player.sendMessage("§l§6• §r§fДля использования перейдите в §6Выживание");
				return false;
			}
			COOLDOWN.put(player, nowTime + 300);
			player.getFoodData().setLevel(player.getFoodData().getMaxLevel(), 12.0F);
			player.sendMessage("§l§6• §fУровень Вашего §6Голода §fуспешно пополнен§7!");
			player.getLevel().addSound(player, Sound.RANDOM_EAT, 1, 1, player);
		}
		return false;
	}
}