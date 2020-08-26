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
		super("seemoney", "§l§fПосмотреть баланс Игрока");
		this.setPermission("Command.SeeMoney");
	}
	
	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		Player player = (Player)sender;
		if (!player.hasPermission("Command.SeeMoney")) {
			return true;
		}
		if (args.length < 1) {
			sender.sendMessage("§l§6| §r§fИспользование §7- /§6seemoney §7(§3игрок§7)");
			return true;
		}
		Player seePlayer = Server.getInstance().getPlayer(args[0]);
		if (seePlayer != null) {
			player.sendMessage(EconomyAPI.PREFIX + "§fБаланс Игрока §3" + seePlayer.getName() + " §7- §6" + EconomyAPI.myMoney(seePlayer) + "");
			return true;
		}
		String nickname = StringUtils.implode(args, 0);
		if (!AuthAPI.isRegistered(nickname)) {
			player.sendMessage(EconomyAPI.PREFIX + "§fИгрок §3" + nickname + " §7- §fне зарегистрирован§7!");
			return true;
		}
		player.sendMessage(EconomyAPI.PREFIX + "§fБаланс Игрока §3" + nickname + " §7- §6" + EconomyAPI.myMoney(nickname) + "");
		return true;
	}
}