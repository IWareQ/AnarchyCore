package ru.iwareq.anarchycore.module.title.command;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import ru.iwareq.anarchycore.module.Auth.AuthAPI;
import ru.iwareq.anarchycore.util.Utils;

public class AddCasesCommand extends Command {

	public AddCasesCommand() {
		super("addcases", "Выдать кейсы с титулами");
		this.setPermission("command.addcases");

		this.commandParameters.clear();
		this.commandParameters.put("addcases", new CommandParameter[]{
				CommandParameter.newType("count", CommandParamType.INT),
				CommandParameter.newType("player", CommandParamType.TARGET)
		});
	}

	@Override
	public boolean execute(CommandSender sender, String alias, String[] args) {
		if (!sender.hasPermission(this.getPermission())) {
			return false;
		}

		if (args.length < 2) {
			sender.sendMessage("§l§6• §rИспользование §7- /§6addcases §7(§6кол-во§7) (§6игрок§7)");
			return true;
		}

		String targetName = Utils.implode(args, 1);
		if (!AuthAPI.isRegister(targetName)) {
			sender.sendMessage("Игрок §6" + targetName + " §fне зарегистрирован§7!");
			return true;
		}

		sender.sendMessage("Игрок §6" + targetName + " §fполучил §6" + args[0] + " кейсов");
		AuthAPI.setCases(targetName, AuthAPI.getCases(targetName) + Integer.parseInt(args[0]));
		return false;
	}
}
