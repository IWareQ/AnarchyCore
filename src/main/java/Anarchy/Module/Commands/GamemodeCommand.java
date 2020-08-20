package Anarchy.Module.Commands;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class GamemodeCommand extends Command {
	
	public GamemodeCommand() {
		super("gm", "\u0421\u043c\u0435\u043d\u0438\u0442\u044c \u0440\u0435\u0436\u0438\u043c \u0438\u0433\u0440\u044b");
		setPermission("Command.Gamemode");
		commandParameters.clear();
	}
	
	@Override()
	public boolean execute(CommandSender commandSender, String label, String[] args) {
		Player player = (Player)commandSender;
		String playerName = player.getName();
		if (!player.hasPermission("Command.Gamemode")) {
			return false;
		}
		if (args.length == 0) {
			player.sendMessage("\u00a7l\u00a7e| \u00a7r\u00a7f\u0418\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u0435 \u00a77- \u00a7e/gm \u00a730\u00a77/\u00a731\u00a77/\u00a732\u00a77/\u00a733");
			return true;
		}
		switch (args[0]) {
			case "0": 
			{
				player.setGamemode(0);
				player.sendMessage("\u00a7l\u00a7a| \u00a7r\u00a7f\u0420\u0435\u0436\u0438\u043c \u00a77(\u00a7e\u0412\u044b\u0436\u0438\u0432\u0430\u043d\u0438\u0435\u00a77) \u00a73\u0443\u0441\u043f\u0435\u0448\u043d\u043e \u00a7f\u0432\u043a\u043b\u044e\u0447\u0435\u043d");
				for (Player admin : Server.getInstance().getOnlinePlayers().values()) {
					if (admin.hasPermission("Admin.Chat")) {
						admin.sendTip("\u00a7l\u00a77(\u00a7c\u0410\u00a77) \u00a7f\u0410\u0434\u043c\u0438\u043d\u0438\u0441\u0442\u0440\u0430\u0442\u043e\u0440 \u00a7e" + playerName + " \u00a77- \u00a7f\u0441\u043c\u0435\u043d\u0438\u043b \u0440\u0435\u0436\u0438\u043c \u0438\u0433\u0440\u044b \u043d\u0430 \u00a77(\u00a7e\u0412\u044b\u0436\u0438\u0432\u0430\u043d\u0438\u0435\u00a77)");
					}
				}
			}
			break;
			
			case "1": 
			{
				if (!player.hasPermission("Access.Admin")) {
					player.sendMessage("\u00a7l\u00a7e| \u00a7r\u00a7f\u0422\u0432\u043e\u0440\u0447\u0435\u0441\u043a\u0438\u0439 \u0440\u0435\u0436\u0438\u043c \u043c\u043e\u0436\u0435\u0442 \u0438\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u0442\u044c \u0442\u043e\u043b\u044c\u043a\u043e \u00a76\u041e\u0441\u043d\u043e\u0432\u0430\u0442\u0435\u043b\u044c \u00a7f\u0438 \u00a74\u0410\u0434\u043c\u0438\u043d\u0438\u0441\u0442\u0440\u0430\u0442\u043e\u0440\u00a77!");
				} else {
					player.setGamemode(1);
					player.sendMessage("\u00a7l\u00a7a| \u00a7r\u00a7f\u0420\u0435\u0436\u0438\u043c \u00a77(\u00a7e\u0422\u0432\u043e\u0440\u0447\u0435\u0441\u0442\u0432\u043e\u00a77) \u00a73\u0443\u0441\u043f\u0435\u0448\u043d\u043e \u00a7f\u0432\u043a\u043b\u044e\u0447\u0435\u043d");
					for (Player admin : Server.getInstance().getOnlinePlayers().values()) {
						if (admin.hasPermission("Admin.Chat")) {
							admin.sendTip("\u00a7l\u00a77(\u00a7c\u0410\u00a77) \u00a7f\u0410\u0434\u043c\u0438\u043d\u0438\u0441\u0442\u0440\u0430\u0442\u043e\u0440 \u00a7e" + playerName + " \u00a77- \u00a7f\u0441\u043c\u0435\u043d\u0438\u043b \u0440\u0435\u0436\u0438\u043c \u0438\u0433\u0440\u044b \u043d\u0430 \u00a77(\u00a7e\u0422\u0432\u043e\u0440\u0447\u0435\u0441\u0442\u0432\u043e\u00a77)");
						}
					}
				}
			}
			break;
			
			case "2": 
			{
				player.setGamemode(2);
				player.sendMessage("\u00a7l\u00a7a| \u00a7r\u00a7f\u0420\u0435\u0436\u0438\u043c \u00a77(\u00a7e\u041f\u0440\u0438\u043a\u043b\u044e\u0447\u0435\u043d\u0438\u0435\u00a77) \u00a73\u0443\u0441\u043f\u0435\u0448\u043d\u043e \u00a7f\u0432\u043a\u043b\u044e\u0447\u0435\u043d");
				for (Player admin : Server.getInstance().getOnlinePlayers().values()) {
					if (admin.hasPermission("Admin.Chat")) {
						admin.sendTip("\u00a7l\u00a77(\u00a7c\u0410\u00a77) \u00a7f\u0410\u0434\u043c\u0438\u043d\u0438\u0441\u0442\u0440\u0430\u0442\u043e\u0440 \u00a7e" + playerName + " \u00a77- \u00a7f\u0441\u043c\u0435\u043d\u0438\u043b \u0440\u0435\u0436\u0438\u043c \u0438\u0433\u0440\u044b \u043d\u0430 \u00a77(\u00a7e\u041f\u0440\u0438\u043a\u043b\u044e\u0447\u0435\u043d\u0438\u0435\u00a77)");
					}
				}
			}
			break;
			
			case "3": 
			{
				player.setGamemode(3);
				player.sendMessage("\u00a7l\u00a7a| \u00a7r\u00a7f\u0420\u0435\u0436\u0438\u043c \u00a77(\u00a7e\u0421\u043f\u0435\u043a\u0442\u0430\u0442\u043e\u0440\u00a77) \u00a73\u0443\u0441\u043f\u0435\u0448\u043d\u043e \u00a7f\u0432\u043a\u043b\u044e\u0447\u0435\u043d");
				for (Player admin : Server.getInstance().getOnlinePlayers().values()) {
					if (admin.hasPermission("Admin.Chat")) {
						admin.sendTip("\u00a7l\u00a77(\u00a7c\u0410\u00a77) \u00a7f\u0410\u0434\u043c\u0438\u043d\u0438\u0441\u0442\u0440\u0430\u0442\u043e\u0440 \u00a7e" + playerName + " \u00a77- \u00a7f\u0441\u043c\u0435\u043d\u0438\u043b \u0440\u0435\u0436\u0438\u043c \u0438\u0433\u0440\u044b \u043d\u0430 \u00a77(\u00a7e\u0421\u043f\u0435\u043a\u0442\u043e\u0440\u00a77)");
					}
				}
			}
			break;
			
		}
		return false;
	}
}