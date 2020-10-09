package Anarchy.Module.Commands.Defaults;

import Anarchy.Manager.Sessions.AllSessionsManager;
import Anarchy.Task.RestartTask;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class StopCommand extends Command {
	
	public StopCommand() {
		super("stop", "§l§fРучная перезагрузка");
		this.setPermission("Command.Stop");
		this.commandParameters.clear();
	}
	
	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (!sender.hasPermission("Command.Stop")) {
			return false;
		}
		Server.getInstance().getScheduler().scheduleRepeatingTask(new RestartTask(), 20);
		AllSessionsManager.saveAllSessions();
		return false;
	}
}