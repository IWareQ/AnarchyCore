package Anarchy.Module.Commands.Defaults;

import Anarchy.Manager.Sessions.PlayerSessionManager;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class ListCommand extends Command {
	
	public ListCommand() {
		super("list", "Список игроков онлайн");
		commandParameters.clear();
	}
	
	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		StringBuilder players = new StringBuilder();
		int onlineCount = 0;
		for (Player player : sender.getServer().getOnlinePlayers().values()) {
			if (player.isOnline() && PlayerSessionManager.hasPlayerSession(player)) {
				players.append("§7, §3").append(player.getDisplayName());
			}
			++onlineCount;
		}
		sender.sendMessage("§l§e| §r§fНа сервере §e" + onlineCount + " §fиз §e" + Server.getInstance().getMaxPlayers() + " §fИгроков\n§fИгроки §7- §3" + (players.length() > 0 ? players.substring(4) : "§7..."));
		return true;
	}
}