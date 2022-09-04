package ru.iwareq.anarchycore.module.AdminSystem.Commands;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import ru.iwareq.anarchycore.manager.Forms.Elements.CustomForm;
import ru.iwareq.anarchycore.module.AdminSystem.AdminAPI;
import ru.iwareq.anarchycore.util.Utils;

import java.util.Arrays;
import java.util.List;

public class MuteCommand extends Command {

	public MuteCommand() {
		super("mute", "§rБлокировка Чата");
		this.setPermission("Command.Mute");
		this.commandParameters.clear();
		this.commandParameters.put("mute", new CommandParameter[]{CommandParameter.newType("player", CommandParamType.TARGET)});
	}

	public static void openMutePlayerForm(Player player, String targetName) {
		List<String> reasonList = Arrays.asList("2.1 (Обсуж. действий перносала)", "2.2 (Дискриминация)", "2.3 (Пропаганда)", "2.4 (Оскорбления)", "2.5 (Спам, флуд)", "2.6 (Реклама)");
		CustomForm customForm = new CustomForm("Блокировка Чата §6" + targetName);
		customForm.addDropDown("§l§6• §rПричина блокировки чата§7:", reasonList);
		customForm.addLabel("§l§6• §rВремя блокировки чата§7:");
		customForm.addSlider("§6Минуты§7", 0, 60, 5, 0);
		customForm.addSlider("§6Часы§7", 0, 24, 1, 0);
		customForm.addSlider("§6Дни§7", 0, 30, 1, 0);
		customForm.send(player, (targetPlayer, targetForm, data) -> {
			if (data == null) {
				return;
			}
			if (AdminAPI.isMuted(targetName)) {
				player.sendMessage(AdminAPI.PREFIX + "Чат игрока §6" + targetName + " §fуже заблокирован§7!");
				return;
			}
			float time = ((float) data.get(2) * 1 * 60) + ((float) data.get(3) * 1 * 3600) + ((float) data.get(4) * 1 * 86400);
			AdminAPI.addMute(targetName, player.getName(), data.get(0).toString(), (int) time);
			player.sendMessage(AdminAPI.PREFIX + "Чат игрока §6" + targetName + " §fбыл успешно заблокирован по причине §6" + data.get(0).toString() + "§7!");
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
				player.sendMessage("§l§6• §rИспользование §7- /§6mute §7(§6игрок§7)");
				return true;
			}
			String targetName = Utils.implode(args, 0);
			if (AdminAPI.isMuted(targetName)) {
				player.sendMessage(AdminAPI.PREFIX + "Чат игрока §6" + targetName + " §fуже заблокирован§7!");
				return true;
			}
			Player target = Server.getInstance().getPlayer(targetName);
			if (target == null) {
				AdminAPI.sendSearchPlayerForm(player, targetName);
				return true;
			}
			openMutePlayerForm(player, target.getName());
		}
		return false;
	}
}