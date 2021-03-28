package ru.jl1mbo.AnarchyCore.Modules.AdminSystem.Commands;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import ru.jl1mbo.AnarchyCore.Modules.AdminSystem.AdminAPI;

public class CheatCheckCommand extends Command {

	public CheatCheckCommand() {
		super("cheatcheack", "§rВызвать на проверку", "", new String[] {"cc"});
		this.setPermission("Command.CheatCheack");
		this.commandParameters.clear();
		this.commandParameters.put("cheatcheack", new CommandParameter[] {CommandParameter.newType("player", CommandParamType.TARGET)});
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player)sender;
			if (!player.hasPermission(this.getPermission())) {
				return false;
			}
			if (args.length != 1) {
				player.sendMessage("§l§6• §rИспользование §7- §6/cc §7(§6игрок§7)");
				return true;
			}
			Player target = Server.getInstance().getPlayer(args[0]);
			if (target == null) {
				player.sendMessage("§l§6• §rИгрок §6" + args[0] + " §fне в сети§7!");
				return true;
			}
			AdminAPI.addCheatCheacker(player, target);
		}
		return false;
	}
}