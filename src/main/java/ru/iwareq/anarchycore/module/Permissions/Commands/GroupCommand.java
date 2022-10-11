package ru.iwareq.anarchycore.module.Permissions.Commands;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandEnum;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import ru.iwareq.anarchycore.module.Auth.AuthAPI;
import ru.iwareq.anarchycore.module.Permissions.Group.DefaultGroup;
import ru.iwareq.anarchycore.module.Permissions.PermissionAPI;
import ru.iwareq.anarchycore.util.Utils;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

public class GroupCommand extends Command {

	public GroupCommand() {
		super("group", "§rВыдача привилегии");
		this.setPermission("Command.Group");
		this.commandParameters.clear();
		ArrayList<String> groupsName = new ArrayList<>();
		for (Entry<String, DefaultGroup> entry : PermissionAPI.GROUPS.entrySet()) {
			groupsName.add(entry.getKey());
		}

		this.commandParameters.put("group", new CommandParameter[]{
				CommandParameter.newEnum("groupId", new CommandEnum("groups", groupsName)),
				CommandParameter.newType("player", CommandParamType.TARGET)
		});

		this.commandParameters.put("group2", new CommandParameter[]{
				CommandParameter.newEnum("action", new CommandEnum("actions", "time")),
				CommandParameter.newEnum("groupId", new CommandEnum("groups", groupsName)),
				CommandParameter.newType("player", CommandParamType.TARGET),
				CommandParameter.newType("time", CommandParamType.INT)
		});
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (!sender.hasPermission(this.getPermission())) {
			return false;
		}

		if (args.length == 4) {
			if (!PermissionAPI.isGroup(args[1])) {
				sender.sendMessage(PermissionAPI.PREFIX + "Группа §6" + args[0] + " §fне существует§7!");
				return true;
			}

			String targetName = args[2];
			if (!AuthAPI.isRegister(targetName)) {
				sender.sendMessage(PermissionAPI.PREFIX + "Игрок §6" + targetName + " §fне зарегистрирован§7!");
				return true;
			}

			sender.sendMessage(PermissionAPI.PREFIX + "Игрок §6" + targetName + " §fполучил группу " + PermissionAPI.getGroup(args[1]).getGroupName() + " §7(" + Utils.getOnlineString(targetName) + "§7)");
			Player target = Server.getInstance().getPlayerExact(targetName);
			if (target != null) {
				target.sendMessage(PermissionAPI.PREFIX + "Вы получили привилегию " + PermissionAPI.getGroup(args[1]).getGroupName() + "§7!\n§l§6• §rПодробнее со списком возможностей можно познакомиться с помощью команды §7/§6donate");
				PermissionAPI.setGroup(target.getName(), args[1], TimeUnit.DAYS.toSeconds(Long.parseLong(args[3])));
				PermissionAPI.updatePermissions(target);
				PermissionAPI.updateNamedTag(target);
				return true;
			}

			PermissionAPI.setGroup(targetName, args[1], TimeUnit.DAYS.toSeconds(Long.parseLong(args[3])));

			return true;
		}

		if (args.length < 2) {
			sender.sendMessage("§l§6• §rИспользование §7- /§6group §7(§6groupId§7) (§6игрок§7)");
			return true;
		}

		if (!PermissionAPI.isGroup(args[0])) {
			sender.sendMessage(PermissionAPI.PREFIX + "Группа §6" + args[0] + " §fне существует§7!");
			return true;
		}

		String targetName = Utils.implode(args, 1);
		if (!AuthAPI.isRegister(targetName)) {
			sender.sendMessage(PermissionAPI.PREFIX + "Игрок §6" + targetName + " §fне зарегистрирован§7!");
			return true;
		}

		sender.sendMessage(PermissionAPI.PREFIX + "Игрок §6" + targetName + " §fполучил группу " + PermissionAPI.getGroup(args[0]).getGroupName() + " §7(" + Utils.getOnlineString(targetName) + "§7)");
		Player target = Server.getInstance().getPlayerExact(targetName);
		if (target != null) {
			target.sendMessage(PermissionAPI.PREFIX + "Вы получили привилегию " + PermissionAPI.getGroup(args[0]).getGroupName() + "§7!\n§l§6• §rПодробнее со списком возможностей можно познакомиться с помощью команды §7/§6donate");
			PermissionAPI.setGroup(target.getName(), args[0], -1);
			PermissionAPI.updatePermissions(target);
			PermissionAPI.updateNamedTag(target);
			return true;
		}

		PermissionAPI.setGroup(targetName, args[0], -1);
		return false;
	}
}