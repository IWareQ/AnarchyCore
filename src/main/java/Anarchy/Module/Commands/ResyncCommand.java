package Anarchy.Module.Commands;

import Anarchy.AnarchyMain;
import Anarchy.Utils.SQLiteUtils;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class ResyncCommand extends Command {
	
	public ResyncCommand() {
		super("resync", "§l§fВводить при вайпе");
		this.setPermission("Command.Resync");
		this.commandParameters.clear();
	}
	
	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (!sender.hasPermission("Command.Resync")) {
			return false;
		}
		SQLiteUtils.query("Users.db", "UPDATE `USERS` SET `Money` = \'0\', `Gametime` = \'0\';");
		sender.sendMessage(AnarchyMain.PREFIX + "§fБаза данных успешно обновлена§7!");
		return false;
	}
}