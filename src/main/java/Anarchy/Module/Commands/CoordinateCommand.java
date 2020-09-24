package Anarchy.Module.Commands;

import Anarchy.Manager.Functions.FunctionsAPI;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.level.GameRule;
import cn.nukkit.network.protocol.GameRulesChangedPacket;

public class CoordinateCommand extends Command {
	
	public CoordinateCommand() {
		super("coordinate", "§l§fВключить§7/§fВыключить координаты");
		this.setPermission("Command.Coordinate");
		this.commandParameters.clear();
		this.commandParameters.put("default", new CommandParameter[]{new CommandParameter("value", new String[]{"on", "off"})});
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
		if (args.length == 0) {
			player.sendMessage("§l§6| §r§fИспользование §7- /§6coordinate on§7/§6off");
			return true;
		}
		switch (args[0]) {
			case "on": 
			{
				GameRulesChangedPacket gameRulesChanged = new GameRulesChangedPacket();
				gameRulesChanged.gameRules = FunctionsAPI.COORDINATE.getGameRules();
				gameRulesChanged.gameRules.setGameRule(GameRule.SHOW_COORDINATES, true);
				player.dataPacket(gameRulesChanged);
				player.sendMessage("§l§a| §r§fКоординаты успешно включены§7!");
			}
			break;
			
			case "off": 
			{
				GameRulesChangedPacket gameRulesChanged = new GameRulesChangedPacket();
				gameRulesChanged.gameRules = FunctionsAPI.COORDINATE.getGameRules();
				gameRulesChanged.gameRules.setGameRule(GameRule.SHOW_COORDINATES, false);
				player.dataPacket(gameRulesChanged);
				player.sendMessage("§l§a| §r§fКоординаты успешно отключены§7!");
			}
			
		}
		return false;
	}
}