package ru.jl1mbo.AnarchyCore.CommandsHandler;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class NearCommand extends Command {
	private static int RADIUS = 50;

	public NearCommand() {
		super("near", "§rУзнать кол§7-§fво игроков которые рядом");
		this.setPermission("Command.Near");
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (!player.hasPermission("Command.Near")) {
				return false;
			}
			if (player.hasPermission("Command.Near.100")) {
				RADIUS = 100;
			} else if (player.hasPermission("Command.Near.150")) {
				RADIUS = 150;
			} else {
				RADIUS = 50;
			}
			int count = 0;
			for (Player players : Server.getInstance().getOnlinePlayers().values()) {
				if (players.distance(player) <= RADIUS && players.getGamemode() != 3) {
					count += 1;
				}
			}
			player.sendMessage("§l§6• §rКол§7-§fво игроков в радиусе §6" + RADIUS + " §fблоков§7 - §6" + count);
			count = 0;
		}
		return false;
	}
}