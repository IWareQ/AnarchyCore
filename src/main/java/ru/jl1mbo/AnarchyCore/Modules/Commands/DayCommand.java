package ru.jl1mbo.AnarchyCore.Modules.Commands;

import java.util.HashMap;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import ru.jl1mbo.AnarchyCore.Manager.WorldSystem.WorldSystemAPI;

public class DayCommand extends Command {
	private static HashMap<String, Long> COOLDOWN = new HashMap<>();

	public DayCommand() {
		super("day", "§rСменить §9Ночь §fна §eДень");
		this.setPermission("Command.Day");
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (!player.hasPermission(this.getPermission())) {
				return false;
			}
			if (COOLDOWN.containsKey(player.getName()) && COOLDOWN.get(player.getName()) >= System.currentTimeMillis() / 1000L) {
				player.sendMessage("§l§7(§3Задержка§7) §rСледующее использование будет доступно через §6" + (COOLDOWN.get(player.getName()) - System.currentTimeMillis() / 1000L) + " §fсек§7.");
				return true;
			}
			WorldSystemAPI.getMap().setTime(1000);
			COOLDOWN.put(player.getName(), System.currentTimeMillis() / 1000L + 600);
			Server.getInstance().broadcastMessage("§l§6• §rИгрок §6" + player.getName() + " §fустановил§7(§fа§7) §6День");
		}
		return false;
	}
}