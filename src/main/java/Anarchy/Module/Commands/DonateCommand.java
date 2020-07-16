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
		new SimpleForm("Описание Донат Услуг", " ").addButton("§l§6Вип").addButton("§l§aПремиум").addButton("§l§dEnigma").addButton("§l§cHydra").send(player, (target, form, data)->{
			if (data == -1) return;
			if (data == 0) {
				new SimpleForm("§fОписание §7- §l§6Вип", "Префикс §7- (§6Вип§7)\n§7• §6/food §7- §f восстановить голод\n§7• §fКоличество приватов §7- §63").send(player);
			}
			if (data == 1) {
				new SimpleForm("§fОписание §7- §l§aПремиум", "Префикс §7- (§aПремиум§7)\n§7• §6/food §7- §f восстановить голод\n§7• §6/heal §7- §f восстановить здоровье\n§7• §6/repair §7- §f починить предмет в руке\n§7• §fКоличество приватов §7- §63").send(player);
			}
			if (data == 2) {
				new SimpleForm("§fОписание §7- §l§dEnigma", "Префикс §7- (§dEnigma§7)\n§7• §6/food §7- §f восстановить голод\n§7• §6/heal §7- §f восстановить здоровье\n§7• §6/repair §7- §f починить предмет в руке\n§7• §6/day §7- §f сменить §9Ночь §fна §eДень\n§7• §6/night §7- §f сменить §eДень §fна §9Ночь\n§7• §fКоличество приватов §7- §63\n§7• §fКоличество точек дома §7- §61").send(player);
			}
			if (data == 3) {
				new SimpleForm("§fОписание §7- §l§cHydra", "Префикс §7- (§cHydra§7)\n§7• §6/food §7- §f восстановить голод\n§7• §6/heal §7- §f восстановить здоровье\n§7• §6/repair §7- §f починить предмет в руке\n§7• §6/day §7- §f сменить §9Ночь §fна §eДень\n§7• §6/night §7- §f сменить §eДень §fна §9Ночь\n§7• §6/near §7- §f узнать кто рядом с тобой в радиусе §e70 §fблоков\n§7• §fКоличество приватов §7- §64\n§7• §fКоличество точек дома §7- §61").send(player);
			}
		});
		return false;
	}
}