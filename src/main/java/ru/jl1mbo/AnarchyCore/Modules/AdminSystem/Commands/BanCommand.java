package ru.jl1mbo.AnarchyCore.Modules.AdminSystem.Commands;

import java.util.Arrays;
import java.util.List;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import ru.jl1mbo.AnarchyCore.Manager.Forms.Elements.CustomForm;
import ru.jl1mbo.AnarchyCore.Modules.AdminSystem.AdminAPI;
import ru.jl1mbo.AnarchyCore.Utils.Utils;

public class BanCommand extends Command {

	public BanCommand() {
		super("ban", "§rБлокировка Аккаунта");
		this.setPermission("Command.Ban");
		this.commandParameters.clear();
		this.commandParameters.put("ban", new CommandParameter[] {CommandParameter.newType("player", CommandParamType.TARGET)});
	}

	public static void openBanPlayerForm(Player player, String targetName) {
		List<String> reasonList = Arrays.asList(new String[]{"3.1 (Читы)", "3.2 (Скин)", "3.3 (Багоюз)", "3.4 (Само-признание)"});
		CustomForm customForm = new CustomForm("Блокировка Аккаунта §6" + targetName);
		customForm.addDropDown("§l§6• §rПричина блокировки Аккаунта§7:", reasonList);
		customForm.addLabel("§l§6• §rВремя блокировки Аккаунта§7:");
		customForm.addSlider("§6Минуты§7", 0, 60, 5, 0);
		customForm.addSlider("§6Часы§7", 0, 24, 1, 0);
		customForm.addSlider("§6Дни§7", 0, 30, 1, 0);
		customForm.send(player, (targetPlayer, targetForm, data) -> {
			if (data == null) return;
			if (AdminAPI.isBanned(targetName)) {
				player.sendMessage(AdminAPI.PREFIX + "Игрок §6" + targetName + " §fуже заблокирован§7!");
				return;
			}
			float time = ((float) data.get(2) * 1 * 60) + ((float) data.get(3) * 1 * 3600) + ((float) data.get(4) * 1 * 86400);
			AdminAPI.addBan(targetName, player.getName(), data.get(0).toString(), (int) time);
			Utils.sendMessageAdmins(AdminAPI.PREFIX + "Аккаунт игрока §6" + targetName + " §fбыл заблокирован Администратором §6" + player.getName() + " §fпо причине§7: §6" + data.get(0).toString() + "§7!", 1);
		});
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (!player.hasPermission(this.getPermission())) {
				return false;
			}
			if (args.length != 1) {
				player.sendMessage("§l§6• §rИспользование §7- /§6ban §7(§6игрок§7)");
				return true;
			}
			String targetName = Utils.implode(args, 0);
			if (AdminAPI.isBanned(targetName)) {
				player.sendMessage(AdminAPI.PREFIX + "Игрок §6" + targetName + " §fуже заблокирован§7!");
				return true;
			}
			Player target = Server.getInstance().getPlayer(targetName);
			if (target == null) {
				AdminAPI.sendSearchPlayerForm(player, targetName);
				return true;
			}
			openBanPlayerForm(player, target.getName());
		}
		return false;
	}
}