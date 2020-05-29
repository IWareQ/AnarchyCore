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
		super("seemoney", "Просмотр баланса");
		this.setPermission("Command.SeeMoney");
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (!sender.hasPermission("Command.SeeMoney")) {
			return true;
		}

		if (args.length<1) {
			sender.sendMessage("§l§e| §fИспользование §7- §e/seemoney <игрок>");
			return true;
		}

		Player player = Server.getInstance().getPlayer(args[0]);
		if (player != null) {
			sender.sendMessage(EconomyAPI.PREFIX + "§fБаланс Игрока §a" + player.getName() + " §7- §e" + EconomyAPI.myMoney(player) + "");
			return true;
		}

		String nickname = StringUtils.implode(args, 0);
		if (!AuthAPI.isRegistered(nickname)) {
			sender.sendMessage(EconomyAPI.PREFIX + "§fИгрок §a" + nickname + " §7- §fне зарегистрирован!");
			return true;
		}

		sender.sendMessage(EconomyAPI.PREFIX + "§fБаланс Игрока §a" + nickname + " §7- §e" + EconomyAPI.myMoney(nickname) + "");
		return true;
	}
}