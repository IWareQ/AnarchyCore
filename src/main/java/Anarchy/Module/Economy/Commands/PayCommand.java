package Anarchy.Module.Economy.Commands;

import Anarchy.Module.Economy.EconomyAPI;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;

public class PayCommand extends Command {
	public PayCommand() {
		super("pay", "Перевод денег");
		commandParameters.clear();
		this.commandParameters.put("default", new CommandParameter[] {
			new CommandParameter("player", CommandParamType.TARGET, false), new CommandParameter("money", CommandParamType.INT, false)
		});
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (args.length<2) {
			sender.sendMessage("§l§e| §fИспользование §7- §e/pay <игрок> <сумма>");
			return true;
		}

		Player player = Server.getInstance().getPlayer(args[0]);
		if (player == null) {
			sender.sendMessage(EconomyAPI.PREFIX + "§fИгрок §a" + args[0] + " §7- §cОффлайн!");
			return true;
		}

		if (!args[1].matches("^[0-9]+$") || Integer.parseInt(args[1])<0) {
			sender.sendMessage(EconomyAPI.PREFIX + "§fСумма может быть только положительным числом");
			return true;
		}

		int money = EconomyAPI.myMoney((Player) sender);
		if (money<Integer.parseInt(args[1])) {
			sender.sendMessage(EconomyAPI.PREFIX + "§fВам не хватает денег для перевода.\n§l§e| §r§fВаш баланс §7- §e" + money + "");
			return true;
		}

		sender.sendMessage(EconomyAPI.PREFIX + "§fВы §aуспешно §fперевели §e" + args[1] + " §fИгроку §a" + player.getName());
		player.sendMessage(EconomyAPI.PREFIX + "§fИгрок §a" + sender.getName() + " §fперевел Вам §e" + args[1] + "");
		EconomyAPI.reduceMoney((Player) sender, Integer.parseInt(args[1]));
		EconomyAPI.addMoney(player, Integer.parseInt(args[1]));
		return true;
	}
}