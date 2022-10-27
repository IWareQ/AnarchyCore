package ru.iwareq.anarchycore.module.Commands.Teleport.Commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import ru.contentforge.formconstructor.form.SimpleForm;
import ru.contentforge.formconstructor.form.element.ImageType;
import ru.iwareq.anarchycore.module.CombatLogger.CombatLoggerAPI;
import ru.iwareq.anarchycore.module.Commands.Teleport.TeleportAPI;
import ru.iwareq.anarchycore.module.Commands.Teleport.Utils.TeleportUtils;

import java.util.LinkedList;
import java.util.List;

public class TpcCommand extends Command {

	public TpcCommand() {
		super("tpc", "§rПринять запрос на телепортацию");
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			List<TeleportUtils> tpList = new LinkedList<>(TeleportAPI.getTpaRequests());
			tpList.removeIf(tpUtils -> !tpUtils.getTarget().equals(player));
			if (tpList.isEmpty()) {
				player.sendMessage(TeleportAPI.PREFIX + "Вы §6не имеете §fзапросов на телепортацию§7!");
				return true;
			}
			SimpleForm simpleForm = new SimpleForm("Заявки на телепортацию", "Здесь показаны §6все §fзаявки§7.\n\n§rЗапрос действует только §630 §fсекунд§7!");
			if (tpList.size() >= 2) {
				simpleForm.addButton("§fНажмите§7, §fчтобы принять всех§7!", (p, button) -> {
					TeleportAPI.getTpaRequests().removeIf(tpList::contains);

					tpList.forEach(tp -> {
						if (!tp.isOutdated() && tp.getPlayer() != null && CombatLoggerAPI.inCombat(tp.getPlayer())) {
							tp.getPlayer().teleport(player);
							tp.getPlayer().sendMessage(TeleportAPI.PREFIX + "Игрок §6" + tp.getTarget().getName() + " " + "§fпринял Ваш запрос§7!");
						}
					});

					player.sendMessage(TeleportAPI.PREFIX + "Все запросы приняты§7!");
				});
			}

			for (TeleportUtils tpUtils : tpList) {
				simpleForm.addButton("§6" + tpUtils.getPlayer().getName() + "\n§fНажмите§7, §fчтобы принять§7!",
						ImageType.PATH, "textures/ui/Friend2", (p, button) -> {
							// TeleportUtils tpUtils = tpList.get(data);

							if (tpUtils.isOutdated()) {
								player.sendMessage(TeleportAPI.PREFIX + "Запрос на телепортацию §6истек§7!");
								return;
							}
							if (tpUtils.getPlayer() != null) {
								if (CombatLoggerAPI.inCombat(tpUtils.getPlayer())) {
									player.sendMessage(TeleportAPI.PREFIX + "Игрок §6" + tpUtils.getPlayer().getName() + " §fнаходится в режиме §6PvP§7!");
									TeleportAPI.getTpaRequests().remove(tpUtils);
									return;
								}
								tpUtils.getPlayer().sendMessage(TeleportAPI.PREFIX + "Игрок §6" + tpUtils.getTarget().getName() + " §fпринял Ваш запрос§7!");
								player.sendMessage(TeleportAPI.PREFIX + "Запрос игрока §6" + tpUtils.getPlayer().getName() + " §fпринят§7!");
								tpUtils.getPlayer().teleport(player);
							} else {
								player.sendMessage(TeleportAPI.PREFIX + "Игрок §6отправивший §fВам запрос §6на телепортацию§7, §fне в сети§7!");
							}
							TeleportAPI.getTpaRequests().remove(tpUtils);
						});
			}

			simpleForm.send(player);
		}
		return false;
	}
}