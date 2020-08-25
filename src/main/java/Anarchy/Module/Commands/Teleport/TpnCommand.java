package Anarchy.Module.Commands.Teleport;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class TpnCommand extends Command {
	
	public TpnCommand() {
		super("tpn", "Отклонить запрос на телепортацию");
		commandParameters.clear();
	}
	
	@Override()
	public boolean execute(CommandSender commandSender, String s, String[] strings) {
		Player calledPlayer = TpaCommand.TPA_REQUEST.get(commandSender);
		if (calledPlayer == null) {
			commandSender.sendMessage(TpaCommand.PREFIX + "§fВы не имеете запросов на телепортацию, или Игрок покинул сервер");
			return true;
		}
		calledPlayer.sendMessage(TpaCommand.PREFIX + "§fИгрок §3" + commandSender.getName() + " §fотклонил Ваш запрос§7!");
		commandSender.sendMessage(TpaCommand.PREFIX + "§fЗапрос Игрока §3" + calledPlayer.getName() + " §fотклонен§7!");
		TpaCommand.TPA_REQUEST.remove(commandSender);
		return true;
	}
}