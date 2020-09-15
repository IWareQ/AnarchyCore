package Anarchy.Module.Commands;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class NearCommand extends Command {
	public static int RADIUS = 70;
	
	public NearCommand() {
		super("near", "Посмотреть кто рядом с тобой");
		this.setPermission("Command.Near");
		this.commandParameters.clear();
	}
	
	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		Player player = (Player)sender;
		StringBuilder stringBuilder = new StringBuilder();
		if (!(sender instanceof Player)) {
			sender.sendMessage("§l§7(§3Система§7) §r§fЭту команду можно использовать только в §3Игре");
			return true;
		}
		if (!player.hasPermission("Command.Near")) {
			return false;
		}
		for (Player players : Server.getInstance().getOnlinePlayers().values()) {
			if (players.distance(player) < RADIUS && players.gamemode != 3) {
				stringBuilder.append("§7, §6").append(players.getName());
			}
		}
		player.sendMessage("§l§6| §r§fИгроки в радиусе §3" + RADIUS + " §fблоков §7- §6" + (stringBuilder.length() != 0 ? stringBuilder.toString().substring(4) : "..."));
		return false;
	}
}