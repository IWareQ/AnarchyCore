package Anarchy.Module.Commands.Teleport;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class TpyCommand extends Command {
	public TpyCommand() {
		super("tpy", "Принять запрос телепортации");
		commandParameters.clear();
	}

	@Override
	public boolean execute(CommandSender commandSender, String s, String[] strings) {
		Player calledPlayer = TpaCommand.TPA_REQUEST.get(commandSender);
		if (calledPlayer == null) {
			commandSender.sendMessage(TpaCommand.PREFIX + "§fВы не имеете запросов на телепортацию, или игрок покинул сервер");
			return true;
		}

		calledPlayer.sendMessage(TpaCommand.PREFIX + "§fИгрок §a" + commandSender.getName() + " §fпринял Ваш запрос!");
		commandSender.sendMessage(TpaCommand.PREFIX + "§fВы приняли запрос Игрока §a" + calledPlayer.getName());
		calledPlayer.teleport((Player) commandSender);
		TpaCommand.TPA_REQUEST.remove(commandSender);
		return true;
	}
}