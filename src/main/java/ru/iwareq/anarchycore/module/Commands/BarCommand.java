package ru.iwareq.anarchycore.module.Commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandEnum;
import cn.nukkit.command.data.CommandParameter;
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
					ScoreboardTask.showScoreboard(player, true);
					player.sendMessage("§l§6• §rСкорбоард успешно §6включены§7!");
				}
				break;

				case "off": {
					ScoreboardTask.showScoreboard(player, false);
					player.sendMessage("§l§6• §rСкорбоард успешно §6выключены§7!");
				}
				break;
			}
		}

		return false;
	}
}
