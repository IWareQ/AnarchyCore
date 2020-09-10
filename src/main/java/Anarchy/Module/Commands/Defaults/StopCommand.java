package Anarchy.Module.Commands.Defaults;

import Anarchy.Module.CombatLogger.CombatLoggerAPI;
import Anarchy.Module.Commands.Spectate.SpectateAPI;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class StopCommand extends Command {
	
	public StopCommand() {
		super("stop", "\u00a7l\u00a7f\u0420\u0443\u0447\u043d\u0430\u044f \u043f\u0435\u0440\u0435\u0437\u0430\u0433\u0440\u0443\u0437\u043a\u0430");
		setPermission("Command.Stop");
		commandParameters.clear();
	}
	
	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (!sender.hasPermission("Command.Stop")) {
			return false;
		}
		for (Player players : Server.getInstance().getOnlinePlayers().values()) {
			CombatLoggerAPI.removeCombat(players);
			SpectateAPI.removeSpectate(players);
			players.close("", "\u00a7l\u00a7f\u041d\u0435 \u0431\u043e\u0439\u0442\u0435\u0441\u044c\u00a77, \u00a7f\u044d\u0442\u043e \u043f\u0440\u043e\u0441\u0442\u043e \u0440\u0443\u0447\u043d\u0430\u044f \u043f\u0435\u0440\u0435\u0437\u0430\u0433\u0440\u0443\u0437\u043a\u0430\u00a77!\n\u00a7f\u0412\u044b \u043d\u0435 \u0443\u043c\u0440\u0435\u0442\u0435, \u0435\u0441\u043b\u0438 \u0432\u0430\u0441 \u043a\u0438\u043a\u043d\u0443\u043b\u043e \u0432 \u043a\u0442\u00a77!");
		}
		sender.getServer().shutdown();
		return false;
	}
}