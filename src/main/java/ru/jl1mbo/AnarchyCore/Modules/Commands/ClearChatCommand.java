package ru.jl1mbo.AnarchyCore.Modules.Commands;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import ru.jl1mbo.AnarchyCore.Utils.Utils;

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
		for (int i = 0; i <= 100; i++) {
			for (Player player : Server.getInstance().getOnlinePlayers().values()) {
				player.sendMessage("\n\n\n");
			}
		}
		Server.getInstance().broadcastMessage("§l§6• §rЧат успешно был §6очищен§7!");
		sender.sendMessage("§l§6• §rВы успешно очистили чат для всех§7!");
		Utils.sendMessageAdmins("Игрок §6" + sender.getName() + " §fочистил чат для всех§7!", 0);
		return false;
	}
}