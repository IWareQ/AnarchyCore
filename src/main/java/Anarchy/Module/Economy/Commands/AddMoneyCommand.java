package Anarchy.Module.Economy.Commands;

import Anarchy.Module.Auth.AuthAPI;
import Anarchy.Module.Economy.EconomyAPI;
import Anarchy.Utils.StringUtils;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class AddMoneyCommand extends Command {
	
	public AddMoneyCommand() {
		super("addmoney", "Выдать Монет Игроку");
		this.setPermission("Command.AddMoney");
	}
	
	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (!sender.hasPermission("Command.AddMoney")) {
			return true;
		}
		if (args.length < 2) {
			sender.sendMessage("§l§e| §r§fИспользование §7- §e/addmoney §7(§3сумма§7) (§3игрок7)");
			return true;
		}
		String nickname = StringUtils.implode(args, 1);
		if (!AuthAPI.isRegistered(nickname)) {
			sender.sendMessage(EconomyAPI.PREFIX + "§fИгрок §e" + nickname + " §7- §fне зарегистрирован§7!");
			return true;
		}
		sender.sendMessage(EconomyAPI.PREFIX + "§fИгрок §e" + nickname + " §fполучил §e" + args[0] + "");
		EconomyAPI.setMoney(nickname, EconomyAPI.myMoney(nickname) + Integer.parseInt(args[0]));
		return true;
	}
}