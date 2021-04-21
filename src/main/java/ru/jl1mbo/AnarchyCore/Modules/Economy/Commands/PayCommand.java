package ru.jl1mbo.AnarchyCore.Modules.Economy.Commands;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import ru.jl1mbo.AnarchyCore.Modules.Auth.AuthAPI;
import ru.jl1mbo.AnarchyCore.Modules.Cooldown.CooldownAPI;
import ru.jl1mbo.AnarchyCore.Modules.Economy.EconomyAPI;
import ru.jl1mbo.AnarchyCore.Utils.Utils;

public class PayCommand extends Command {

	public PayCommand() {
		super("pay", "§rПеревод монет");
		this.commandParameters.clear();
		this.commandParameters.put("pay", new CommandParameter[]{CommandParameter.newType("money", CommandParamType.INT), CommandParameter.newType("player", CommandParamType.TARGET)});
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length < 2) {
				player.sendMessage("§l§6• §rИспользование §7- /§6pay §7(§6сумма§7) (§6игрок§7)");
				return true;
			}
			String targetName = Utils.implode(args, 1);
			if (!AuthAPI.isRegister(targetName)) {
				player.sendMessage(EconomyAPI.PREFIX + "Игрок §6" + targetName + " §fне зарегистрирован§7!");
				return true;
			}
			if (!Utils.isDouble(args[0]) || Double.parseDouble(args[0]) <= 0) {
				player.sendMessage(EconomyAPI.PREFIX + "Сумма может быть только §6положительным §fчислом§7!");
				return true;
			}
			if (EconomyAPI.myMoney(player.getName()) < Double.parseDouble(args[0])) {
				player.sendMessage(EconomyAPI.PREFIX + "Вам §6не хватает §fмонет для перевода§7!\n§l§6• §rВаш баланс§7: §6" + String.format("%.1f", EconomyAPI.myMoney(player.getName())) + "");
				return true;
			}
			player.sendMessage(EconomyAPI.PREFIX + "Вы успешно перевели §6" + String.format("%.1f", Double.parseDouble(args[0])) + " §fигроку §6" + targetName);
			Player target = Server.getInstance().getPlayer(targetName);
			if (target != null) {
				target.sendMessage(EconomyAPI.PREFIX + "Игрок §6" + player.getName() + " §fперевел Вам §6" + String.format("%.1f", Double.parseDouble(args[0])) + "");
			}
			EconomyAPI.reduceMoney(player.getName(), Double.parseDouble(args[0]));
			EconomyAPI.addMoney(targetName, Double.parseDouble(args[0]));
			CooldownAPI.addCooldown(player, this.getName(), 10);
		}
		return false;
	}
}