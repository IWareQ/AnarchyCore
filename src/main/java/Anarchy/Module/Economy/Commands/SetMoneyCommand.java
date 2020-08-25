package Anarchy.Module.Economy.Commands;

import Anarchy.Module.Auth.AuthAPI;
import Anarchy.Module.Economy.EconomyAPI;
import Anarchy.Utils.StringUtils;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class SetMoneyCommand extends Command {
	
	public SetMoneyCommand() {
		super("setmoney", "§l§fУстановить монет");
		this.setPermission("Command.SetMoney");
	}
	
	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		Player player = (Player)sender;
		if (!player.hasPermission("Command.SetMoney")) {
			return true;
		}
		if (args.length < 2) {
			player.sendMessage("§l§6| §r§fИспользование §7- /§6setmoney §7(§3сумма§7) (§3игрок§7)");
			return true;
		}
		String nickname = StringUtils.implode(args, 1);
		if (!AuthAPI.isRegistered(nickname)) {
			player.sendMessage(EconomyAPI.PREFIX + "§fИгрок §3" + nickname + " §7- §fне зарегистрирован§7!");
			return true;
		}
		player.sendMessage(EconomyAPI.PREFIX + "§fИгрок §3" + nickname + " §fтеперь имеет §6" + args[0] + "");
		EconomyAPI.setMoney(nickname, Integer.parseInt(args[0]));
		return true;
	}
}