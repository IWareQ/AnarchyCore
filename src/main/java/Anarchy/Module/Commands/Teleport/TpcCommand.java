package Anarchy.Module.Commands.Teleport;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class TpcCommand extends Command {
	
	public TpcCommand() {
		super("tpc", "Принять запрос на телепортацию");
		commandParameters.clear();
	}
	
	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		Player player = (Player)sender;
		Player target = TpaCommand.TPA_REQUEST.get(player);
		if (target == null) {
			player.sendMessage(TpaCommand.PREFIX + "§fВы не имеете запросов на телепортацию§7, §fили Игрок покинул сервер");
			return true;
		}
		target.sendMessage(TpaCommand.PREFIX + "§fИгрок §3" + player.getName() + " §fпринял Ваш запрос§7!");
		player.sendMessage(TpaCommand.PREFIX + "§fЗапрос Игрока §3" + target.getName() + " §fпринят§7!");
		target.teleport(player);
		TpaCommand.TPA_REQUEST.remove(player);
		return true;
	}
}