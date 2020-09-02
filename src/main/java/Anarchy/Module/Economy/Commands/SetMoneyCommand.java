package Anarchy.Module.Economy.Commands;

import Anarchy.Module.Auth.AuthAPI;
import Anarchy.Module.Economy.EconomyAPI;
import Anarchy.Utils.StringUtils;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;

public class SetMoneyCommand extends Command {
	
	public SetMoneyCommand() {
		super("setmoney", "§l§fУстановить монет");
		this.setPermission("Command.SetMoney");
		this.commandParameters.put("default", new CommandParameter[]{new CommandParameter("player", CommandParamType.TARGET, false), new CommandParameter("money", CommandParamType.INT, false)});
	}
	
	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		Player player = (Player)sender;
		if (sender instanceof Player) {
			sender.sendMessage("§l§7(§3Система§7) §r§fЭту команду можно использовать только в §3Консоли");
			return true;
		}
		if (!player.hasPermission("Command.SetMoney")) {
			return true;
		}
		if (args.length < 2) {
			player.sendMessage("§l§6| §r§fИспользование §7- /§6setmoney §7(§игрок§7) (§3сумма§7)");
			return true;
		}
		String nickname = StringUtils.implode(args, 0);
		if (!AuthAPI.isRegistered(nickname)) {
			player.sendMessage(EconomyAPI.PREFIX + "§fИгрок §3" + nickname + " §7- §fне зарегистрирован§7!");
			return true;
		}
		player.sendMessage(EconomyAPI.PREFIX + "§fИгрок §3" + nickname + " §fтеперь имеет §6" + args[1] + "");
		EconomyAPI.setMoney(nickname, Double.parseDouble(args[1]));
		return true;
	}
}