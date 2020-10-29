package Anarchy.Module.Commands.Teleport;

import Anarchy.Module.Commands.Teleport.Utils.TpaPlayer;
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
		if (sender instanceof Player) {
			Player player = (Player)sender;
			if (TpaCommand.TPA_REQUEST.containsKey(player.getName())) {
				TpaPlayer target = TpaCommand.TPA_REQUEST.get(player.getName());
				if (target.getTime() < System.currentTimeMillis() / 1000) {
					player.sendMessage(TpaCommand.PREFIX + "§fЗапрос на телепортацию истек§7!");
					TpaCommand.TPA_REQUEST.remove(player.getName());
					return true;
				}
				if (target.getPlayer() != null) {
					target.getPlayer().sendMessage(TpaCommand.PREFIX + "§fИгрок §6" + player.getName() + " §fотклонил Ваш запрос§7!");
					player.sendMessage(TpaCommand.PREFIX + "§fЗапрос Игрока §6" + target.getPlayer().getName() + " §fотклонен§7!");
					TpaCommand.TPA_REQUEST.remove(player.getName());
				} else {
					player.sendMessage(TpaCommand.PREFIX + "§fИгрок который отправил Вам запрос на телепортацию§7, §fне в сети§7!");
					TpaCommand.TPA_REQUEST.remove(player.getName());
				}
			} else {
				player.sendMessage(TpaCommand.PREFIX + "§fВы не имеете запросов на телепортацию§7!");
			}
		}
		return false;
	}
}