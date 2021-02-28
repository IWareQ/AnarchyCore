package ru.jl1mbo.AnarchyCore.CommandsHandler;

import java.util.List;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import ru.jl1mbo.AnarchyCore.Manager.Forms.Elements.CustomForm;
import ru.jl1mbo.AnarchyCore.Manager.Forms.Elements.SimpleForm;
import ru.jl1mbo.AnarchyCore.SystemProcessorHandler.Permissions.PermissionAPI;
import ru.jl1mbo.AnarchyCore.Utils.Utils;

public class AdminPanelCommand extends Command {

	public AdminPanelCommand() {
		super("adm", "§rПанель Администрирования");
		this.setPermission("Command.AdminPanel");
		this.commandParameters.clear();
		this.commandParameters.put("default", new CommandParameter[] {new CommandParameter("user", CommandParamType.TARGET, false)});
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
				customForm.addInput("Steve");
				customForm.send(player, (targetPlayer, targetForm, data) -> {
					if (data == null) return;
					if (data.get(1).toString().equals(" ") || data.get(1).toString().equals("")) {
						customForm.addLabel("\n\n§fА никнейм то где?");
						customForm.send(player);
						return;
					}
					sendSearchPlayerForm(player, data.get(1).toString());
				});
			} else {
				Player target = Server.getInstance().getPlayer(args[0]);
				if (target != null) {
					sendAdminPanelForm(player, target.getName());
					return true;
				}
				sendSearchPlayerForm(player, args[0]);
			}
		}
		return false;
	}

	private static void sendSearchPlayerForm(Player player, String playerName) {
		List<String> players = Utils.getPlayerToName(playerName);
		SimpleForm simpleForm = new SimpleForm("Панель Администрирования", "Здесь показанны все никнеймы начинающиеся с " + playerName);
		for (String playerList : players) {
			simpleForm.addButton("§l§6" + playerList + "\n§rНажмите чтобы перейти");
		}
		simpleForm.send(player, (targetPlayer, targetForm, data) -> {
			if (data == -1) return;
			sendAdminPanelForm(player, players.get(data));
		});
	}

	private static void sendAdminPanelForm(Player player, String targetName) {
		Player target = Server.getInstance().getPlayerExact(targetName);
		SimpleForm simpleForm = new SimpleForm("Панель Администрирования",
											   "§l§6• §rИгрок§7: §6" + targetName + "\n§l§6• §rРанг§7: " + PermissionAPI.getAllGroups().get(PermissionAPI.getGroup(
													   targetName)).getGroupName() + "\n§l§6• §rУстройство§7: §6" + target == null ? "§cИгрок должен быть в сети" : target.getLoginChainData().getDeviceModel() +
											   "\n\n§rВыберите §6нужный пункт §fМеню§7:");
		simpleForm.addButton("Блокировка Аккаунта");
		simpleForm.addButton("Блокировка Чата");
		simpleForm.addButton("Просмотреть Инвентарь");
		simpleForm.send(player, (targetPlayer, targetForm, data) -> {
			if (data == -1) return;
		});
	}
}