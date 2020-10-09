package Anarchy.Module.Commands;

import Anarchy.Manager.Sessions.PlayerSessionManager;
import Anarchy.Manager.Sessions.Session.PlayerSession;
import Anarchy.Module.Permissions.PermissionsAPI;
import Anarchy.Utils.StringUtils;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class ACommand extends Command {

	public ACommand() {
		super("a", "§l§fАдмин-Чат");
		this.setPermission("Command.A");
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player)sender;
			if (!player.hasPermission("Command.A")) {
				return false;
			}
			if (args.length < 1) {
				player.sendMessage("§l§6| §r§fИспользование §7- /§6a §7(§3текст§7)");
				return true;
			}
			PlayerSession playerSession = PlayerSessionManager.getPlayerSession(player.getName());
			String displayName = PermissionsAPI.GROUPS.get(playerSession.getInteger("Permission")) + " §f" + player.getName();
			String message = StringUtils.implode(args, 0);
			for (Player adminChat : Server.getInstance().getOnlinePlayers().values()) {
				if (adminChat.hasPermission("Command.A")) {
					adminChat.sendMessage("§cⒶ " + displayName + " §8» §f" + message);
				}
			}
		}
		return false;
	}
}