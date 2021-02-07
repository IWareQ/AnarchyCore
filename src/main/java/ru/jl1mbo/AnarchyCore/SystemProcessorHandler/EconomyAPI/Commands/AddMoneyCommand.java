package ru.jl1mbo.AnarchyCore.SystemProcessorHandler.EconomyAPI.Commands;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.EconomyAPI.EconomyAPI;
import ru.jl1mbo.AnarchyCore.Utils.Utils;

public class AddMoneyCommand extends Command {

	public AddMoneyCommand() {
		super("addmoney", "§r§fВыдача монет");
		this.setPermission("Command.AddMoney");
		this.commandParameters.clear();
		this.commandParameters.put("default", new CommandParameter[] {new CommandParameter("money", CommandParamType.INT, false), new CommandParameter("player", CommandParamType.TARGET, false)});
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (!sender.hasPermission(this.getPermission())) {
			return false;
		}
		if (args.length < 2) {
			sender.sendMessage("§l§6• §r§fИспользование §7- /§6addmoney §7(§6сумма§7) (§6игрок§7)");
			return true;
		}
		String nickname = Utils.implode(args, 1);
		sender.sendMessage(EconomyAPI.PREFIX + "§fИгрок §6" + nickname + " §fполучил §6" + String.format("%.1f", Double.parseDouble(args[0])) + "");
		Player target = Server.getInstance().getPlayerExact(nickname);
		if (target != null) {
			target.sendMessage(EconomyAPI.PREFIX + "§fВаш баланс пополнен на §6" + String.format("%.1f", Double.parseDouble(args[0])) + "");
		}
		EconomyAPI.addMoney(nickname, Double.parseDouble(args[0]));
		return false;
	}
}