package ru.iwareq.anarchycore.module.Commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import ru.iwareq.anarchycore.manager.Forms.Elements.SimpleForm;

public class RulesCommand extends Command {

	public RulesCommand() {
		super("rules", "§rОткрыть правила сервера");
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			SimpleForm simpleForm = new SimpleForm("Правила сервера");
			simpleForm.addContent("Твой текст");
			simpleForm.send((Player) sender, (targetPlayer, targetForm, data) -> {});
		}

		return false;
	}
}