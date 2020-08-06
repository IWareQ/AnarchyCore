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
	public boolean execute(CommandSender commandSender, String s, String[] strings) {
		if (strings.length < 2) {
			commandSender.sendMessage("§l§e| §r§fИспользование §7- §e/tell §7(§3игрок§7) (§3текст§7)");
			return true;
		}
		Player tellPlayer = Server.getInstance().getPlayer(strings[0]);
		if (tellPlayer == null) {
			commandSender.sendMessage("§l§e| §r§fИгрок §e" + strings[0] + " §7- §3Оффлайн");
			return true;
		}
		String message = StringUtils.implode(strings, 1);
		commandSender.sendMessage(PREFIX + "§fЛичное сообщение для §e" + tellPlayer.getName() + "\n§fТекст §7- §f" + message);
		tellPlayer.sendMessage(PREFIX + "§fЛичное сообщение от §e" + commandSender.getName() + "\n§fТекст §7- §f" + message);
		tellPlayer.sendPopup("§l§9| §fНовое Личное Сообщение! §9|");
		tellPlayer.getLevel().addSound(tellPlayer, Sound.RANDOM_CLICK, 1, 1, tellPlayer);
		return true;
	}
}