package ru.jl1mbo.AnarchyCore.Modules.AdminSystem.Commands;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import ru.jl1mbo.AnarchyCore.Manager.Forms.Elements.CustomForm;
import ru.jl1mbo.AnarchyCore.Modules.AdminSystem.AdminAPI;

public class AdminPanelCommand extends Command {

	public AdminPanelCommand() {
		super("adm", "§rПанель Администрирования");
		this.setPermission("Command.AdminPanel");
		this.commandParameters.clear();
		this.commandParameters.put("adminpanel", new CommandParameter[] {CommandParameter.newType("player", CommandParamType.TARGET)});
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (!player.hasPermission(this.getPermission())) {
				return false;
			}
			if (args.length == 0) {
				CustomForm customForm = new CustomForm("Панель Администрирования");
				customForm.addLabel("Для начала Вам надо §6ввести никнейм игрока §fс которым хотите §6работать§7.\n\n§fДостаточно ввести §6первые 3 §fбуквы никнейма§7.");
				customForm.addInput("", "Steve");
				customForm.send(player, (targetPlayer, targetForm, data) -> {
					if (data == null) return;
					String targetName = data.get(0).toString();
					if (targetName == null || targetName.equals("")) {
						player.sendMessage(AdminAPI.PREFIX + "Введите §6никнейм игрока §fс которым хотите работать§7!");
						return;
					}
					AdminAPI.sendSearchPlayerForm(player, targetName);
				});
			} else {
				Player target = Server.getInstance().getPlayer(args[0]);
				if (target != null) {
					AdminAPI.sendAdminPanelForm(player, target.getName());
					return true;
				}
				AdminAPI.sendSearchPlayerForm(player, args[0]);
			}
		}
		return false;
	}
}