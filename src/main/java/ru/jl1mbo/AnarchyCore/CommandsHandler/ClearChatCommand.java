package ru.jl1mbo.AnarchyCore.CommandsHandler;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class ClearChatCommand extends Command {

	public ClearChatCommand() {
		super("clearchat", "§rОчистка чата");
		this.setPermission("Command.ClearChat");
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (!player.hasPermission(this.getPermission())) {
				return false;
			}
			for (int i = 0; i <= 100; i++) {
				Server.getInstance().getOnlinePlayers().values().forEach(players -> {
					player.sendMessage("\n\n\n");
				});
			}
			Server.getInstance().broadcastMessage("§l§6• §rЧат был очищен§7!");
			player.sendMessage("§l§a• §rВы успешно очистили чат для всех§7!");
			for (Player admins : Server.getInstance().getOnlinePlayers().values()) {
				if (admins.hasPermission("AdminChat")) {
					admins.sendPopup("§lИгрок §6" + player.getName() + " §fочистил чат для всех§7!");
				}
			}
		}
		return false;
	}
}