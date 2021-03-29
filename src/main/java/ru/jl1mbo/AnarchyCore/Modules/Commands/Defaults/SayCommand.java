package ru.jl1mbo.AnarchyCore.Modules.Commands.Defaults;

import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import ru.jl1mbo.AnarchyCore.Utils.Utils;

public class SayCommand extends Command {

	public SayCommand() {
		super("say", "§rСообщить нужную информацию на весь сервер");
		this.setPermission("Command.Say");
		this.commandParameters.clear();
		this.commandParameters.put("say", new CommandParameter[] {CommandParameter.newType("message", CommandParamType.MESSAGE)});
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (!sender.hasPermission(this.getPermission())) {
			return false;
		}
		if (args.length < 1) {
			sender.sendMessage("§l§6• §rИспользование §7- /§6say §7(§6текст§7)");
			return true;
		}
		Server.getInstance().broadcastMessage("§l§7(§3Сервер§7) §r" + Utils.implode(args, 0));
		return false;
	}
}