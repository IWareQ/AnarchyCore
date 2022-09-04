package ru.iwareq.anarchycore.module.AdminSystem.Commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import ru.iwareq.anarchycore.manager.Forms.Elements.SimpleForm;

public class CheatCheckTutorialCommand extends Command {

	public CheatCheckTutorialCommand() {
		super("cct", "§rИнформация по прохождению Проверки");
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			SimpleForm simpleForm = new SimpleForm("Как пройти Проверку§7?");
			simpleForm.setContent("Первое что Вам надо сделать §7- §fэто §6не волноваться§7.\n§fНа проверку может быть вызван §6любой §fигрок§7, §fдаже если он без читов§7.\n\n§fВсего нужно выполнить §64 §fпростых пункта§7:\n§61§7. §fСделайте скриншот Вашего экрана с §6летающем текстом §fи с §6проверочным кодом§7;\n§62§7. §fСделайте скриншот §6открытых приложений §fтак§7, §fчтобы все было видно§7;\n§63§7. §fОтправьте все скриншоты в группу §6ВК§7(§fvk§7.§fcom§7/§6deathmc§7.§6club§7) §fиспользуя команду §7/§6проверка §7<§6проверочный_код§7> §fв группе\n\n§fНа этом обучение окончено§7!");
			simpleForm.send(player);
		}
		return false;
	}
}