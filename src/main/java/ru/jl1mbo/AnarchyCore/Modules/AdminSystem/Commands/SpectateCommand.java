package ru.jl1mbo.AnarchyCore.Modules.AdminSystem.Commands;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import ru.jl1mbo.AnarchyCore.Modules.AdminSystem.AdminAPI;

public class SpectateCommand extends Command {

	public SpectateCommand() {
		super("spectate", "§rНаблюдение за Игроками", "", new String[]{"sp"});
		this.setPermission("Command.Spectate");
		this.commandParameters.clear();
		this.commandParameters.put("spectate", new CommandParameter[]{CommandParameter.newType("player", CommandParamType.TARGET)});
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (!player.hasPermission(this.getPermission())) {
				return false;
			}
			if (args.length != 1) {
				player.sendMessage("§l§6• §rИспользование §7- §6/sp §7(§6игрок§7)");
				return true;
			}
			Player target = Server.getInstance().getPlayer(args[0]);
			if (target == null) {
				player.sendMessage("§l§6• §rИгрок §6" + args[0] + " §fне в сети§7!");
				return true;
			}
			if (target.equals(player)) {
				player.sendMessage(AdminAPI.PREFIX + "Вы пытаетесь §6наблюдать §fза собой§7!");
				return true;
			}
			AdminAPI.addSpectate(player, target);
		}
		return false;
	}
}