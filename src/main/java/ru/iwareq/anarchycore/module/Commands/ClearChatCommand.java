package ru.iwareq.anarchycore.module.Commands;

import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

import java.util.stream.IntStream;

public class ClearChatCommand extends Command {

	public ClearChatCommand() {
		super("clearchat", "§rОчистка чата");
		this.setPermission("Command.ClearChat");
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (!sender.hasPermission(this.getPermission())) {
			return false;
		}
		IntStream.rangeClosed(0, 100).forEach(i -> Server.getInstance().getOnlinePlayers().values().forEach(player -> player.sendMessage("\n\n\n")));
		Server.getInstance().broadcastMessage("§l§6• §rИгрок " + sender.getName() + " очистил(а) чат на сервере!§7!");
		sender.sendMessage("§l§6• §rВы успешно очистили чат для всех§7!");
		return false;
	}
}