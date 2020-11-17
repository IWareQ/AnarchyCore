package Anarchy.Module.BanSystem.Commands;

import Anarchy.AnarchyMain;
import Anarchy.Module.BanSystem.BanSystemAPI;
import Anarchy.Utils.StringUtils;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;

public class UnBanCommand extends Command {

	public UnBanCommand() {
		super("unban", "§r§fСнять блокировку аккаунта");
		this.setPermission("Command.UnBan");
		this.commandParameters.clear();
		this.commandParameters.put("default", new CommandParameter[] {new CommandParameter("player", CommandParamType.TARGET, false)});
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (!sender.hasPermission("Command.UnBan")) {
			return false;
		}
		if (args.length < 2) {
			sender.sendMessage("§l§6• §r§fИспользование §7- /§6unban §7(§3игрок§7) (§3причина§7)");
			return true;
		}
		String reason = StringUtils.implode(args, 1);
		if (BanSystemAPI.playerIsBanned(args[0])) {
			sender.sendMessage(AnarchyMain.PREFIX + "§fАккаунт игрока §6" + args[0] + " §fбыл разблокирован§7!");
			AnarchyMain.sendMessageToChat("🔓Разблокировка аккаунта\n\nИгрок: " + args[0] + "\nАдминистратор: " + sender.getName() + "\nПричина: " + reason, 2000000001);
			BanSystemAPI.unBanPlayer(args[0]);
		} else {
			sender.sendMessage(AnarchyMain.PREFIX + "§fАккаунт игрока §6" + args[0] + " §fне заблокирован§7!");
		}
		return false;
	}
}