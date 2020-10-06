package Anarchy.Module.Economy.Commands;

import Anarchy.Module.Auth.AuthAPI;
import Anarchy.Module.Economy.EconomyAPI;
import Anarchy.Utils.StringUtils;
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
		this.commandParameters.put("default", new CommandParameter[]{new CommandParameter("money", CommandParamType.INT, false), new CommandParameter("player", CommandParamType.TARGET, false)});
	}
	
	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (!sender.hasPermission("Command.SetMoney")) {
			return true;
		}
		if (args.length < 2) {
			sender.sendMessage("§l§6| §r§fИспользование §7- /§6setmoney §7(§3сумма§7) (§3игрок§7)");
			return true;
		}
		String nickname = StringUtils.implode(args, 1);
		if (!AuthAPI.isRegistered(nickname)) {
			sender.sendMessage(EconomyAPI.PREFIX + "§fИгрок §6" + nickname + " §fне зарегистрирован§7!");
			return true;
		}
		Player target = Server.getInstance().getPlayerExact(nickname);
		sender.sendMessage(EconomyAPI.PREFIX + "§fИгрок §6" + nickname + " §fтеперь имеет §6" + String.format("%.1f", args[1]) + "");
		EconomyAPI.setMoney(target, Double.parseDouble(String.format("%.1f", args[1])));
		return true;
	}
}