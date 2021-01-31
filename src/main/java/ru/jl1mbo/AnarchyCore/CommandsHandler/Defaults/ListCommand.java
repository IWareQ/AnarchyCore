package ru.jl1mbo.AnarchyCore.CommandsHandler.Defaults;

import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class ListCommand extends Command {
	public ListCommand() {
		super("list", "§r§fСписок иигроков которые онлайн");
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		StringBuilder stringBuilder = new StringBuilder();
		Server.getInstance().getOnlinePlayers().values().forEach(players -> stringBuilder.append("§7, §6").append(players.getName()));
		sender.sendMessage("§l§6• §r§fНа сервере §6" + Server.getInstance().getOnlinePlayers().size() + " §fонлайна§7!\n§fИгроки§7: §6" + stringBuilder.toString());
		return false;
	}
}