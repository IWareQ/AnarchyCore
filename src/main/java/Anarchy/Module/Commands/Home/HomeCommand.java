package Anarchy.Module.Commands.Home;

import java.util.Map;

import Anarchy.Utils.SQLiteUtils;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;

public class HomeCommand extends Command {
	public static String PREFIX = "§l§7(§3Дом§7) §r";
	
	public HomeCommand() {
		super("home", "Телепортироватся домой");
		commandParameters.clear();
	}
	
	@Override()
	public boolean execute(CommandSender commandSender, String s, String[] strings) {
		Player player = (Player)commandSender;
		String playerName = player.getName();
		String upperCase = playerName.toUpperCase();
		Map<String, String> homeData = SQLiteUtils.selectStringMap("Homes.db", "SELECT * FROM `HOMES` WHERE UPPER(`Username`) = \'" + upperCase + "\';");
		if (homeData == null || homeData.isEmpty()) {
			commandSender.sendMessage(PREFIX + "§fВы еще не поставили ни 1 точки дома");
		} else {
		Level level = Server.getInstance().getLevelByName(homeData.get("LEVEL"));
		int x = Integer.parseInt(homeData.get("X"));
		int y = Integer.parseInt(homeData.get("Y"));
		int z = Integer.parseInt(homeData.get("Z"));
		commandSender.sendMessage(PREFIX + "§fВы §3успешно §fтелепортированы домой§7!");
		player.teleport(new Position(x, y, z, level));
	}
		return false;
	}
}