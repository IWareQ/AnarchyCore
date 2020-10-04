package Anarchy.Module.Commands.Defaults;

import Anarchy.Utils.StringUtils;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.level.Sound;

public class TellCommand extends Command {

	public TellCommand() {
		super("tell", "§l§fОтправить Личное Сообщение");
		this.commandParameters.clear();
		this.commandParameters.put("default", new CommandParameter[] {new CommandParameter("player", CommandParamType.TARGET, false)});
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("§l§7(§3Система§7) §r§fЭту команду можно использовать только в §3Игре");
			return true;
		}
		Player player = (Player)sender;
		if (args.length < 2) {
			player.sendMessage("§l§6| §r§fИспользование §7- /§6tell §7(§3игрок§7) (§3текст§7)");
			return true;
		}
		Player target = Server.getInstance().getPlayer(args[0]);
		if (!target.isOnline()) {
			player.sendMessage("§l§6• §r§fИгрок §6" + args[0] + " §fне в сети§7!");
			return true;
		}
		String message = StringUtils.implode(args, 1);
		player.sendMessage("§7(§6Вы §7-> §6" + target.getName() + "§7) §f" + message);
		target.sendMessage("§7(§6" + player.getName() + " §7-> §6Вы§7) §f" + message);
		target.sendTip("§r§l§fНовое Личное Сообщение§7!");
		target.getLevel().addSound(target, Sound.RANDOM_CLICK, 1, 1, target);
		return true;
	}
}