package ru.iwareq.anarchycore.module.Commands;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

import java.util.HashSet;
import java.util.Set;

public class HideGlobalChatCommand extends Command {

	public static final Set<String> PLAYERS = new HashSet<>();

	public HideGlobalChatCommand() {
		super("hideglobalchat", "§rСкрыть/показать глобальный чат");
		this.setAliases(new String[]{"hgc"});
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (PLAYERS.contains(sender.getName())) {
			PLAYERS.remove(sender.getName());
			sender.sendMessage("Глобальный чат успешно включен");
		} else {
			PLAYERS.add(sender.getName());
			sender.sendMessage("Глобальный чат успешно выключен");
		}

		return false;
	}
}