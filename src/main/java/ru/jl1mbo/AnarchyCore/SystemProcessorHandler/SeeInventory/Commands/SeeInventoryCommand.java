package ru.jl1mbo.AnarchyCore.SystemProcessorHandler.SeeInventory.Commands;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.SeeInventory.SeeInventoryAPI;

public class SeeInventoryCommand extends Command {

	public SeeInventoryCommand() {
		super("seeinventory", "§r§fПросмотр инвентаря", "", new String[] {"seeinv"});
		this.setPermission("Command.SeeInventory");
		this.commandParameters.clear();
		this.commandParameters.put("default", new CommandParameter[] {new CommandParameter("player", CommandParamType.TARGET, false)});
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (!player.hasPermission(this.getPermission())) {
				return false;
			}
			if (args.length != 1) {
				player.sendMessage("§l§6• §rИспользование §7- /§6seeinv §7(§6игрок§7)");
				return true;
			}
			Player target = Server.getInstance().getPlayer(args[0]);
			if (target == null) {
				player.sendMessage("§l§6• §rИгрок §6" + args[0] + " §fне в сети§7!");
				return true;
			}
			SeeInventoryAPI.checkInventory(target, player);
		}
		return false;
	}
}