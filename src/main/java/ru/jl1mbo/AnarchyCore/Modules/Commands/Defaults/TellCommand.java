package ru.jl1mbo.AnarchyCore.Modules.Commands.Defaults;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.level.Sound;
import ru.jl1mbo.AnarchyCore.Utils.Utils;

public class TellCommand extends Command {

	public TellCommand() {
		super("tell", "§rОтправить Личное Сообщение");
		this.commandParameters.clear();
		this.commandParameters.put("tell", new CommandParameter[] {CommandParameter.newType("player", CommandParamType.TARGET), CommandParameter.newType("message", CommandParamType.STRING)});
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (args.length < 2) {
			sender.sendMessage("§l§6• §rИспользование §7- /§6tell §7(§6игрок§7) (§6текст§7)");
			return true;
		}
		Player target = Server.getInstance().getPlayer(args[0]);
		if (target == null) {
			sender.sendMessage("§l§6• §rИгрок §6" + args[0] + " §fне в сети§7!");
			return true;
		}
		sender.sendMessage("§7(§6Вы §7-> §6" + target.getName() + "§7) §f" + Utils.implode(args, 1));
		target.sendMessage("§7(§6" + sender.getName() + " §7-> §6Вы§7) §f" + Utils.implode(args, 1));
		target.sendPopup("Новое Личное Сообщение§7!");
		target.getLevel().addSound(target, Sound.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, 1, 1, target);
		return false;
	}
}