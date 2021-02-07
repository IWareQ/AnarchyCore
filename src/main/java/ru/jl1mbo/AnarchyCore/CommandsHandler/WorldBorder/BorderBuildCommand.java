package ru.jl1mbo.AnarchyCore.CommandsHandler.WorldBorder;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import ru.jl1mbo.AnarchyCore.CommandsHandler.WorldBorder.Task.BorderBuildTask;

public class BorderBuildCommand extends Command {

	public BorderBuildCommand() {
		super("borderbuild", "§r§fПостроить границу во всех мирах");
		this.setPermission("Command.BorderBuild");
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			player.sendMessage("§l§6• §r§fНачинаем строительство барьера§7...");
			Server.getInstance().getScheduler().scheduleRepeatingTask(new BorderBuildTask(), 20);
		}
		return false;
	}
}