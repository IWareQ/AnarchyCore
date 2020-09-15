package Anarchy.Module.Commands;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class GamemodeCommand extends Command {
	
	public GamemodeCommand() {
		super("gm", "§l§fСменить режим игры");
		this.setPermission("Command.Gamemode");
		this.commandParameters.clear();
	}
	
	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		Player player = (Player)sender;
		String playerName = player.getName();
		if (!(sender instanceof Player)) {
			sender.sendMessage("§l§7(§3Система§7) §r§fЭту команду можно использовать только в §3Игре");
			return true;
		}
		if (!player.hasPermission("Command.Gamemode")) {
			return false;
		}
		if (args.length == 0) {
			player.sendMessage("§l§6| §r§fИспользование §7- /§6gm §30§7/§31§7/§32§7/§33");
			return true;
		}
		switch (args[0]) {
			case "0": 
			{
				player.setGamemode(0);
				player.sendMessage("§l§a| §r§fРежим §7(§6Выживание§7) §3успешно §fвключен");
				for (Player admin : Server.getInstance().getOnlinePlayers().values()) {
					if (admin.hasPermission("Admin.Chat")) {
						admin.sendTip("§l§7(§cА§7) §fАдминистратор §3" + playerName + " §7- §fсменил режим игры на §7(§6Выживание§7)");
					}
				}
			}
			break;
			
			case "1": 
			{
				if (!player.hasPermission("Access.Admin")) {
					player.sendMessage("§l§6| §r§fТворческий режим может использовать только §6Основатель §fи §4Администратор§7!");
				} else {
					player.setGamemode(1);
					player.sendMessage("§l§a| §r§fРежим §7(§6Творчество§7) §3успешно §fвключен");
					for (Player admin : Server.getInstance().getOnlinePlayers().values()) {
						if (admin.hasPermission("Admin.Chat")) {
							admin.sendTip("§l§7(§cА§7) §fАдминистратор §e" + playerName + " §7- §fсменил режим игры на §7(§eТворчество§7)");
						}
					}
				}
			}
			break;
			
			case "2": 
			{
				player.setGamemode(2);
				player.sendMessage("§l§a| §r§fРежим §7(§eПриключение§7) §3успешно §fвключен");
				for (Player admin : Server.getInstance().getOnlinePlayers().values()) {
					if (admin.hasPermission("Admin.Chat")) {
						admin.sendTip("§l§7(§cА§7) §fАдминистратор §e" + playerName + " §7- §fсменил режим игры на §7(§eПриключение§7)");
					}
				}
			}
			break;
			
			case "3": 
			{
				player.setGamemode(3);
				player.sendMessage("§l§a| §r§fРежим §7(§eСпектатор§7) §3успешно §fвключен");
				for (Player admin : Server.getInstance().getOnlinePlayers().values()) {
					if (admin.hasPermission("Admin.Chat")) {
						admin.sendTip("§l§7(§cА§7) §fАдминистратор §e" + playerName + " §7- §fсменил режим игры на §7(§eСпектор§7)");
					}
				}
			}
			break;
			
		}
		return false;
	}
}