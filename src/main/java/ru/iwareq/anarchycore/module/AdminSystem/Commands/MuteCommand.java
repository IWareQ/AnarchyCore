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

public class MuteCommand extends Command {

	public MuteCommand() {
		super("mute", "§rБлокировка Чата");
		this.setPermission("Command.Mute");
		this.commandParameters.clear();
		this.commandParameters.put("mute", new CommandParameter[]{
				CommandParameter.newType("player", CommandParamType.TARGET)
		});
	}

	public static void openMutePlayerForm(Player player, String targetName) {
		List<String> reasons = Arrays.asList("2.1 (Обсуж. действий перносала)", "2.2 (Дискриминация)", "2.3 " +
				"(Пропаганда)", "2.4 (Оскорбления)", "2.5 (Спам, флуд)", "2.6 (Реклама)");
		List<SelectableElement> reasonList = new ArrayList<>();
		reasons.forEach(reson -> {
			reasonList.add(new SelectableElement(reson));
		});
		CustomForm customForm = new CustomForm("Блокировка Чата §6" + targetName);
		customForm.addElement("reason", new Dropdown("§l§6• §rПричина блокировки чата§7:", reasonList));
		customForm.addElement(new Label("§l§6• §rВремя блокировки чата§7:"));
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
			if (AdminAPI.isMuted(targetName)) {
				player.sendMessage(AdminAPI.PREFIX + "Чат игрока §6" + targetName + " §fуже заблокирован§7!");
				return;
			}
			float time = (response.getStepSlider("minutes").getValue().getValue(Integer.class) * 60) +
					(response.getStepSlider("hours").getValue().getValue(Integer.class) * 3600) +
					(response.getStepSlider("days").getValue().getValue(Integer.class) * 86400);
			String reason = response.getDropdown("reason").getValue().getText();
			AdminAPI.addMute(targetName, player.getName(), reason, (int) time);
			player.sendMessage(AdminAPI.PREFIX + "Чат игрока §6" + targetName + " §fбыл успешно заблокирован по причине §6" + reason + "§7!");
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
				player.sendMessage("§l§6• §rИспользование §7- /§6mute §7(§6игрок§7)");
				return true;
			}
			String targetName = Utils.implode(args, 0);
			if (AdminAPI.isMuted(targetName)) {
				player.sendMessage(AdminAPI.PREFIX + "Чат игрока §6" + targetName + " §fуже заблокирован§7!");
				return true;
			}
			Player target = Server.getInstance().getPlayer(targetName);
			if (target == null) {
				AdminAPI.sendSearchPlayerForm(player, targetName);
				return true;
			}
			openMutePlayerForm(player, target.getName());
		}
		return false;
	}
}