package AnarchySystem.Components.Commands;

import AnarchySystem.Manager.WorldSystem.WorldSystemAPI;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.level.GameRule;
import cn.nukkit.network.protocol.GameRulesChangedPacket;

public class CoordinateCommand extends Command {

	public CoordinateCommand() {
		super("coordinate", "§r§fУправление координатами");
		this.setPermission("Command.Coordinate");
		this.commandParameters.clear();
		this.commandParameters.put("default", new CommandParameter[] {new CommandParameter("value", new String[]{"on", "off"})});
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (!player.hasPermission("Command.Coordinate")) {
				return false;
			}
			if (args.length == 0) {
				player.sendMessage("§l§6• §r§fИспользование §7- /§6coordinate §3on§7/§3off");
				return true;
			}
			if (player.getLevel().equals(WorldSystemAPI.getSpawn()) && !player.hasPermission("Development")) {
				player.sendMessage("§l§c• §r§fДанная команда заблокирована в этом мире§7!");
				return false;
			}
			switch (args[0]) {
			case "on": {
				GameRulesChangedPacket gameRulesChanged = new GameRulesChangedPacket();
				gameRulesChanged.gameRules = WorldSystemAPI.getTest().getGameRules();
				gameRulesChanged.gameRules.setGameRule(GameRule.SHOW_COORDINATES, true);
				player.dataPacket(gameRulesChanged);
				player.sendMessage("§l§a• §r§fКоординаты успешно включены§7!");
			}
			break;
			case "off": {
				GameRulesChangedPacket gameRulesChanged = new GameRulesChangedPacket();
				gameRulesChanged.gameRules = WorldSystemAPI.getTest().getGameRules();
				gameRulesChanged.gameRules.setGameRule(GameRule.SHOW_COORDINATES, false);
				player.dataPacket(gameRulesChanged);
				player.sendMessage("§l§a• §r§fКоординаты успешно отключены§7!");
			}
			break;
			}
		}
		return false;
	}
}