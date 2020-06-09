package Anarchy.Module.Commands;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import ru.nukkitx.forms.elements.CustomForm;

public class KickCommand extends Command {
	public KickCommand() {
		super("kick", "Кикнуть игрока с сервера");
		setPermission("Command.Check");
		commandParameters.clear();
		this.commandParameters.put("default", new CommandParameter[] {
			new CommandParameter("player", CommandParamType.TARGET, false)
		});
	}

	@Override
	public boolean execute(CommandSender commandSender, String s, String[] strings) {
		if (!commandSender.hasPermission("Command.Kick")) {
			return false;
		}

		if (strings.length != 1) {
			commandSender.sendMessage("§l§e| §fИспользование §7- §e/kick <игрок>");
			return true;
		}

		Player kickPlayer = Server.getInstance().getPlayer(strings[0]);
		String playerName = kickPlayer.getName();
		Player player = (Player) commandSender;

		if (kickPlayer == null) {
			commandSender.sendMessage("§fИгрок §a" + strings[0] + " §7- §cОффлайн");
			return true;
		}

		new CustomForm("Кикнуть игрока " + playerName).addLabel("Текст").addInput("Причина").addToggle("Отправить сообщение о кике в общий чат сервера §7•").send(player, (target, form, data) -> {
			if (data == null) return;
			kickPlayer.close("", "§l§c| §r§fВас кикнули с сервера! §l§e| §r§fПричина §7- " + data.get(1).toString());
		});
		return false;
	}
}