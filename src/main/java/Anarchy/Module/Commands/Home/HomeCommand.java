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
		super("home", "§r§fТелепортироватся домой");
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player)sender;
			Map<String, String> homeData = SQLiteUtils.selectStringMap("Homes.db", "SELECT * FROM HOMES WHERE UPPER(Username) = \'" + player.getName().toUpperCase() + "\';");
			if (homeData == null || homeData.isEmpty()) {
				player.sendMessage(PREFIX + "§fТочек дома не обнаружено§7, §fдля создания используйте §7/§6sethome");
			} else {
				int x = Integer.parseInt(homeData.get("X"));
				int y = Integer.parseInt(homeData.get("Y"));
				int z = Integer.parseInt(homeData.get("Z"));
				player.sendMessage(PREFIX + "§fВы успешно телепортированы домой§7!");
				player.teleport(new Position(x + 0.5, y + 0.5, z + 0.5, FunctionsAPI.MAP));
			}
		}
		return false;
	}
}