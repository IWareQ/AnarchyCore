package Anarchy.Module.Commands;

import Anarchy.AnarchyMain;
import Anarchy.Utils.SQLiteUtils;
import FormAPI.Forms.Elements.ModalForm;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class ResyncCommand extends Command {

	public ResyncCommand() {
		super("resync", "§l§fВводить при вайпе");
		this.setPermission("Command.Resync");
		this.commandParameters.clear();
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player)sender;
			if (!player.hasPermission("Command.Resync")) {
				return false;
			}
			new ModalForm("§l§fВернуть базу данных в начальное положение", "§l§fЭта команда очищает базу данных игроков§7, §fлучше это вводить при вайпе§7!nn§l§fЧто именно очищается§7:n§l§6• §fМонеты игроков§7;n§l§6• §fВремя§7, §fнаигранное Игроками§7;", "§l§fОбновить Базу Данныхn§l§6ПРОГРЕСС ВЕРНУТЬ НЕ ВОЗМОЖНО", "§l§fЯ передумал").send(player, (targetPlayer, targetForm, data) -> {
				if (data == -1) return;
				if (data == 0) {
					SQLiteUtils.query("Users.db", "UPDATE `USERS` SET `Money` = '0', `Gametime` = '0';");
					player.sendMessage(AnarchyMain.PREFIX + "§fБаза данных успешно обновленна§7!");
				}
				if (data == 1) {
					player.sendMessage(AnarchyMain.PREFIX + "Обновленте базы данных отменено§7!");
				}
			});
		}
		return false;
	}
}