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
	private static String PREFIX = "§l§7(§1Сообщения§7) §r";

	public TellCommand() {
		super("tell", "Личные сообщения");
		commandParameters.clear();
		this.commandParameters.put("default", new CommandParameter[] {
			new CommandParameter("player", CommandParamType.TARGET, false)
		});
	}

	@Override
	public boolean execute(CommandSender commandSender, String s, String[] strings) {
		if (strings.length < 2) {
			commandSender.sendMessage("§l§e| §fИспользование §7- §e/tell <ник> <сообщение>");
			return true;
		}

		Player tellPlayer = Server.getInstance().getPlayer(strings[0]);
		if (tellPlayer == null) {
			commandSender.sendMessage("§l§e| §r§fИгрок §a" + strings[0] + " §7- §cОффлайн");
			return true;
		}

		String message = StringUtils.implode(strings, 1);
		commandSender.sendMessage(PREFIX + "§fЛичное сообщение для §e" + tellPlayer.getName() + "\n §fТекст: §f" + message);
		tellPlayer.sendMessage(PREFIX + "§fЛичное сообщение от §e" + commandSender.getName() + "\n §fТекст: §f" + message);
		tellPlayer.sendPopup("§l§9| §fНовое личное сообщение! §9|");
		tellPlayer.getLevel().addSound(tellPlayer, Sound.RANDOM_CLICK, 1, 1, tellPlayer);
		return true;
	}
}