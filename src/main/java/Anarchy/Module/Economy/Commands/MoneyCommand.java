package Anarchy.Module.Economy.Commands;

import Anarchy.Module.Economy.EconomyAPI;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class MoneyCommand extends Command {
	
	public MoneyCommand() {
		super("money", "Игровой баланс", "", new String[]{"mymoney"});
		commandParameters.clear();
	}
	
	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		sender.sendMessage(EconomyAPI.PREFIX + "§fВаш баланс §7- §e" + EconomyAPI.myMoney((Player)sender) + "");
		return true;
	}
}