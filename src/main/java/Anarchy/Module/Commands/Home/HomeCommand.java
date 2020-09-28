package Anarchy.Module.Commands.Home;

import java.util.Map;

import Anarchy.Manager.Functions.FunctionsAPI;
import Anarchy.Utils.SQLiteUtils;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Position;

public class HomeCommand extends Command {
	public static String PREFIX = "§l§7(§3Дом§7) §r";
	
	public HomeCommand() {
		super("home", "Телепортироватся домой");
		this.commandParameters.clear();
	}
	
	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("§l§7(§3Система§7) §r§fЭту команду можно использовать только в §3Игре");
			return true;
		}
		Player player = (Player)sender;
		String playerName = player.getName();
		String upperCase = playerName.toUpperCase();
		Map<String, String> homeData = SQLiteUtils.selectStringMap("Homes.db", "SELECT * FROM `HOMES` WHERE UPPER(`Username`) = \'" + upperCase + "\';");
		if (homeData == null || homeData.isEmpty()) {
			player.sendMessage(PREFIX + "§fТочек дома не обнаружено§7, §fдля создания используйте §7/§3sethome");
		} else {
			int x = Integer.parseInt(homeData.get("X"));
			int y = Integer.parseInt(homeData.get("Y"));
			int z = Integer.parseInt(homeData.get("Z"));
			player.sendMessage(PREFIX + "§fВы успешно телепортированы домой§7!");
			player.teleport(new Position(x, y + 0.5, z, FunctionsAPI.MAP));
		}
		return false;
	}
}