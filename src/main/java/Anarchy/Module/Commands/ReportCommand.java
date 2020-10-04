package Anarchy.Module.Commands;

import Anarchy.AnarchyMain;
import Anarchy.Utils.StringUtils;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.level.Sound;

public class ReportCommand extends Command {

	public ReportCommand() {
		super("report", "§r§l§fПожаловаться на Игрока");
		this.commandParameters.clear();
		this.commandParameters.put("default", new CommandParameter[] {new CommandParameter("cheater", CommandParamType.TARGET, false), new CommandParameter("reason", CommandParamType.INT, false)});
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("§l§7(§3Система§7) §r§fЭту команду можно использовать только в §3Игре");
			return true;
		}
		Player player = (Player)sender;
		if (args.length < 1) {
			player.sendMessage("§l§6| §r§fИспользование §7- /§6report §7(§3игрок§7) (§3текст§7)");
			return true;
		}
		Player target = Server.getInstance().getPlayer(args[0]);
		if (target == null) {
			player.sendMessage("§fИгрок §6" + args[0] + " §fне в сети§7!");
			player.getLevel().addSound(player, Sound.NOTE_BASS, 1, 1, player);
			return true;
		}
		String message = StringUtils.implode(args, 1);
		player.sendMessage("§l§7(§3Репорты§7) §r§fРепорт был отправлен§7!");
		AnarchyMain.sendMessageToChat("Новая жалоба! (" + player.getName() + ")\nНарушитель: " + target.getName() + "\nПричина: " + message, 2000000004);
		return false;
	}
}