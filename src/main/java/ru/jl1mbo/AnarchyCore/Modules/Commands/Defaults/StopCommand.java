package ru.jl1mbo.AnarchyCore.Modules.Commands.Defaults;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import ru.jl1mbo.AnarchyCore.Task.RestartTask;

public class StopCommand extends Command {

	public StopCommand() {
		super("stop", "§rРучная перезагрузка");
		this.setPermission("Command.Stop");
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (!sender.hasPermission(this.getPermission())) {
			return false;
		}
		sender.sendMessage("§l§7(§3Перезагрузка§7) §rНачинается принудительная §6Перезагрузка§7!");
		RestartTask.seconds = 11;
		return false;
	}
}