package ru.iwareq.anarchycore.module.Commands.Defaults;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.level.Sound;
import ru.iwareq.anarchycore.module.AdminSystem.AdminAPI;
import ru.iwareq.anarchycore.util.Utils;

public class TellCommand extends Command {

	public TellCommand() {
		super("tell", "§rОтправить Личное Сообщение");
		this.commandParameters.clear();
		this.commandParameters.put("tell", new CommandParameter[]{CommandParameter.newType("player", CommandParamType.TARGET), CommandParameter.newType("message", CommandParamType.MESSAGE)});
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (AdminAPI.isMuted(player.getName())) {
				player.sendMessage("§l§6• §rВо время мута запрещено писать личные сообщения§7!");
				return false;
			}
			if (args.length < 2) {
				player.sendMessage("§l§6• §rИспользование §7- /§6tell §7(§6игрок§7) (§6текст§7)");
				return true;
			}
			Player target = Server.getInstance().getPlayer(args[0]);
			if (target == null) {
				player.sendMessage("§l§6• §rИгрок §6" + args[0] + " §fне в сети§7!");
				return true;
			}
			player.sendMessage("§7(§6Вы §7-> §6" + target.getName() + "§7) §f" + Utils.implode(args, 1));
			target.sendMessage("§7(§6" + player.getName() + " §7-> §6Вы§7) §f" + Utils.implode(args, 1));
			target.sendPopup("Новое Личное Сообщение");
			target.getLevel().addSound(target, Sound.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, 1, 1, target);
		}
		return false;
	}
}