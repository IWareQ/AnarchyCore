package ru.jl1mbo.AnarchyCore.Modules.AdminSystem.Commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import ru.jl1mbo.AnarchyCore.Manager.Forms.Elements.CustomForm;
import ru.jl1mbo.AnarchyCore.Modules.AdminSystem.AdminAPI;
import ru.jl1mbo.AnarchyCore.Utils.Utils;

public class UnMuteCommand extends Command {

	public UnMuteCommand() {
		super("unmute", "§r§fСнять блокировку чата");
		this.setPermission("Command.UnMute");
		this.commandParameters.clear();
		this.commandParameters.put("unmute", new CommandParameter[]{CommandParameter.newType("player", CommandParamType.TARGET)});
	}

	public static void openUnMutePlayerForm(Player player, String targetName) {
		CustomForm customForm = new CustomForm("Разблокировка чата");
		customForm.addInput("§l§6• §rВведите причину разблокировки§7:", "Причина разблокировки");
		customForm.send(player, (targetPlayer, targetForm, data) -> {
			if (data == null) {
				return;
			}
			String reason = data.get(0).toString();
			if (reason == null || reason.equals("")) {
				player.sendMessage(AdminAPI.PREFIX + "Укажите §6причину §fразблокировки§7!");
				return;
			}
			player.sendMessage(AdminAPI.PREFIX + "Чат игрока §6" + targetName + " §fуспешно разблокирован§7!");
			AdminAPI.removeMute(targetName, player.getName(), reason);
		});
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (!player.hasPermission(this.getPermission())) {
				return false;
			}
			if (args.length < 1) {
				player.sendMessage("§l§6• §rИспользование §7- /§6unmute §7(§6игрок§7)");
				return true;
			}
			String targetName = Utils.implode(args, 0);
			if (AdminAPI.isMuted(targetName)) {
				openUnMutePlayerForm(player, targetName);
			} else {
				player.sendMessage(AdminAPI.PREFIX + "Чат игрока §6" + targetName + " §fне заблокирован§7!");
			}
		} else {
			if (args.length < 1) {
				sender.sendMessage("§l§6• §rИспользование §7- /§6unmute §7(§6игрок§7)");
				return true;
			}
			String targetName = Utils.implode(args, 0);
			if (AdminAPI.isMuted(targetName)) {
				sender.sendMessage(AdminAPI.PREFIX + "Чат игрока §6" + targetName + " §fуспешно разблокирован§7!");
				AdminAPI.removeMute(targetName, sender.getName(), "buy");
			} else {
				sender.sendMessage(AdminAPI.PREFIX + "Чат игрока §6" + targetName + " §fне заблокирован§7!");
			}
		}
		return false;
	}
}