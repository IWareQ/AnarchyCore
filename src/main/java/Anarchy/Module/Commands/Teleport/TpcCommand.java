package Anarchy.Module.Commands.Teleport;

import Anarchy.AnarchyMain;
import Anarchy.Module.Commands.Spectate.SpectateAPI;
import Anarchy.Module.Commands.Teleport.Utils.TpaPlayer;
import CombatLoggerAPI.CombatLogger.CombatLoggerAPI;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class TpcCommand extends Command {

	public TpcCommand() {
		super("tpc", "§§fПринять запрос на телепортацию");
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player)sender;
			if (TpaCommand.TPA_REQUEST.containsKey(player.getName())) {
				TpaPlayer target = TpaCommand.TPA_REQUEST.get(player.getName());
				if (target.getTime() < System.currentTimeMillis() / 1000L) {
					player.sendMessage(TpaCommand.PREFIX + "§fЗапрос на телепортацию истек§7!");
					TpaCommand.TPA_REQUEST.remove(player.getName());
					return true;
				}
				if (SpectateAPI.SPECTATE_PLAYERS.containsKey(player.getName())) {
					player.sendMessage(AnarchyMain.PREFIX + "§fДанная команда заблокированна в режиме Наблюдения§7!\n§l§6• §r§fДля окончания наблюдения используйте §6Редстоун§7!");
					return true;
				}
				if (target.getPlayer() != null) {
					if (CombatLoggerAPI.inCombat(target.getPlayer())) {
						player.sendMessage(TpaCommand.PREFIX + "§fИгрок §6" + target.getPlayer().getName() + " §fнаходится в режиме §6PvP§7!");
						TpaCommand.TPA_REQUEST.remove(player.getName());
					} else {
						target.getPlayer().sendMessage(TpaCommand.PREFIX + "§fИгрок §6" + player.getName() + " §fпринял Ваш запрос§7!");
						player.sendMessage(TpaCommand.PREFIX + "§fЗапрос Игрока §6" + target.getPlayer().getName() + " §fпринят§7!");
						target.getPlayer().teleport(player);
						TpaCommand.TPA_REQUEST.remove(player.getName());
					}
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