package ru.iwareq.anarchycore.module.Economy.Commands;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import ru.iwareq.anarchycore.module.Auth.AuthAPI;
import ru.iwareq.anarchycore.module.Economy.EconomyAPI;
import ru.iwareq.anarchycore.util.Utils;

public class AddMoneyCommand extends Command {

	public AddMoneyCommand() {
		super("addmoney", "§rВыдача монет");
		this.setPermission("Command.AddMoney");
		this.commandParameters.clear();
		this.commandParameters.put("addmoney", new CommandParameter[]{
				CommandParameter.newType("money", CommandParamType.INT),
				CommandParameter.newType("player", CommandParamType.TARGET)
		});
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (!sender.hasPermission(this.getPermission())) {
			return false;
		}
		if (args.length < 2) {
			sender.sendMessage("§l§6• §rИспользование §7- /§6addmoney §7(§6сумма§7) (§6игрок§7)");
			return true;
		}
		String targetName = Utils.implode(args, 1);
		if (!AuthAPI.isRegister(targetName)) {
			sender.sendMessage(EconomyAPI.PREFIX + "Игрок §6" + targetName + " §fне зарегистрирован§7!");
			return true;
		}
		sender.sendMessage(EconomyAPI.PREFIX + "Игрок §6" + targetName + " §fполучил §6" + EconomyAPI.format(Double.parseDouble(args[0])) + "");
		Player target = Server.getInstance().getPlayer(targetName);
		if (target != null) {
			target.sendMessage(EconomyAPI.PREFIX + "Ваш баланс пополнен на §6" + EconomyAPI.format(Double.parseDouble(args[0])) + "");
		}
		EconomyAPI.addMoney(targetName, Double.parseDouble(args[0]));
		return false;
	}
}