package ru.jl1mbo.AnarchyCore.SystemProcessorHandler.Permissions.Commands;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.Permissions.PermissionAPI;
import ru.jl1mbo.AnarchyCore.Utils.Utils;

public class GroupCommand extends Command {

	public GroupCommand() {
		super("group", "§rВыдача привилегии");
		this.setPermission("Command.Group");
		this.commandParameters.clear();
		this.commandParameters.put("default", new CommandParameter[] {new CommandParameter("groupId", CommandParamType.INT, false), new CommandParameter("player", CommandParamType.TARGET, false)});
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (!sender.hasPermission(this.getPermission())) {
			return false;
		}
		if (args.length < 2) {
			sender.sendMessage("§l§6• §rИспользование §7- /§6group §7(§6groupId§7) (§6Игрок§7)");
			return true;
		}
		if (!PermissionAPI.isGroup(args[0])) {
			sender.sendMessage(PermissionAPI.PREFIX + "Группа §6" + args[0] + " §fне существует§7!");
			return true;
		}
		String nickname = Utils.implode(args, 1);
		sender.sendMessage(PermissionAPI.PREFIX + "Игрок §6" + nickname + " §fполучил группу " + PermissionAPI.getAllGroups().get(
							   args[0]).getGroupName() + " §7(" + Utils.getOnlineString(nickname) + "§7)");
		Player target = Server.getInstance().getPlayerExact(nickname);
		if (target != null) {
			target.sendMessage(PermissionAPI.PREFIX + "Вы получили привилегию " + PermissionAPI.getAllGroups().get(args[0]).getGroupName() +
							   "\n§l§6• §rПодробнее со списком возможностей можно познакомиться с помощью команды §7/§6donate");
			PermissionAPI.setGroup(target.getName(), args[0]);
			PermissionAPI.updatePermissions(target);
			PermissionAPI.updateNamedTag(target);
			return true;
		}
		PermissionAPI.setGroup(nickname, args[0]);
		return false;
	}
}