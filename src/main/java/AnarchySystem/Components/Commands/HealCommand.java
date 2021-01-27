package AnarchySystem.Components.Commands;

import java.util.HashMap;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class HealCommand extends Command {
	private static HashMap<Player, Long> COOLDOWN = new HashMap<>();

	public HealCommand() {
		super("heal", "§r§fВосстановить Здоровье");
		this.setPermission("Command.Heal");
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (!player.hasPermission("Command.Heal")) {
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
			player.setHealth(20);
			COOLDOWN.put(player, nowTime + 600);
			player.sendMessage("§l§6• §fУровень Вашего §6Здоровья §fуспешно пополнен§7!");
		}
		return false;
	}
}