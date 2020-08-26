package Anarchy.Module.Economy.Commands;

import Anarchy.Module.Economy.EconomyAPI;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class MoneyCommand extends Command {
	
	public MoneyCommand() {
		super("money", "§l§fИгровой баланс", "", new String[]{"mymoney"});
		commandParameters.clear();
	}
	
	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		Player player = (Player)sender;
		if (!(sender instanceof Player)) {
			sender.sendMessage("§l§7(§3Система§7) §r§fЭту команду можно использовать только в §3Игре");
			return true;
		}
		player.sendMessage(EconomyAPI.PREFIX + "§fВаш баланс §7- §6" + EconomyAPI.myMoney(player) + "");
		return true;
	}
}