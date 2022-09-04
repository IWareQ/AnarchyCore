package ru.iwareq.anarchycore.module.Commands.Defaults;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class ListCommand extends Command {

	public ListCommand() {
		super("list", "§rСписок игроков которые онлайн");
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		StringBuilder stringBuilder = new StringBuilder();
		for (Player player : Server.getInstance().getOnlinePlayers().values()) {
			stringBuilder.append("§7, §6").append(player.getName());
		}
		sender.sendMessage("§l§6• §fНа сервере §6" + Server.getInstance().getOnlinePlayers().size() + " §fонлайна§7!\n§fИгроки§7: §6" + stringBuilder.substring(1));
		return false;
	}
}