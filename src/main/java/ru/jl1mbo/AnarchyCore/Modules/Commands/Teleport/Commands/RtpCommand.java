package ru.jl1mbo.AnarchyCore.Modules.Commands.Teleport.Commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import ru.jl1mbo.AnarchyCore.Manager.WorldSystem.WorldSystemAPI;
import ru.jl1mbo.AnarchyCore.Modules.Commands.Teleport.TeleportAPI;

public class RtpCommand extends Command {

	public RtpCommand() {
		super("rtp", "§rТелепортироваться в случайное место");
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (player.getLevel().equals(WorldSystemAPI.Map) || player.getLevel().equals(WorldSystemAPI.Spawn)) {
				WorldSystemAPI.randomPosition(WorldSystemAPI.Map, pos -> {
					player.sendTitle("Телепортация§7...");
					player.teleport(pos);
				});
			} else {
				player.sendMessage(TeleportAPI.PREFIX + "С этого измерения §6запрещено §fтелепортироваться§7!");
			}
		}
		return false;
	}
}