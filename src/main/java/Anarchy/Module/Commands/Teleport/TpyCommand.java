package Anarchy.Module.Commands.Teleport;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class TpyCommand extends Command {
	
	public TpyCommand() {
		super("tpy", "Принять запрос на телепортацию");
		commandParameters.clear();
	}
	
	@Override()
	public boolean execute(CommandSender commandSender, String s, String[] strings) {
		Player calledPlayer = TpaCommand.TPA_REQUEST.get(commandSender);
		if (calledPlayer == null) {
			commandSender.sendMessage(TpaCommand.PREFIX + "§fВы не имеете запросов на телепортацию§7, §fили Игрок покинул сервер");
			return true;
		}
		calledPlayer.sendMessage(TpaCommand.PREFIX + "§fИгрок §e" + commandSender.getName() + " §fпринял Ваш запрос§7!");
		commandSender.sendMessage(TpaCommand.PREFIX + "§fЗапрос Игрока §e" + calledPlayer.getName() + " §fпринят§7!");
		calledPlayer.teleport((Player)commandSender);
		TpaCommand.TPA_REQUEST.remove(commandSender);
		return true;
	}
}