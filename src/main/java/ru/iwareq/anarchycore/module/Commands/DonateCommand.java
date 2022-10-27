package ru.iwareq.anarchycore.module.Commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import ru.contentforge.formconstructor.form.SimpleForm;

public class DonateCommand extends Command {

	public DonateCommand() {
		super("donate", "§rОписание Донат Услуг");
		this.commandParameters.clear();
	}

	private static void sendDonateListForm(Player player) {
		SimpleForm simpleForm = new SimpleForm("Описание Донат Услуг");
		simpleForm.addContent("Выберите один из пунктов§7, §fчтобы увидеть описание привилегии§7.");
		simpleForm.addContent("\n\n§fДелая любое пожертвование §7- §fВы очень сильно помогаете серверу§7!");
		simpleForm.addContent("\n§fНаш сайт§7: §6death§7-§6mc§7.§6ru");
		simpleForm.send(player);
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			sendDonateListForm((Player) sender);
		}
		return false;
	}
}