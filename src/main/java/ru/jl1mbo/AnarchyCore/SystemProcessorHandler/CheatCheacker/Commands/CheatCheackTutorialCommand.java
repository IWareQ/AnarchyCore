package ru.jl1mbo.AnarchyCore.SystemProcessorHandler.CheatCheacker.Commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import ru.jl1mbo.AnarchyCore.Manager.Forms.Elements.SimpleForm;

public class CheatCheackTutorialCommand extends Command {

	public CheatCheackTutorialCommand() {
		super("cheatcheacktutorial", "§r§fИнформация по прохождению Проверки", "", new String[] {"cct"});
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player)sender;
			SimpleForm simpleForm = new SimpleForm("§lКак пройти Проверку§7?");
			simpleForm.setContent("§r§fПервое что Вам надо сделать §7- §fэто §6не волноваться§7.\n§r§fНа проверку может быть вызван §6любой §fигрок§7, §fдаже если он без читов§7!\n\n§r§fВсего нужно выполнить §64 §fпростых пункта§7:\n§61§7. §fСделайте скриншот Вашего экрана с §6летающем текстом §fи с §6проверочным кодом§7;\n§62§7. §fСделайте скриншот §6открытых приложений §fтак§7, §fчтобы все было видно§7;\n§63§7. §fОтправьте все скриншоты в группу §6ВК§7(§fvk§7.§fcom§7/§6deathmc§7.§6club§7) §fиспользуя команду §7/§6проверка §7<§6проверочный_код§7> §fв группе\n\n§fНа этом обучение окончено§7!");
			simpleForm.send(player);
		}
		return false;
	}
}