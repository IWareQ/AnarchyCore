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
		this.commandParameters.clear();
		this.commandParameters.put("default", new CommandParameter[]{new CommandParameter("player", CommandParamType.TARGET, false)});
	}
	
	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		Player player = (Player)sender;
		if (args.length != 1) {
			player.sendMessage("§l§6| §r§fИспользование §7- /§6tpa §7(§3игрок§7)");
			return true;
		}
		Player target = Server.getInstance().getPlayer(args[0]);
		if (target == null) {
			player.sendMessage("§l§6| §r§fИгрок §6" + args[0] + " §7- §3Оффлайн");
			return true;
		}
		player.sendMessage(PREFIX + "§fЗапрос на телепортицию к Игроку §6" + target.getName() + " §3успешно §fотправлен§7!");
		target.sendMessage(PREFIX + "§fИгрок §3" + player.getName() + " §fхочет телепортироваться к Вам§7!");
		target.sendMessage("§l§a| §r§7/§atpc §7- §fпринять запрос");
		target.sendMessage("§l§c| §r§7/§ctpd §7- §fотклонить запрос");
		TPA_REQUEST.put(target, player);
		return false;
	}
}