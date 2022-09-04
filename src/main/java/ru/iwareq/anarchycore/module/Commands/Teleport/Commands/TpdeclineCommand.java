package ru.iwareq.anarchycore.module.Commands.Teleport.Commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import ru.iwareq.anarchycore.module.Commands.Teleport.TeleportAPI;

import java.util.HashSet;
import java.util.Set;

public class TpdeclineCommand extends Command {

	public static final Set<String> PLAYERS = new HashSet<>();

	public TpdeclineCommand() {
		super("tpdecline", "§rОтключить/включить запросы на телепортацию");
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (PLAYERS.add(player.getName().toLowerCase())) {
				player.sendMessage("Телепортация к вам отключена");
			} else {
				player.sendMessage("Телепортация к вам включена");
				PLAYERS.remove(player.getName().toLowerCase());
			}
		}

		return false;
	}
}
