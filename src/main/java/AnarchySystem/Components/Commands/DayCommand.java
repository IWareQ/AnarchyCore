package AnarchySystem.Components.Commands;

import java.util.HashMap;

import AnarchySystem.Manager.WorldSystem.WorldSystemAPI;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class DayCommand extends Command {
	private static HashMap<Player, Long> COOLDOWN = new HashMap<>();

	public DayCommand() {
		super("day", "§r§fСменить §9Ночь §fна §eДень");
		this.setPermission("Command.Day");
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (!player.hasPermission("Command.Day")) {
				return false;
			}
			Long cooldownTime = COOLDOWN.get(player);
			long nowTime = System.currentTimeMillis() / 1000;
			if (cooldownTime != null && cooldownTime > nowTime) {
				player.sendMessage("§l§7(§3Задержка§7) §r§fСледующее использование будет доступно через §6" + (cooldownTime - nowTime) + " §fсек§7.");
				return false;
			}
			WorldSystemAPI.getMap().setTime(1000);
			COOLDOWN.put(player, nowTime + 600);
			Server.getInstance().broadcastMessage("§l§6• §r§fИгрок §6" + player.getName() + " §fустановил§7(§fа§7) §eДень");
		}
		return false;
	}
}