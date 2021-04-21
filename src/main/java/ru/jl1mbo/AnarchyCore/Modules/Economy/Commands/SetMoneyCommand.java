package ru.jl1mbo.AnarchyCore.Modules.Economy.Commands;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import ru.jl1mbo.AnarchyCore.Modules.Auth.AuthAPI;
import ru.jl1mbo.AnarchyCore.Modules.Economy.EconomyAPI;
import ru.jl1mbo.AnarchyCore.Utils.Utils;

public class SetMoneyCommand extends Command {

	public SetMoneyCommand() {
		super("setmoney", "§rУстановить баланс");
		this.setPermission("Command.SetMoney");
		this.commandParameters.clear();
		this.commandParameters.put("setmoney", new CommandParameter[]{CommandParameter.newType("money", CommandParamType.INT), CommandParameter.newType("player", CommandParamType.TARGET)});
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
		sender.sendMessage(EconomyAPI.PREFIX + "Игрок §6" + targetName + " §fтеперь имеет §6" + String.format("%.1f", Double.parseDouble(args[0])) + "");
		EconomyAPI.setMoney(targetName, Double.parseDouble(args[0]));
		return false;
	}
}