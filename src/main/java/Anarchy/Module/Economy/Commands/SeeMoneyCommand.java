package Anarchy.Module.Economy.Commands;

import Anarchy.Module.Auth.AuthAPI;
import Anarchy.Module.Economy.EconomyAPI;
import Anarchy.Utils.StringUtils;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class SeeMoneyCommand extends Command {
	
	public SeeMoneyCommand() {
		super("seemoney", "Посмотреть баланс Игрока");
		this.setPermission("Command.SeeMoney");
	}
	
	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (!sender.hasPermission("Command.SeeMoney")) {
			return true;
		}
		if (args.length < 1) {
			sender.sendMessage("§l§e| §r§fИспользование §7- §e/seemoney §7(§3игрок§7)");
			return true;
		}
		Player player = Server.getInstance().getPlayer(args[0]);
		if (player != null) {
			sender.sendMessage(EconomyAPI.PREFIX + "§fБаланс Игрока §e" + player.getName() + " §7- §e" + EconomyAPI.myMoney(player) + "");
			return true;
		}
		String nickname = StringUtils.implode(args, 0);
		if (!AuthAPI.isRegistered(nickname)) {
			sender.sendMessage(EconomyAPI.PREFIX + "§fИгрок §e" + nickname + " §7- §fне зарегистрирован§7!");
			return true;
		}
		sender.sendMessage(EconomyAPI.PREFIX + "§fБаланс Игрока §e" + nickname + " §7- §e" + EconomyAPI.myMoney(nickname) + "");
		return true;
	}
}