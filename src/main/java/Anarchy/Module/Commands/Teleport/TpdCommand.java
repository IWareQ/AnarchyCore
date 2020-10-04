package Anarchy.Module.Commands.Teleport;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class TpdCommand extends Command {
	
	public TpdCommand() {
		super("tpd", "Отклонить запрос на телепортацию");
		this.commandParameters.clear();
	}
	
	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		Player player = (Player)sender;
		Player target = TpaCommand.TPA_REQUEST.get(player);
		if (!target.isOnline()) {
			player.sendMessage(TpaCommand.PREFIX + "§fВы не имеете запросов на телепортацию, или Игрок покинул сервер");
		}
		target.sendMessage(TpaCommand.PREFIX + "§fИгрок §6" + player.getName() + " §fотклонил Ваш запрос§7!");
		player.sendMessage(TpaCommand.PREFIX + "§fЗапрос Игрока §6" + target.getName() + " §fотклонен§7!");
		TpaCommand.TPA_REQUEST.remove(player);
		return false;
	}
}