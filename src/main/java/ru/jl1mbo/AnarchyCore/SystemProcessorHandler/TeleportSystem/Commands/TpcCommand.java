package ru.jl1mbo.AnarchyCore.SystemProcessorHandler.TeleportSystem.Commands;

import java.util.LinkedList;
import java.util.List;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import ru.jl1mbo.AnarchyCore.GameHandler.CombatLogger.CombatLoggerAPI;
import ru.jl1mbo.AnarchyCore.Manager.Forms.Elements.ImageType;
import ru.jl1mbo.AnarchyCore.Manager.Forms.Elements.SimpleForm;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.TeleportSystem.TeleportAPI;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.TeleportSystem.Utils.TeleportUtils;

public class TpcCommand extends Command {

	public TpcCommand() {
		super("tpc", "§rПринять запрос на телепортацию");
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			List<TeleportUtils> tpList = new LinkedList<>(TeleportAPI.getTpaRequest());
			tpList.removeIf(tpUtils -> !tpUtils.getTarget().equals(player));
			if (tpList.isEmpty()) {
				player.sendMessage(TeleportAPI.PREFIX + "Вы §6не имеете §fзапросов на телепортацию§7!");
				return true;
			}
			SimpleForm simpleForm = new SimpleForm("Заявки на телепортацию",
												   "Здесь показаны §6все §fзаявки§7.\n\n§rЗапрос действует только §630 §fсекунд§7!");
			for (TeleportUtils tpUtils : tpList) {
				simpleForm.addButton("§6" + tpUtils.getPlayer().getName() + "\n§fНажмите§7, §fчтобы принять", ImageType.PATH, "textures/ui/Friend2");
			}
			simpleForm.send(player, (targetPlayer, targetForm, data) -> {
				if (data == -1) return;
				TeleportUtils tpUtils = tpList.get(data);

				if (tpUtils.isOutdated()) {
					player.sendMessage(TeleportAPI.PREFIX + "Запрос на телепортацию §6истек§7!");
					return;
				}
				if (tpUtils.getPlayer() != null) {
					if (CombatLoggerAPI.inCombat(tpUtils.getPlayer())) {
						player.sendMessage(TeleportAPI.PREFIX + "Игрок §6" + tpUtils.getPlayer().getName() + " §fнаходится в режиме §6PvP§7!");
						TeleportAPI.getTpaRequest().remove(tpUtils);
						return;
					}
					tpUtils.getPlayer().sendMessage(TeleportAPI.PREFIX + "Игрок §6" + tpUtils.getTarget().getName() + " §fпринял Ваш запрос§7!");
					player.sendMessage(TeleportAPI.PREFIX + "Запрос игрока §6" + tpUtils.getPlayer().getName() + " §fпринят§7!");
					tpUtils.getPlayer().teleport(player);
					TeleportAPI.getTpaRequest().remove(tpUtils);
				} else {
					player.sendMessage(TeleportAPI.PREFIX + "Игрок §6отправивший §fВам запрос §6на телепортацию§7, §fне в сети§7!");
					TeleportAPI.getTpaRequest().remove(tpUtils);
				}
			});
		}
		return false;
	}
}