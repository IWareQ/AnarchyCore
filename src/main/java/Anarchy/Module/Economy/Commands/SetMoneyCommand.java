package Anarchy.Module.Economy.Commands;

import Anarchy.Module.Auth.AuthAPI;
import Anarchy.Module.Economy.EconomyAPI;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;

public class SetMoneyCommand extends Command {
	
	public SetMoneyCommand() {
		super("setmoney", "§l§fУстановить монет");
		this.setPermission("Command.SetMoney");
		this.commandParameters.clear();
		this.commandParameters.put("default", new CommandParameter[]{new CommandParameter("player", CommandParamType.TARGET, false), new CommandParameter("money", CommandParamType.INT, false)});
	}
	
	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (!sender.hasPermission("Command.SetMoney")) {
			return true;
		}
		if (args.length < 2) {
			sender.sendMessage("§l§6| §r§fИспользование §7- /§6setmoney §7(§игрок§7) (§3сумма§7)");
			return true;
		}
		if (!AuthAPI.isRegistered(args[0])) {
			sender.sendMessage(EconomyAPI.PREFIX + "§fИгрок §6" + args[0] + " §fне зарегистрирован§7!");
			return true;
		}
		Player target = Server.getInstance().getPlayerExact(args[0]);
		sender.sendMessage(EconomyAPI.PREFIX + "§fИгрок §6" + target.getName() + " §fтеперь имеет §6" + String.format("%.1f", args[1]) + "");
		EconomyAPI.setMoney(target, Double.parseDouble(args[1]));
		return true;
	}
}