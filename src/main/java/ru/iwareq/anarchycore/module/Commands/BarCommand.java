package ru.iwareq.anarchycore.module.Commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandEnum;
import cn.nukkit.command.data.CommandParameter;
import ru.iwareq.anarchycore.AnarchyCore;
import ru.iwareq.anarchycore.task.ScoreboardTask;

public class BarCommand extends Command {

	public BarCommand() {
		super("bar", "§rВключить§7/§rвыключить скорбоард");
		this.commandParameters.clear();
		this.commandParameters.put("bar", new CommandParameter[]{
				CommandParameter.newEnum("action", new CommandEnum("BarAction", "on", "off"))
		});
	}

	@Override()
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length == 0) {
				player.sendMessage("§l§6• §rИспользование §7- /§6bar on/off");
				return true;
			}

			switch (args[0]) {
				case "on": {
					ScoreboardTask.getInstance().showScoreboard(player);
					player.sendMessage("§l§6• §rСкорбоард успешно §6включен§7!");
				}
				break;

				case "off": {
					ScoreboardTask.getInstance().hideScoreboard(player);
					player.sendMessage("§l§6• §rСкорбоард успешно §6выключен§7!");
				}
				break;
			}
		}

		return false;
	}
}
