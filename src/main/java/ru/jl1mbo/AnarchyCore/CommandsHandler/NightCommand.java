package ru.jl1mbo.AnarchyCore.CommandsHandler;

import java.util.HashMap;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import ru.jl1mbo.AnarchyCore.Manager.WorldSystem.WorldSystemAPI;

public class NightCommand extends Command {
	private static HashMap<Player, Long> COOLDOWN = new HashMap<>();

	public NightCommand() {
		super("night", "§r§fСменить §eДень §fна §9Ночь");
		this.setPermission("Command.Night");
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (!player.hasPermission("Command.Night")) {
				return false;
			}
			Long cooldownTime = COOLDOWN.get(player);
			long nowTime = System.currentTimeMillis() / 1000;
			if (cooldownTime != null && cooldownTime > nowTime) {
				player.sendMessage("§l§7(§3Задержка§7) §r§fСледующее использование будет доступно через §6" + (cooldownTime - nowTime) + " §fсек§7.");
				return false;
			}
			WorldSystemAPI.getMap().setTime(14000);
			COOLDOWN.put(player, nowTime + 600);
			Server.getInstance().broadcastMessage("§l§6• §r§fИгрок §6" + player.getName() + " §fустановил§7(§fа§7) §9Ночь");
		}
		return false;
	}
}