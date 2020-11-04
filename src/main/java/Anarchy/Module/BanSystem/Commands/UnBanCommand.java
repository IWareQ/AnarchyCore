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
		super("unban", "§r§l§fСнять блокировку аккаунта");
		this.setPermission("Command.UnBan");
		this.commandParameters.clear();
		this.commandParameters.put("default", new CommandParameter[] {new CommandParameter("player", CommandParamType.TARGET, false)});
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (!sender.hasPermission("Command.UnBan")) {
			return false;
		}
		if (args.length != 1) {
			sender.sendMessage("§l§6| §r§fИспользование §7- /§6unban §7(§3игрок§7)");
			return true;
		}
		String nickname = StringUtils.implode(args, 0);
		if (BanSystemAPI.playerIsBanned(nickname)) {
			sender.sendMessage(AnarchyMain.PREFIX + "§fАккаунт игрока §6" + nickname + " §fбыл разблокирован§7!");
			AnarchyMain.sendMessageToChat("🌲 Разблокировка аккаунта\n\nИгрок: " + nickname + "\nАдминистратор: " + sender.getName(), 2000000004);
			BanSystemAPI.unBanPlayer(nickname);
		} else {
			sender.sendMessage(AnarchyMain.PREFIX + "§fАккаунт игрока §6" + nickname + " §fне заблокирован§7!");
		}
		return false;
	}
}