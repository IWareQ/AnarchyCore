package ru.jl1mbo.AnarchyCore.Modules.Economy.Commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import ru.jl1mbo.AnarchyCore.Modules.Economy.EconomyAPI;

public class MoneyCommand extends Command {

	public MoneyCommand() {
		super("money", "§rИгровой баланс", "", new String[]{"mymoney"});
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			sender.sendMessage(EconomyAPI.PREFIX + "Ваш баланс§7: §6" + String.format("%.1f", EconomyAPI.myMoney(sender.getName())) + "");
		}
		return false;
	}
}