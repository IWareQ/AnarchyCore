package Anarchy.Module.BanSystem.Commands;

import Anarchy.AnarchyMain;
import Anarchy.Module.BanSystem.BanSystemAPI;
import Anarchy.Utils.StringUtils;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;

public class UnMuteCommand extends Command {

	public UnMuteCommand() {
		super("unmute", "§r§fСнять блокировку чата");
		this.setPermission("Command.UnMute");
		this.commandParameters.clear();
		this.commandParameters.put("default", new CommandParameter[] {new CommandParameter("player", CommandParamType.TARGET, false)});
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player)sender;
			if (!player.hasPermission("Command.UnMute")) {
				return false;
			}
			if (args.length != 1) {
				player.sendMessage("§l§6• §r§fИспользование §7- /§6unmute §7(§3игрок§7)");
				return true;
			}
			String nickname = StringUtils.implode(args, 0);
			if (BanSystemAPI.playerIsMuted(nickname)) {
				player.sendMessage(AnarchyMain.PREFIX + "§fЧат игрока §6" + nickname + " §fбыл разблокирован§7!");
				BanSystemAPI.unMutePlayer(nickname);
			} else {
				player.sendMessage(AnarchyMain.PREFIX + "§fЧат игрока §6" + nickname + " §fне заблокирован§7!");
			}
		}
		return false;
	}
}