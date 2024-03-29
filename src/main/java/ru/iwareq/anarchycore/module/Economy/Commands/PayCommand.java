package ru.iwareq.anarchycore.module.Economy.Commands;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import ru.iwareq.anarchycore.module.Auth.AuthAPI;
import ru.iwareq.anarchycore.module.Cooldown.CooldownAPI;
import ru.iwareq.anarchycore.module.Economy.EconomyAPI;
import ru.iwareq.anarchycore.util.Utils;

public class PayCommand extends Command {

	public PayCommand() {
		super("pay", "§rПеревод монет");
		this.commandParameters.clear();
		this.commandParameters.put("pay", new CommandParameter[]{
				CommandParameter.newType("money", CommandParamType.INT),
				CommandParameter.newType("player", CommandParamType.TARGET)
		});
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

			Player target = Server.getInstance().getPlayerExact(targetName);
			if (target != null) {
				if (player.getName().equalsIgnoreCase(target.getName())) {
					player.sendMessage(EconomyAPI.PREFIX + "Вы не можете §6перевести себе же§7!");
					return true;
				}

				double count = Double.parseDouble(args[0]);
				if (!Utils.isDouble(args[0]) || count <= 0D) {
					player.sendMessage(EconomyAPI.PREFIX + "Сумма может быть только §6положительным §fчислом§7!");
					return true;
				}

				if (AuthAPI.getMoney(player.getName()) < count) {
					player.sendMessage(EconomyAPI.PREFIX + "Вам §6не хватает §fмонет для перевода§7!\n§l§6• §rВаш баланс§7: §6" + EconomyAPI.format(AuthAPI.getMoney(player.getName())) + "");
					return true;
				}

				player.sendMessage(EconomyAPI.PREFIX + "Вы успешно перевели §6" + String.format("%.2f",
						count) + " §fигроку §6" + targetName);
				target.sendMessage(EconomyAPI.PREFIX + "Игрок §6" + player.getName() + " §fперевел Вам §6" + EconomyAPI.format(count) + "");

				EconomyAPI.reduceMoney(player.getName(), count);
				EconomyAPI.addMoney(targetName, count);
				CooldownAPI.addCooldown(player, this.getName(), 10);
			}
		}

		return false;
	}
}
