package Anarchy.Module.Commands.Home;

import Anarchy.Utils.SQLiteUtils;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

import java.util.ArrayList;

public class HomesCommand extends Command {
	public HomesCommand() {
		super("homes", "Спискок домов");
		commandParameters.clear();
	}

	@Override
	public boolean execute(CommandSender commandSender, String s, String[] strings) {
		ArrayList<String> homesData = SQLiteUtils.selectStringList("Homes.db", "SELECT `Home_Name`, `X`, `Y`, `Z` FROM `HOMES` WHERE `Username` = '" + commandSender.getName() + "';");
		if (homesData == null || homesData.isEmpty()) {
			commandSender.sendMessage(HomeCommand.PREFIX + "§fВы не установили ни §c1 §fточку дома!");
			return false;
		}

		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i<homesData.size(); i += 4) {
			stringBuilder.append("§7, §a").append(homesData.get(i)).append(" §7(§e").append(homesData.get(i + 1)).append(", ").append(homesData.get(i + 2)).append(", ").append(homesData.get(i + 3)).append("§7)");
		}

		commandSender.sendMessage(HomeCommand.PREFIX + "§fТочки Ваших домов: " + stringBuilder.substring(4));
		return false;
	}
}