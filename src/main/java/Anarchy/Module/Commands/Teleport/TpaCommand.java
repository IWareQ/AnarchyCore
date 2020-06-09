package Anarchy.Module.Commands.Teleport;

import java.util.HashMap;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;

public class TpaCommand extends Command {
	public static String PREFIX = "§l§7(§aТелепорт§7) §r";
	public static Map <Player, Player> TPA_REQUEST = new HashMap < >();

	public TpaCommand() {
		super("tpa", "Запрос телепортации");
		commandParameters.clear();
		this.commandParameters.put("default", new CommandParameter[] {
			new CommandParameter("player", CommandParamType.TARGET, false)
		});
	}

	@Override
	public boolean execute(CommandSender commandSender, String s, String[] strings) {
		if (strings.length != 1) {
			commandSender.sendMessage("§l§e| §fИспользование §7- §e/tpa <игрок>");
			return true;
		}

		Player calledPlayer = Server.getInstance().getPlayer(strings[0]);
		if (calledPlayer == null) {
			commandSender.sendMessage(PREFIX + "§fИгрок §a" + strings[0] + " §7- §cОффлайн");
			return true;
		}

		commandSender.sendMessage(PREFIX + "§fВы отправили запрос Игроку §a" + calledPlayer.getName());
		calledPlayer.sendMessage(PREFIX + "§fИгрок §a" + commandSender.getName() + " §fхочет телепортироваться к Вам");
		calledPlayer.sendMessage("§l§a| §r§a/tpy §7- §fпринять запрос");
		calledPlayer.sendMessage("§l§e| §r§c/tpn §7- §fотклонить запрос");

		TPA_REQUEST.put(calledPlayer, (Player) commandSender);
		return false;
	}
}