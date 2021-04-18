package ru.jl1mbo.AnarchyCore.Modules.Commands;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import ru.jl1mbo.AnarchyCore.Manager.WorldSystem.WorldSystemAPI;
import ru.jl1mbo.AnarchyCore.Modules.Cooldown.CooldownAPI;

public class DayCommand extends Command {

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
			WorldSystemAPI.Map.setTime(0);
			Server.getInstance().broadcastMessage("§l§6• §rИгрок §6" + player.getName() + " §fустановил§7(§fа§7) §6День");
			CooldownAPI.addCooldown(player, this.getName(), 600);
		}
		return false;
	}
}