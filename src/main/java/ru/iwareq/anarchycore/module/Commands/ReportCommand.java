package ru.iwareq.anarchycore.module.Commands;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import ru.contentforge.formconstructor.form.CustomForm;
import ru.contentforge.formconstructor.form.element.Dropdown;
import ru.contentforge.formconstructor.form.element.Input;
import ru.contentforge.formconstructor.form.element.SelectableElement;
import ru.iwareq.anarchycore.module.Cooldown.CooldownAPI;
import ru.iwareq.anarchycore.util.Utils;

import java.util.ArrayList;

public class ReportCommand extends Command {

	public ReportCommand() {
		super("report", "§rПожаловаться на Игрока");
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			ArrayList<SelectableElement> playersList = new ArrayList<>();
			for (Player players : Server.getInstance().getOnlinePlayers().values()) {
				playersList.add(new SelectableElement(players.getName()));
			}
			CustomForm customForm = new CustomForm("Пожаловаться на Игрока");
			customForm.addElement("target", new Dropdown("§l§6• §rВыберите игрока§7:", playersList));
			customForm.addElement("reason", new Input("§l§6• §rВведите причину§7:", "Он меня ударил :0"));
			customForm.send(player, (p, response) -> {
				String targetName = response.getDropdown("target").getValue().getValue(String.class);
				String reason = response.getInput("reason").getValue();
				if (reason == null || reason.equals("")) {
					player.sendMessage("§l§7(§3Репорты§7) §rВведите причину жалобы§7!");
					return;
				}
				player.sendMessage("§l§7(§3Репорты§7) §rРепорт на игрока §6" + targetName + " §fотправлен§7!");
				Utils.sendMessageToChat("Новая жалоба! (" + player.getName() + ")\nНарушитель: " + targetName + "\nПричина: " + reason);
				CooldownAPI.addCooldown(player, this.getName(), 300);
			});
		}
		return false;
	}
}