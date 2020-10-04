package Anarchy.Module.Commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class ClearChatCommand extends Command {
	
	public ClearChatCommand() {
		super("clearchat", "§r§l§fОчистить чат для себя", "", new String[]{"cc"});
		this.setPermission("Command.ClearChat");
		this.commandParameters.clear();
	}
	
	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		Player player = (Player)sender;
		if (!player.hasPermission("Command.ClearChat")) {
			return false;
		}
		player.sendMessage("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n§l§6• §r§fЧат был успешно очищен§7!");
		return false;
	}
}