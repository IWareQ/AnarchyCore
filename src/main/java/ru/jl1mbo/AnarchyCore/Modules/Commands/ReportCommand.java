package ru.jl1mbo.AnarchyCore.Modules.Commands;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import ru.jl1mbo.AnarchyCore.Manager.Forms.Elements.CustomForm;
import ru.jl1mbo.AnarchyCore.Modules.Cooldown.CooldownAPI;
import ru.jl1mbo.AnarchyCore.Utils.Utils;

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
			ArrayList<String> playersList = new ArrayList<>();
			for (Player players : Server.getInstance().getOnlinePlayers().values()) {
				playersList.add(players.getName());
			}
			CustomForm customForm = new CustomForm("Пожаловаться на Игрока");
			customForm.addDropDown("§l§6• §rВыберите игрока§7:", playersList, playersList.indexOf(player.getName()));
			customForm.addInput("§l§6• §rВведите причину§7:", "Он меня ударил :0");
			customForm.send(player, (targetPlayer, targetForm, data) -> {
				if (data == null) {
					return;
				}
				String targetName = data.get(0).toString();
				String reason = data.get(1).toString();
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