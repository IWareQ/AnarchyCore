package Anarchy.Module.Commands;

import FormAPI.Forms.Elements.SimpleForm;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class DonateCommand extends Command {
	
	public DonateCommand() {
		super("donate", "Описание Донат Услуг");
	}
	
	@Override()
	public boolean execute(CommandSender commandSender, String s, String[] args) {
		Player player = (Player)commandSender;
		new SimpleForm("Описание Донат Услуг", "§l§fЕсли Вас §3устраивает §fлюбая привилегия§7, §fто Вы можете пройти на наш §3сайт §fи приобрести ее§7!\n\n§l§fНаш сайт §7- §3deathmc§7.§3mcpetrade§7.§3ru").addButton("§l§fДля чего нужен Донат§7?").send(player, (target, form, data)->{
			if (data == -1) return;
			if (data == 0) {
				new SimpleForm("§l§fДля чего нужен Донат§7?", "§l§fДля того чтобы проект §3жил §fи §3развивался §fему нужны деньги §7:§fc\n§l§fК сожалению для всего в этом мире нужны деньги§7...\n§l§fДеньги идут на§7:\n§l§e• §fОплату хостинга §7(§fстоитдостаточно дорого§7);\n§l§e• §fЗарплату сотрудникам§7;\n§l§e• §fРазработка собственных §3плагинов §fи §3режимов§7.\n\n§l§fНа это все идет много средств§7.\n\n§l§fИ мы безумно сильно благодарны за поддержку §3Проекта§7!").send(player);
			}
		});
		return false;
	}
}