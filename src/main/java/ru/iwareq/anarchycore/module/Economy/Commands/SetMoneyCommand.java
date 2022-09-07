package ru.iwareq.anarchycore.module.Economy.Commands;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import ru.iwareq.anarchycore.module.Auth.AuthAPI;
import ru.iwareq.anarchycore.module.Economy.EconomyAPI;
import ru.iwareq.anarchycore.util.Utils;

public class SetMoneyCommand extends Command {

	public SetMoneyCommand() {
		super("setmoney", "§rУстановить баланс");
		this.setPermission("Command.SetMoney");
		this.commandParameters.clear();
		this.commandParameters.put("setmoney", new CommandParameter[]{
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
			sender.sendMessage("§l§6• §rИспользование §7- /§6setmoney §7(§6сумма§7) (§6игрок§7)");
			return true;
		}
		String targetName = Utils.implode(args, 1);
		if (!AuthAPI.isRegister(targetName)) {
			sender.sendMessage(EconomyAPI.PREFIX + "Игрок §6" + targetName + " §fне зарегистрирован§7!");
			return true;
		}
		sender.sendMessage(EconomyAPI.PREFIX + "Игрок §6" + targetName + " §fтеперь имеет §6" + EconomyAPI.format(Double.parseDouble(args[0])) + "");
		AuthAPI.setMoney(targetName, Double.parseDouble(args[0]));
		return false;
	}
}