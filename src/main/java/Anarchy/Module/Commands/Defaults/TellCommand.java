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
	private static String PREFIX = "§l§7(§3Сообщения§7) §r";
	
	public TellCommand() {
		super("tell", "Отправить Личное Сообщение");
		commandParameters.clear();
		this.commandParameters.put("default", new CommandParameter[]{new CommandParameter("player", CommandParamType.TARGET, false)});
	}
	
	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		Player player = (Player)sender;
		String playerName = player.getName();
		if (args.length < 2) {
			player.sendMessage("§l§e| §r§fИспользование §7- §e/tell §7(§3игрок§7) (§3текст§7)");
			return true;
		}
		Player tellPlayer = Server.getInstance().getPlayer(args[0]);
		if (tellPlayer == null) {
			player.sendMessage("§l§e| §r§fИгрок §e" + args[0] + " §7- §3Оффлайн");
			return true;
		}
		String message = StringUtils.implode(args, 1);
		player.sendMessage(PREFIX + "§fЛичное сообщение для §3" + tellPlayer.getName() + "\n§fТекст §7- §f" + message);
		tellPlayer.sendMessage(PREFIX + "§fЛичное сообщение от §3" + playerName + "\n§fТекст §7- §f" + message);
		tellPlayer.sendTip("§l§fНовое Личное Сообщение§7!");
		tellPlayer.getLevel().addSound(tellPlayer, Sound.RANDOM_CLICK, 1, 1, tellPlayer);
		return true;
	}
}