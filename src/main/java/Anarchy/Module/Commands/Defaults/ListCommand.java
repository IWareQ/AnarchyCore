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
		Player player = (Player)sender;
		StringBuilder stringBuilder = new StringBuilder();
		int onlineCount = 0;
		for (Player players : sender.getServer().getOnlinePlayers().values()) {
			if (players.isOnline() && PlayerSessionManager.hasPlayerSession(players)) {
				stringBuilder.append("§7, §3").append(players.getDisplayName());
			}
			++onlineCount;
		}
		player.sendMessage("§l§e| §r§fНа сервере §3" + onlineCount + " §fиз §3" + Server.getInstance().getMaxPlayers() + " §fИгроков\n§fИгроки §7- §3" + (stringBuilder.length() > 0 ? stringBuilder.substring(4) : "§7..."));
		return true;
	}
}