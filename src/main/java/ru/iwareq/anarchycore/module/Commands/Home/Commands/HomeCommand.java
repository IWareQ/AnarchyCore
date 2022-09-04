package ru.iwareq.anarchycore.module.Commands.Home.Commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import ru.iwareq.anarchycore.manager.WorldSystem.WorldSystemAPI;
import ru.iwareq.anarchycore.module.Commands.Home.HomeAPI;
import ru.iwareq.anarchycore.module.Cooldown.CooldownAPI;

public class HomeCommand extends Command {

	public HomeCommand() {
		super("home", "§rТелепортироваться домой");
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (HomeAPI.canHome(player.getName())) {
				player.sendMessage("Запускаем перемещение...\nПрибытие через 2 сек");
				CooldownAPI.addTask(player, () -> {
					player.sendMessage(HomeAPI.PREFIX + "Вы успешно телепортированы домой§7!");
					player.teleport(HomeAPI.getPosition(player.getName()));
					CooldownAPI.addCooldown(player, this.getName(), 10);
				}, 2);
			} else {
				player.sendMessage(HomeAPI.PREFIX + "Точек дома не обнаружено§7!\n§l§6• §rДля создания точки Дома используйте §7/§6sethome");
			}
		}

		return false;
	}
}
