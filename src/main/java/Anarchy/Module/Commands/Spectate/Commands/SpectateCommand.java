package Anarchy.Module.Commands.Spectate.Commands;

import Anarchy.AnarchyMain;
import Anarchy.Module.Commands.Spectate.SpectateAPI;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;

public class SpectateCommand extends Command {

	public SpectateCommand() {
		super("spectate", "Наблюдение за Игроком", "", new String[] {"sp"});
		this.setPermission("Command.Spectate");
		this.commandParameters.clear();
		this.commandParameters.put("default", new CommandParameter[] {new CommandParameter("player", CommandParamType.TARGET, false)});
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player)sender;
			if (!player.hasPermission("Command.Spectate")) {
				return false;
			}
			if (args.length != 1) {
				player.sendMessage("§l§6| §r§fИспользование §7- §6/sp §7(§3игрок§7)");
				return true;
			}
			Player target = Server.getInstance().getPlayer(args[0]);
			if (target == null) {
				player.sendMessage(AnarchyMain.PREFIX + "§fИгрок §6" + args[0] + " §fне в сети§7!");
				return true;
			}
			/*if (target.equals(player)) {
				player.sendMessage(AnarchyMain.PREFIX + "§fВы пытаетесь наблюдать за собой§7!");
				return true;
			}*/
			SpectateAPI.addSpectate(player, target);
		}
		return false;
	}
}