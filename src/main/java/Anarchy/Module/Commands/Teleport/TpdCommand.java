package Anarchy.Module.Commands.Teleport;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class TpdCommand extends Command {
	
	public TpdCommand() {
		super("tpd", "Отклонить запрос на телепортацию");
		commandParameters.clear();
	}
	
	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		Player player = (Player)sender;
		Player target = TpaCommand.TPA_REQUEST.get(player);
		if (target == null) {
			player.sendMessage(TpaCommand.PREFIX + "§fВы не имеете запросов на телепортацию, или Игрок покинул сервер");
			return true;
		}
		target.sendMessage(TpaCommand.PREFIX + "§fИгрок §3" + player.getName() + " §fотклонил Ваш запрос§7!");
		player.sendMessage(TpaCommand.PREFIX + "§fЗапрос Игрока §3" + target.getName() + " §fотклонен§7!");
		TpaCommand.TPA_REQUEST.remove(player);
		return true;
	}
}