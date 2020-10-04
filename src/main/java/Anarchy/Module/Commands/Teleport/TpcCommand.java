package Anarchy.Module.Commands.Teleport;

import Anarchy.Module.CombatLogger.CombatLoggerAPI;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class TpcCommand extends Command {
	
	public TpcCommand() {
		super("tpc", "Принять запрос на телепортацию");
		this.commandParameters.clear();
	}
	
	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		Player player = (Player)sender;
		Player target = TpaCommand.TPA_REQUEST.get(player);
		if (!target.isOnline()) {
			player.sendMessage(TpaCommand.PREFIX + "§fВы не имеете запросов на телепортацию§7, §fили Игрок покинул сервер§7!");
		}
		if (CombatLoggerAPI.inCombat(target)) {
			player.sendMessage(TpaCommand.PREFIX + "§fИгрок §6" + target.getName() + " §fнаходится в режиме §6PvP§7!");
			TpaCommand.TPA_REQUEST.remove(player);
		} else {
		target.sendMessage(TpaCommand.PREFIX + "§fИгрок §6" + player.getName() + " §fпринял Ваш запрос§7!");
		player.sendMessage(TpaCommand.PREFIX + "§fЗапрос Игрока §6" + target.getName() + " §fпринят§7!");
		target.teleport(player);
		TpaCommand.TPA_REQUEST.remove(player);
		}
		return false;
	}
}