package AnarchySystem.Components.Commands.Defaults;

import AnarchySystem.Utils.Utils;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class SayCommand extends Command {

	public SayCommand() {
		super("say", "§r§fСообщить нужную информацию на весь сервер");
		this.setPermission("Command.Say");
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (!sender.hasPermission("Command.Say")) {
			return false;
		}
		if (args[0].length() < 1) {
			sender.sendMessage("§l§6• §r§fИспользование §7- /§6say §7(§6текст§7)");
			return true;
		}
		String message = Utils.implode(args, 0);
		Server.getInstance().broadcastMessage("§l§7(§3Сервер§7) §r§f" + message);
		return false;
	}
}