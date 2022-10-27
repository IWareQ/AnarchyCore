package ru.iwareq.anarchycore.module.AdminSystem.Commands;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import ru.contentforge.formconstructor.form.CustomForm;
import ru.contentforge.formconstructor.form.element.Dropdown;
import ru.contentforge.formconstructor.form.element.Label;
import ru.contentforge.formconstructor.form.element.SelectableElement;
import ru.contentforge.formconstructor.form.element.StepSlider;
import ru.iwareq.anarchycore.module.AdminSystem.AdminAPI;
import ru.iwareq.anarchycore.util.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BanCommand extends Command {

	public BanCommand() {
		super("ban", "§rБлокировка Аккаунта");
		this.setPermission("Command.Ban");
		this.commandParameters.clear();
		this.commandParameters.put("ban", new CommandParameter[]{
				CommandParameter.newType("player", CommandParamType.TARGET)
		});
	}

	public static void openBanPlayerForm(Player player, String targetName) {
		List<String> reasons = Arrays.asList("3.1 (Читы)", "3.2 (Скин)", "3.3 (Багоюз)", "3.4 (Само-признание)");
		List<SelectableElement> reasonList = new ArrayList<>();
		reasons.forEach(reson -> {
			reasonList.add(new SelectableElement(reson));
		});

		CustomForm customForm = new CustomForm("Блокировка Аккаунта §6" + targetName);
		customForm.addElement("reason", new Dropdown("§l§6• §rПричина блокировки Аккаунта§7:", reasonList));
		customForm.addElement(new Label("§l§6• §rВремя блокировки Аккаунта§7:"));
		List<SelectableElement> minutes = new ArrayList<>();
		for (int i = 0; i < 60; i++) {
			if (i % 5 == 0) {
				minutes.add(new SelectableElement(String.valueOf(i)));
			}
		}
		customForm.addElement("minutes", new StepSlider("§6Минуты§7", minutes, 0));
		List<SelectableElement> hours = new ArrayList<>();
		for (int i = 0; i < 24; i++) {
			hours.add(new SelectableElement(String.valueOf(i)));
		}
		customForm.addElement("hours", new StepSlider("§6Часы§7", hours, 0));
		List<SelectableElement> days = new ArrayList<>();
		for (int i = 0; i < 30; i++) {
			days.add(new SelectableElement(String.valueOf(i)));
		}
		customForm.addElement("days", new StepSlider("§6Дни§7", days, 0));
		customForm.send(player, (p, response) -> {
			if (AdminAPI.isBanned(targetName)) {
				player.sendMessage(AdminAPI.PREFIX + "Игрок §6" + targetName + " §fуже заблокирован§7!");
				return;
			}

			float time = (response.getStepSlider("minutes").getValue().getValue(Integer.class) * 60) +
					(response.getStepSlider("hours").getValue().getValue(Integer.class) * 3600) +
					(response.getStepSlider("days").getValue().getValue(Integer.class) * 86400);
			String reason = response.getDropdown("reason").getValue().getText();
			AdminAPI.addBan(targetName, player.getName(), reason, (int) time);

			Utils.sendMessageAdmins(AdminAPI.PREFIX + "Аккаунт игрока §6" + targetName + " §fбыл заблокирован " +
					"Администратором §6" + player.getName() + " §fпо причине§7: §6" + reason + "§7!", 1);
		});
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (!player.hasPermission(this.getPermission())) {
				return false;
			}
			if (args.length != 1) {
				player.sendMessage("§l§6• §rИспользование §7- /§6ban §7(§6игрок§7)");
				return true;
			}
			String targetName = Utils.implode(args, 0);
			if (AdminAPI.isBanned(targetName)) {
				player.sendMessage(AdminAPI.PREFIX + "Игрок §6" + targetName + " §fуже заблокирован§7!");
				return true;
			}
			Player target = Server.getInstance().getPlayer(targetName);
			if (target == null) {
				AdminAPI.sendSearchPlayerForm(player, targetName);
				return true;
			}
			openBanPlayerForm(player, target.getName());
		}
		return false;
	}
}