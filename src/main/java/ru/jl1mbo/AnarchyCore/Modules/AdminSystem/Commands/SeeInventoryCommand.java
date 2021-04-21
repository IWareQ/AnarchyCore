package ru.jl1mbo.AnarchyCore.Modules.AdminSystem.Commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import ru.jl1mbo.AnarchyCore.Modules.AdminSystem.AdminAPI;
import ru.jl1mbo.AnarchyCore.Utils.Utils;

public class SeeInventoryCommand extends Command {

	public SeeInventoryCommand() {
		super("seeinventory", "§rПросмотр инвентаря", "", new String[]{"seeinv"});
		this.setPermission("Command.SeeInventory");
		this.commandParameters.clear();
		this.commandParameters.put("seeinventory", new CommandParameter[]{CommandParameter.newType("player", CommandParamType.TARGET)});
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
			String targetName = Utils.implode(args, 0);
			AdminAPI.openCheckInventoryChest(player, targetName);
		}
		return false;
	}
}