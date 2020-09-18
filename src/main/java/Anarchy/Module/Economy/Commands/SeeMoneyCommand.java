package Anarchy.Module.Economy.Commands;

import Anarchy.Module.Auth.AuthAPI;
import Anarchy.Module.Economy.EconomyAPI;
import Anarchy.Utils.StringUtils;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;

public class SeeMoneyCommand extends Command {
	
	public SeeMoneyCommand() {
		super("seemoney", "§l§fПосмотреть баланс Игрока");
		this.setPermission("Command.SeeMoney");
		this.commandParameters.clear();
		this.commandParameters.put("default", new CommandParameter[]{new CommandParameter("player", CommandParamType.TARGET, false)});
	}
	
	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		Player player = (Player)sender;
		if (!(sender instanceof Player)) {
			sender.sendMessage("§l§7(§3Система§7) §r§fЭту команду можно использовать только в §3Игре");
			return true;
		}
		if (!player.hasPermission("Command.SeeMoney")) {
			return true;
		}
		if (args.length < 1) {
			player.sendMessage("§l§6| §r§fИспользование §7- /§6seemoney §7(§3игрок§7)");
			return true;
		}
		String nickname = StringUtils.implode(args, 0);
		if (!AuthAPI.isRegistered(nickname)) {
			player.sendMessage(EconomyAPI.PREFIX + "§fИгрок §3" + nickname + " §7- §fне зарегистрирован§7!");
			return true;
		}
		player.sendMessage(EconomyAPI.PREFIX + "§fБаланс Игрока §3" + nickname + " §7- §6" + String.format("%.1f", EconomyAPI.myMoney(nickname)) + "");
		return true;
	}
}