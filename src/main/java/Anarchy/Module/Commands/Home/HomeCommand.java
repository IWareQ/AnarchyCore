package Anarchy.Module.Commands.Home;

import java.util.Map;

import Anarchy.Utils.SQLiteUtils;
import Anarchy.Utils.StringUtils;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.BlockID;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;

public class HomeCommand extends Command {
	public static String PREFIX = "§l§7(§3Дом§7) §r";

	public HomeCommand() {
		super("home", "Телепортация домой");
		commandParameters.clear();
	}

	@Override
	public boolean execute(CommandSender commandSender, String s, String[] strings) {
		if (strings.length != 1) {
			commandSender.sendMessage("§l§e| §fИспользование §7- §e/home <название>");
			return false;
		}

		if (!StringUtils.isValidString(strings[0])) {
			commandSender.sendMessage(PREFIX + "§fВы можете использовать только буквы, цифры и нижнее подчеркивание");
			return false;
		}

		Map <String, String> homeData = SQLiteUtils.selectStringMap("Homes.db", "SELECT * FROM `HOMES` WHERE UPPER(`Home_Name`) = '" + strings[0].toUpperCase() + "' AND `Username` = '" + commandSender.getName() + "';");
		if (homeData == null || homeData.isEmpty()) {
			commandSender.sendMessage(PREFIX + "§fТочки дома §c" + strings[0] + " §7- §fне существует!\n§l§e| §r§fСписок всех точек §7- §e/homes");
			return false;
		}

		Level level = Server.getInstance().getLevelByName(homeData.get("LEVEL"));
		int x = Integer.parseInt(homeData.get("X")),
		y = Integer.parseInt(homeData.get("Y")),
		z = Integer.parseInt(homeData.get("Z"));
		if (level.getBlock(new Position(x, y, z, level)).getId() != BlockID.BED_BLOCK) {
			commandSender.sendMessage(PREFIX + "§fКровати нет на месте точки дома!");
			commandSender.sendMessage("§l§e| §r§fДля удаления точки дома используйте §7- §e/delhome");
			return false;
		}

		if (level.getBlock(new Position(x, y + 1, z, level)).getId() != 0 || level.getBlock(new Position(x, y + 2, z, level)).getId() != 0) {
			commandSender.sendMessage(PREFIX + "§fКровать застроена блоками!");
			commandSender.sendMessage("§l§e| §r§fДля удаления точки дома используйте §7- §e/delhome");
			return false;
		}

		commandSender.sendMessage(PREFIX + "§fВы телепортированы домой!");
		level.loadChunk(x >> 4, z >> 4);
		((Player) commandSender).teleport(new Position(x + 0.5, y + 1, z + 0.5, level));
		return false;
	}
}