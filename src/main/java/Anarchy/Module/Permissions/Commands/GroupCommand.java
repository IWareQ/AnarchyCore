package Anarchy.Module.Permissions.Commands;

import Anarchy.Module.Auth.AuthAPI;
import Anarchy.Module.Permissions.PermissionsAPI;
import Anarchy.Utils.StringUtils;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class GroupCommand extends Command {
	
	public GroupCommand() {
		super("group", "Выдача группы");
		this.setPermission("Command.Group");
	}
	
	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			sender.sendMessage("§l§7(§3Система§7) §r§fЭту команду можно использовать только в §3Консоли");
			return true;
		}
		if (args.length < 2 || !PermissionsAPI.isGroup(Integer.parseInt(args[0]))) {
			sender.sendMessage("§l§6| §r§fИспользование §7- §6/group §7(§3ID§7) (§3игрок§7)");
			return true;
		}
		String nickname = StringUtils.implode(args, 1);
		if (!AuthAPI.isRegistered(nickname)) {
			sender.sendMessage(PermissionsAPI.PREFIX + "§fИгрок §6" + nickname + " §7- §fне зарегистрирован§7!");
			return true;
		}
		if (!StringUtils.isInteger(args[0]) || !PermissionsAPI.isGroup(Integer.parseInt(args[0]))) {
			sender.sendMessage(PermissionsAPI.PREFIX + "§fГруппа §3" + nickname + " §7- §fне существует§7!");
			return true;
		}
		sender.sendMessage(PermissionsAPI.PREFIX + "§fИгрок §6" + nickname + " §fполучил группу §7- " + PermissionsAPI.GROUPS.get(Integer.parseInt(args[0])));
		Player addPermission = Server.getInstance().getPlayerExact(nickname);
		if (addPermission != null) {
			addPermission.sendMessage(PermissionsAPI.PREFIX + "§fВы получили привилегию §7(" + PermissionsAPI.GROUPS.get(Integer.parseInt(args[0])) + "§7)");
			PermissionsAPI.setGroup(addPermission, Integer.parseInt(args[0]));
			PermissionsAPI.updatePermissions(addPermission);
			PermissionsAPI.updateTag(addPermission);
			return true;
		}
		PermissionsAPI.setGroup(nickname, Integer.parseInt(args[0]));
		return true;
	}
}