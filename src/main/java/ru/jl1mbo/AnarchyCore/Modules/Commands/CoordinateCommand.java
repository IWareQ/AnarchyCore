package ru.jl1mbo.AnarchyCore.Modules.Commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.GameRule;
import cn.nukkit.network.protocol.GameRulesChangedPacket;
import ru.jl1mbo.AnarchyCore.Manager.WorldSystem.WorldSystemAPI;

public class CoordinateCommand extends Command {

	public CoordinateCommand() {
		super("coordinate", "§rУправление координатами");
		this.setPermission("Command.Coordinate");
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (!player.hasPermission(this.getPermission())) {
				return false;
			}
			if (player.getLevel().equals(WorldSystemAPI.getSpawn()) && !player.hasPermission("Development")) {
				player.sendMessage("§l§c• §rДанная команда §6заблокирована §fв этом мире§7!");
				return true;
			}
			GameRulesChangedPacket gameRulesChanged = new GameRulesChangedPacket();
			gameRulesChanged.gameRules = WorldSystemAPI.getTest().getGameRules();
			if (gameRulesChanged.gameRules.getBoolean(GameRule.SHOW_COORDINATES)) {
				gameRulesChanged.gameRules.setGameRule(GameRule.SHOW_COORDINATES, false);
				player.dataPacket(gameRulesChanged);
				player.sendMessage("§l§6• §rКоординаты успешно §6выключены§7!");
			} else {
				gameRulesChanged.gameRules.setGameRule(GameRule.SHOW_COORDINATES, true);
				player.dataPacket(gameRulesChanged);
				player.sendMessage("§l§6• §rКоординаты успешно §6включены§7!");
			}
		}
		return false;
	}
}