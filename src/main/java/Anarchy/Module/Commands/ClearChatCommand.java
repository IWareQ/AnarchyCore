package Anarchy.Module.Commands;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class ClearChatCommand extends Command {
	
	public ClearChatCommand() {
		super("clearchat", "§l§fОчистить чат для всех", "", new String[]{"cc"});
		this.setPermission("Command.ClearChat");
		this.commandParameters.clear();
	}
	
	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		Player player = (Player)sender;
		if (!player.hasPermission("Command.ClearChat")) {
			return false;
		}
		Server.getInstance().broadcastMessage("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n§fЧат был §3успешно §fочищен§7!");
		return false;
	}
}