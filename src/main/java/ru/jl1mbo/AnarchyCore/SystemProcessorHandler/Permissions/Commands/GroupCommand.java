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
		super("group", "§r§fВыдача привилегии");
		this.setPermission("Command.Group");
		this.commandParameters.clear();
		this.commandParameters.put("default", new CommandParameter[] {new CommandParameter("number", CommandParamType.INT, false), new CommandParameter("player", CommandParamType.TARGET, false)});
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (!(sender instanceof Player)) {
			if (args.length < 2 || !PermissionAPI.isGroup(args[0])) {
				sender.sendMessage("§l§6• §r§fИспользование §7- /§6group (§6ID§7) (§6Игрок§7)");
				return true;
			}
			String nickname = Utils.implode(args, 1);
			if (!PermissionAPI.isGroup(args[0])) {
				sender.sendMessage(PermissionAPI.PREFIX + "§fГруппа §6" + args[0] + " §fне существует§7!");
				return true;
			}
			sender.sendMessage(PermissionAPI.PREFIX + "§fИгрок §6" + nickname + " §fполучил группу " + PermissionAPI.getAllGroups().get(
								   args[0]).getGroupName() + "§7(" + Utils.getOnlineString(nickname) + "§7)");
			Utils.sendMessageToChat("Игрок " + nickname + " приобрел привилегию " + PermissionAPI.getAllGroups().get(args[0]).getGroupName().replaceAll("§[0-9]+",
									"").replaceAll("§[a-z]+", "") + " (" + Utils.getOnlineString(nickname).replaceAll("§[0-9]+", "").replaceAll("§[a-z]+", "") + ")", 2000000001);
			Player target = Server.getInstance().getPlayerExact(nickname);
			if (target != null) {
				target.sendMessage(PermissionAPI.PREFIX + "§fВы получили привилегию " + PermissionAPI.getAllGroups().get(args[0]).getGroupName() +
								   "§7. §fПодробнее со списком возможностей можно познакомиться с помощью команды §7/§6donate");
				PermissionAPI.setGroup(target.getName(), args[0]);
				PermissionAPI.updatePermissions(target);
				PermissionAPI.updateNamedTag(target);
				return true;
			}
			PermissionAPI.setGroup(nickname, args[0]);
		}
		return false;
	}
}