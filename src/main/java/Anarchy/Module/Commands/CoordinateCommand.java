package Anarchy.Module.Commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.Level;
import cn.nukkit.network.protocol.GameRulesChangedPacket;

public class CoordinateCommand extends Command {
	
	public CoordinateCommand() {
		super("coordinate", "§l§fВключить§7/§fВыключить координаты");
		setPermission("Command.Coordinate");
		commandParameters.clear();
	}
	
	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		Player player = (Player)sender;
		if (!(sender instanceof Player)) {
			sender.sendMessage("§l§7(§3Система§7) §r§fЭту команду можно использовать только в §3Игре");
			return true;
		}
		if (!player.hasPermission("Command.Coordinate")) {
			return false;
		}
		switch (args[0]) {
			case "on": 
			{
				Level level = player.getLevel();
				GameRulesChangedPacket gameRulesChanged = new GameRulesChangedPacket();
				gameRulesChanged.gameRules = level.getGameRules();
				gameRulesChanged.gameRules.setGameRule(GameRule.SHOW_COORDINATES, true);
				player.sendMessage("§l§a| §r§fКоординаты успешно включены§7!");
			}
			break;
			
			case "off": 
			{
				Level level = player.getLevel();
				GameRulesChangedPacket gameRulesChanged = new GameRulesChangedPacket();
				gameRulesChanged.gameRules = level.getGameRules();
				gameRulesChanged.gameRules.setGameRule(GameRule.SHOW_COORDINATES, false);
				player.sendMessage("§l§a| §r§fКоординаты успешно отключены§7!");
			}
		}
		return false;
	}
}