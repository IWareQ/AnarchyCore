package ru.iwareq.anarchycore.module.Commands;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import ru.iwareq.anarchycore.manager.WorldSystem.WorldSystemAPI;
import ru.iwareq.anarchycore.module.Cooldown.CooldownAPI;

public class SunnyCommand extends Command {

	public SunnyCommand() {
		super("sunny", "§rЦстановить солнечную погоду на сервере");
		this.setPermission("Command.Sunny");
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (player.hasPermission(this.getPermission())) {
				CooldownAPI.addCooldown(player, this.getName(), 50 * 60);
				WorldSystemAPI.Map.setRaining(false);
				Server.getInstance().broadcastMessage("§l§6• §rИгрок §6" + player.getName() + " §fустановил§7(§fа§7) " +
						"§rсолнечную погоду");
			}
		}
		return false;
	}
}