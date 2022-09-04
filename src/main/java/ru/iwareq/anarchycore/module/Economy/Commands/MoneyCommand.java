package ru.iwareq.anarchycore.module.Economy.Commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import ru.iwareq.anarchycore.module.Auth.AuthAPI;
import ru.iwareq.anarchycore.module.Economy.EconomyAPI;

public class MoneyCommand extends Command {

	public MoneyCommand() {
		super("money", "§rИгровой баланс", "", new String[]{"mymoney"});
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			sender.sendMessage(EconomyAPI.PREFIX + "Ваш баланс§7: §6" + String.format("%.2f", AuthAPI.getMoney(sender.getName())) + "");
		}

		return false;
	}
}