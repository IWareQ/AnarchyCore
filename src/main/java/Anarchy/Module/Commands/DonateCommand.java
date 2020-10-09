package Anarchy.Module.Commands;

import FormAPI.Forms.Elements.SimpleForm;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class DonateCommand extends Command {

	public DonateCommand() {
		super("donate", "§l§fОписание Донат Услуг");
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player)sender;
			new SimpleForm("Описание Донат Услуг", "§l§fЕсли Вас §6устраивает §fлюбая привилегия§7, §fто Вы можете пройти на наш §6сайт §fи приобрести ее§7!\n\n§l§fНаш сайт§7: §6death§7-§6mc§7.§6online").addButton("§l§fДля чего нужен Донат§7?").send(player, (target, form, data)-> {
				if (data == -1) return;
				if (data == 0) {
					new SimpleForm("§l§fДля чего нужен Донат§7?", "§l§fДля того чтобы проект §6жил §fи §6развивался §fему нужны деньги §7:§fc\n§l§fК сожалению для всего в этом мире нужны деньги§7...\n§l§fДеньги идут на§7:\n§l§6• §fОплату хостинга §7(§fстоит достаточно дорого§7);\n§l§6• §fЗарплату сотрудникам§7;\n§l§6• §fРазработка собственных §6плагинов §fи §6режимов§7.\n\n§l§fНа это все идет много средств§7.\n\n§l§fИ мы безумно сильно благодарны за поддержку §6Проекта§7!").send(player);
				}
			});
		}
		return false;
	}
}