package ru.jl1mbo.AnarchyCore.Modules.AdminSystem.Commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import ru.jl1mbo.AnarchyCore.Manager.Forms.Elements.CustomForm;
import ru.jl1mbo.AnarchyCore.Modules.AdminSystem.AdminAPI;
import ru.jl1mbo.AnarchyCore.Utils.Utils;

public class UnBanCommand extends Command {

	public UnBanCommand() {
		super("unban", "§rСнять блокировку аккаунта");
		this.setPermission("Command.UnBan");
		this.commandParameters.clear();
		this.commandParameters.put("unban", new CommandParameter[] {CommandParameter.newType("player", CommandParamType.TARGET)});
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player)sender;
			if (!player.hasPermission(this.getPermission())) {
				return false;
			}
			if (args.length < 1) {
				player.sendMessage("§l§6• §rИспользование §7- /§6unban §7(§6игрок§7)");
				return true;
			}
			String targetName = Utils.implode(args, 0);
			if (AdminAPI.isBanned(targetName)) {
				openUnBanPlayerForm(player, targetName);
			} else {
				player.sendMessage(AdminAPI.PREFIX + "Аккаунт игрока §6" + targetName + " §fне заблокирован§7!");
			}
		} else {
			if (args.length < 1) {
				sender.sendMessage("§l§6• §rИспользование §7- /§6unban §7(§6игрок§7)");
				return true;
			}
			String targetName = Utils.implode(args, 0);
			if (AdminAPI.isBanned(targetName)) {
				sender.sendMessage(AdminAPI.PREFIX + "Аккаунт игрока §6" + targetName + " §fуспешно разблокирован§7!");
				AdminAPI.removeBan(targetName, sender.getName(), "buy");
			} else {
				sender.sendMessage(AdminAPI.PREFIX + "Аккаунт игрока §6" + targetName + " §fне заблокирован§7!");
			}
		}
		return false;
	}

	public static void openUnBanPlayerForm(Player player, String targetName) {
		CustomForm customForm = new CustomForm("Разблокировка аккаунта");
		customForm.addInput("§l§6• §rВведите причину разблокировки§7:", "Причина разблокировки");
		customForm.send(player, (targetPlayer, targetForm, data) -> {
			if (data == null) return;
			String reason = data.get(0).toString();
			if (reason == null || reason.equals("")) {
				player.sendMessage(AdminAPI.PREFIX + "Укажите §6причину §fразблокировки§7!");
				return;
			}
			player.sendMessage(AdminAPI.PREFIX + "Аккаунт игрока §6" + targetName + " §fуспешно разблокирован§7!");
			AdminAPI.removeBan(targetName, player.getName(), reason);
		});
	}
}