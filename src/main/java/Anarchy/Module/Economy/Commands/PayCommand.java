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
		super("pay", "§l§fПеревести Монет");
		commandParameters.clear();
		this.commandParameters.put("default", new CommandParameter[]{new CommandParameter("player", CommandParamType.TARGET, false), new CommandParameter("money", CommandParamType.INT, false)});
	}
	
	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		Player player = (Player)sender;
		if (args.length < 2) {
			player.sendMessage("§l§6| §r§fИспользование §7- /§6pay §7(§3игрок§7) (§3сумма§7)");
			return true;
		}
		Player payPlayer = Server.getInstance().getPlayer(args[0]);
		if (payPlayer == null) {
			player.sendMessage(EconomyAPI.PREFIX + "§fИгрок §6" + args[0] + " §7- §3Оффлайн!");
			return true;
		}
		if (!args[1].matches("^[0-9]+$") || Integer.parseInt(args[1]) < 0) {
			player.sendMessage(EconomyAPI.PREFIX + "§fСумма может быть только положительным числом");
			return true;
		}
		int money = EconomyAPI.myMoney(player);
		if (money < Integer.parseInt(args[1])) {
			player.sendMessage(EconomyAPI.PREFIX + "§fВам не хватает §3монет §fдля перевода§7.\n§l§6| §r§fВаш баланс §7- §6" + money + "");
			return true;
		}
		player.sendMessage(EconomyAPI.PREFIX + "§fВы §3успешно §fперевели §6" + args[1] + " §fИгроку §3" + player.getName());
		payPlayer.sendMessage(EconomyAPI.PREFIX + "§fИгрок §3" + sender.getName() + " §fперевел Вам §6" + args[1] + "");
		EconomyAPI.reduceMoney(player, Integer.parseInt(args[1]));
		EconomyAPI.addMoney(payPlayer, Integer.parseInt(args[1]));
		return true;
	}
}