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

public class AddMoneyCommand extends Command {
	
	public AddMoneyCommand() {
		super("addmoney", "§l§fВыдать Монет Игроку");
		this.setPermission("Command.AddMoney");
		this.commandParameters.clear();
		this.commandParameters.put("default", new CommandParameter[]{new CommandParameter("money", CommandParamType.INT, false), new CommandParameter("player", CommandParamType.TARGET, false)});
	}
	
	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (!sender.hasPermission("Command.AddMoney")) {
			return true;
		}
		if (args.length < 2) {
			sender.sendMessage("§l§6| §r§fИспользование §7- /§6addmoney §7(§3игрок§7) (§3сумма§7)");
			return true;
		}
		String nickname = StringUtils.implode(args, 1);
		if (!AuthAPI.isRegistered(nickname)) {
			sender.sendMessage(EconomyAPI.PREFIX + "§fИгрок §6" + nickname + " §fне зарегистрирован§7!");
			return true;
		}
		Player target = Server.getInstance().getPlayerExact(nickname);
		sender.sendMessage(EconomyAPI.PREFIX + "§fИгрок §6" + target.getName() + " §fполучил §6" + args[1] + "");
		EconomyAPI.addMoney(target, Double.parseDouble(args[1]));
		return true;
	}
}