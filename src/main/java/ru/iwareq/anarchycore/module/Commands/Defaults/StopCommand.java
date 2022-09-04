package ru.iwareq.anarchycore.module.Commands.Defaults;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import ru.iwareq.anarchycore.module.CombatLogger.CombatLoggerAPI;

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
		for (Player player : sender.getServer().getOnlinePlayers().values()) {
			CombatLoggerAPI.removeCombat(player);
			player.close(player.getLeaveMessage(), "§l§6Перезагрузка");
		}
		Server.getInstance().shutdown();
		return false;
	}
}