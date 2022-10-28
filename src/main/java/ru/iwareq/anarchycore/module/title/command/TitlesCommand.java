package ru.iwareq.anarchycore.module.title.command;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import ru.contentforge.formconstructor.form.SimpleForm;
import ru.iwareq.anarchycore.module.title.TitleAPI;
import ru.iwareq.anarchycore.module.title.Titles;
import ru.iwareq.anarchycore.module.title.manager.TitleManager;

public class TitlesCommand extends Command {

	public TitlesCommand() {
		super("titles", "Список ваших титулов");
	}

	@Override
	public boolean execute(CommandSender sender, String alias, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			TitleManager manager = TitleAPI.getManager(player);
			this.sendListPrefixes(player, manager);
		}

		return false;
	}

	private void sendListPrefixes(Player player, TitleManager manager) {
		SimpleForm simpleForm = new SimpleForm("Титулы");
		Titles current = manager.getCurrentTitle();
		simpleForm.addContent("Выберите категорию титулов, которую хотите просмотреть"
				+ "\nТекущий титул: " + (current == null ? "нету" : current.getName()));
		Titles.getAll().keySet().forEach(type ->
				simpleForm.addButton(type.getName() + "\n" + manager.getUnlockedTitles().get(type).size() + "/" + Titles.getAllByType(type).size(), (p, button) -> this.sendUnlockedListPrefixes(p, type, manager)));

		simpleForm.addButton("Скрыть титул", (p, button) -> manager.setCurrentTitle(null));

		simpleForm.send(player);
	}

	private void sendUnlockedListPrefixes(Player player, TitleAPI.Type type, TitleManager manager) {
		SimpleForm simpleForm = new SimpleForm("Титулы");
		manager.getUnlockedTitles().get(type).forEach(prefix -> {
			simpleForm.addButton(prefix.getName() + "\nНажмите чтобы выбрать",
					(p, button) -> manager.setCurrentTitle(prefix));
		});

		simpleForm.addButton("Назад", (p, button) -> sendListPrefixes(player, manager));
		simpleForm.send(player);
	}
}
