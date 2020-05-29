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

	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			return true;
		}

		if (args.length<2 || !PermissionsAPI.isGroup(Integer.parseInt(args[0]))) {
			sender.sendMessage("§l§e| §fИспользование §7- §e/group <ID> <игрок>");
			return true;
		}

		String nickname = StringUtils.implode(args, 1);
		if (!AuthAPI.isRegistered(nickname)) {
			sender.sendMessage(PermissionsAPI.PREFIX + "§fИгрок §a" + nickname + " §7- §fне зарегистрирован!");
			return true;
		}

		if (!StringUtils.isInteger(args[0]) || !PermissionsAPI.isGroup(Integer.parseInt(args[0]))) {
			sender.sendMessage(PermissionsAPI.PREFIX + "§fГруппа §c" + nickname + " §7- §fне существует!");
			return true;
		}

		sender.sendMessage(PermissionsAPI.PREFIX + "§fИгрок §a" + nickname + " §fполучил группу " + PermissionsAPI.GROUPS.get(Integer.parseInt(args[0])));
		Player player = Server.getInstance().getPlayerExact(nickname);
		if (player != null) {
			player.sendMessage(PermissionsAPI.PREFIX + "§fВы получили привилегию §7(" + PermissionsAPI.GROUPS.get(Integer.parseInt(args[0])) + "§7)");
			PermissionsAPI.setGroup(player, Integer.parseInt(args[0]));
			PermissionsAPI.updatePermissions(player);
			PermissionsAPI.updateTag(player);
			return true;
		}
		PermissionsAPI.setGroup(nickname, Integer.parseInt(args[0]));
		return true;
	}
}