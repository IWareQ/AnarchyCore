package Anarchy.Module.Economy.Commands;

import Anarchy.Module.Auth.AuthAPI;
import Anarchy.Module.Economy.EconomyAPI;
import Anarchy.Utils.StringUtils;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class SetMoneyCommand extends Command {
	
	public SetMoneyCommand() {
		super("setmoney", "Установить монет");
		this.setPermission("Command.SetMoney");
	}
	
	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (!sender.hasPermission("Command.SetMoney")) {
			return true;
		}
		if (args.length < 2) {
			sender.sendMessage("§l§e| §r§fИспользование §7- §e/setmoney §7(§6сумма§7) (§6игрок§7)");
			return true;
		}
		String nickname = StringUtils.implode(args, 1);
		if (!AuthAPI.isRegistered(nickname)) {
			sender.sendMessage(EconomyAPI.PREFIX + "§fИгрок §e" + nickname + " §7- §fне зарегистрирован§7!");
			return true;
		}
		sender.sendMessage(EconomyAPI.PREFIX + "§fИгрок §e" + nickname + " §fтеперь имеет §e" + args[0] + "");
		EconomyAPI.setMoney(nickname, Integer.parseInt(args[0]));
		return true;
	}
}