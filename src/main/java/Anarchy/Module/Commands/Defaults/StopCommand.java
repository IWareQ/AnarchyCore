package Anarchy.Module.Commands.Defaults;

import Anarchy.Module.CombatLogger.CombatLoggerAPI;
import Anarchy.Module.Commands.Spectate.SpectateAPI;
import cn.nukkit.Player;
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
		for (Player players : Server.getInstance().getOnlinePlayers().values()) {
			CombatLoggerAPI.removeCombat(players);
			SpectateAPI.removeSpectate(players);
			players.close("", "§l§fНе бойтесь§7, §fэто просто ручная перезагрузка§7!\n§fВы не умрете, если вас кикнуло в кт§7!");
		}
		sender.getServer().shutdown();
		return false;
	}
}