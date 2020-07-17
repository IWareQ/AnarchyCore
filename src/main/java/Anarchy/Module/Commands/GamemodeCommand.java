package Anarchy.Module.Commands;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class GamemodeCommand extends Command {
	
	public GamemodeCommand() {
		super("gm", "Сменить режим игры");
		setPermission("Command.Gamemode");
		commandParameters.clear();
	}
	
	@Override()
	public boolean execute(CommandSender commandSender, String s, String[] strings) {
		if (!commandSender.hasPermission("Command.Gamemode")) {
			return false;
		}
		if (strings.length == 0) {
			commandSender.sendMessage("§l§e| §r§fИспользование §7- §e/gm §60§7/§61§7/§62§7/§63");
			return true;
		}
		Player player = (Player)commandSender;
		String playerName = player.getName();
		switch (strings[0]) {
			case "0": 
			{
				player.setGamemode(0);
				player.sendMessage("§l§a| §r§fРежим §7(§eВыживание§7) §6успешно §fвключен");
				for (Player admin : Server.getInstance().getOnlinePlayers().values()) {
					if (admin.hasPermission("Admin.Chat")) {
						admin.sendTip("§l§7(§cА§7) §fАдминистратор §e" + playerName + " §7- §fсменил режим игры на §7(§eВыживание§7)");
					}
				}
			}
			break;
			
			case "1": 
			{
				player.setGamemode(1);
				player.sendMessage("§l§a| §r§fРежим §7(§eТворчество§7) §6успешно §fвключен");
				for (Player admin : Server.getInstance().getOnlinePlayers().values()) {
					if (admin.hasPermission("Admin.Chat")) {
						admin.sendPopup("§l§7(§cА§7) §fАдминистратор §e" + playerName + " §7- §fсменил режим игры на §7(§eТворчество§7)");
					}
				}
			}
			break;
			
			case "2": 
			{
				player.setGamemode(2);
				player.sendMessage("§l§a| §r§fРежим §7(§eПриключение§7) §6успешно §fвключен");
				for (Player admin : Server.getInstance().getOnlinePlayers().values()) {
					if (admin.hasPermission("Admin.Chat")) {
						admin.sendPopup("§l§7(§cА§7) §fАдминистратор §e" + playerName + " §7- §fсменил режим игры на §7(§eПриключение§7)");
					}
				}
			}
			break;
			
			case "3": 
			{
				player.setGamemode(3);
				player.sendMessage("§l§a| §r§fРежим §7(§eСпектатор§7) §6успешно §fвключен");
				for (Player admin : Server.getInstance().getOnlinePlayers().values()) {
					if (admin.hasPermission("Admin.Chat")) {
						admin.sendPopup("§l§7(§cА§7) §fАдминистратор §e" + playerName + " §7- §fсменил режим игры на §7(§eСпектор§7)");
					}
				}
			}
			break;
			
		}
		return false;
	}
}