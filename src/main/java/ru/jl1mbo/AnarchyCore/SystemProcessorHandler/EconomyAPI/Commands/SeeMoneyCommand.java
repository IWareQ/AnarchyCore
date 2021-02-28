package ru.jl1mbo.AnarchyCore.SystemProcessorHandler.EconomyAPI.Commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.EconomyAPI.EconomyAPI;
import ru.jl1mbo.AnarchyCore.Utils.Utils;

public class SeeMoneyCommand extends Command {

	public SeeMoneyCommand() {
		super("seemoney", "§rПросмотр баланса");
		this.setPermission("Command.SeeMoney");
		this.commandParameters.clear();
		this.commandParameters.put("default", new CommandParameter[] {new CommandParameter("player", CommandParamType.TARGET, false)});
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (!sender.hasPermission(this.getPermission())) {
			return true;
		}
		if (args.length < 1) {
			sender.sendMessage("§l§6• §rИспользование §7- /§6seemoney §7(§6игрок§7)");
			return true;
		}
		String nickname = Utils.implode(args, 0);
		if (!EconomyAPI.isRegister(nickname)) {
			sender.sendMessage(EconomyAPI.PREFIX + "Игрок §6" + nickname + " §fне зарегистрирован§7!");
			return true;
		}
		sender.sendMessage(EconomyAPI.PREFIX + "Баланс Игрока §6" + nickname + " §7- §6" + String.format("%.1f", EconomyAPI.myMoney(nickname)) + "");
		return false;
	}
}