package Anarchy.Module.Permissions.Commands;

import Anarchy.Module.Auth.AuthAPI;
import Anarchy.Module.Permissions.PermissionsAPI;
import Anarchy.Utils.StringUtils;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;

public class GroupCommand extends Command {
	
	public GroupCommand() {
		super("group", "Выдача группы");
		this.setPermission("Command.Group");
		this.commandParameters.put("default", new CommandParameter[]{new CommandParameter("player", CommandParamType.TARGET, false), new CommandParameter("number", CommandParamType.INT, false)});
	}
	
	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (!sender.hasPermission("Command.Group")) {
			return false;
		}
		if (args.length < 2 || !PermissionsAPI.isGroup(Integer.parseInt(args[1]))) {
			sender.sendMessage("§l§6| §r§fИспользование §7- /§6group (§3игрок§7) (§3ID§7)");
			return true;
		}
		String nickname = StringUtils.implode(args, 0);
		if (!AuthAPI.isRegistered(nickname)) {
			sender.sendMessage(PermissionsAPI.PREFIX + "§fИгрок §6" + nickname + " §7- §fне зарегистрирован§7!");
			return true;
		}
		if (!StringUtils.isInteger(args[0]) || !PermissionsAPI.isGroup(Integer.parseInt(args[1]))) {
			sender.sendMessage(PermissionsAPI.PREFIX + "§fГруппа §3" + args[1] + " §7- §fне существует§7!");
			return true;
		}
		sender.sendMessage(PermissionsAPI.PREFIX + "§fИгрок §6" + nickname + " §fполучил группу  " + PermissionsAPI.GROUPS.get(Integer.parseInt(args[0])));
		Player target = Server.getInstance().getPlayerExact(nickname);
		if (target != null) {
			target.sendMessage(PermissionsAPI.PREFIX + "§fВы получили привилегию " + PermissionsAPI.GROUPS.get(Integer.parseInt(args[0])));
			PermissionsAPI.setGroup(target, Integer.parseInt(args[1]));
			PermissionsAPI.updatePermissions(target);
			PermissionsAPI.updateTag(target);
			return true;
		}
		PermissionsAPI.setGroup(nickname, Integer.parseInt(args[1]));
		return true;
	}
}