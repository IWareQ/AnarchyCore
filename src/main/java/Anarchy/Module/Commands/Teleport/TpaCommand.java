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
	public static String PREFIX = "§l§7(§3Телепорт§7) §r";
	public static Map<Player, Player> TPA_REQUEST = new HashMap<>();
	
	public TpaCommand() {
		super("tpa", "Отправить запрос на телепортацию");
		commandParameters.clear();
		this.commandParameters.put("default", new CommandParameter[]{new CommandParameter("player", CommandParamType.TARGET, false)});
	}
	
	@Override()
	public boolean execute(CommandSender commandSender, String s, String[] strings) {
		if (strings.length != 1) {
			commandSender.sendMessage("§l§6| §r§fИспользование §7- /§6tpa §7(§3игрок§7)");
			return true;
		}
		Player calledPlayer = Server.getInstance().getPlayer(strings[0]);
		if (calledPlayer == null) {
			commandSender.sendMessage("§l§6| §r§fИгрок §6" + strings[0] + " §7- §3Оффлайн");
			return true;
		}
		commandSender.sendMessage(PREFIX + "§fЗапрос на телепортицию к Игроку §6" + calledPlayer.getName() + " §3успешно §fотправлен§7!");
		calledPlayer.sendMessage(PREFIX + "§fИгрок §3" + commandSender.getName() + " §fхочет телепортироваться к Вам§7!");
		calledPlayer.sendMessage("§l§a| §r§7/§atpy §7- §fпринять запрос");
		calledPlayer.sendMessage("§l§c| §r§7/§ctpn §7- §fотклонить запрос");
		TPA_REQUEST.put(calledPlayer, (Player)commandSender);
		return false;
	}
}